package test.dom

import kotlinx.dom.*
import kotlinx.dom.build.*
import kotlinx.browser.*
import kotlin.test.*
import org.junit.Test as test

class DomBuilderTest() {

    @test fun buildDocument() {
        var doc = document

        assertTrue {
            doc.search("grandchild").isEmpty()
        }

        doc.addElement("foo") {
            id = "id1"
            style = "bold"
            classes = "bar"
            addElement("child") {
                id = "id2"
                classes = "another"
                addElement("grandChild") {
                    id = "id3"
                    classes = " bar tiny"
                    appendText("Hello World!")
                // TODO support neater syntax sugar for adding text?
                // += "Hello World!"
                }
                addElement("grandChild2") {
                    id = "id3"
                    classes = "tiny thing bar "
                    appendText("Hello World!")
                // TODO support neater syntax sugar for adding text?
                // += "Hello World!"
                }
            }
        }

        // test css selections on document
        assertEquals(0, doc.search(".doesNotExist").size)
        assertEquals(1, doc.search(".another").size)
        assertEquals(3, doc.search(".bar").size)
        assertEquals(2, doc.search(".tiny").size)

        // element tag selections
        assertEquals(0, doc.search("doesNotExist").size)
        assertEquals(1, doc.search("foo").size)
        assertEquals(1, doc.search("child").size)
        assertEquals(1, doc.search("grandChild").size)

        // id selections
        assertEquals(1, doc.search("#id1").size)
        assertEquals(1, doc.search("#id2").size)
        assertEquals(1, doc.search("#id3").size)

        val root = doc.documentElement
        if (root == null) {
            fail("No root!")
        } else {
            assertTrue {
                root.hasClass("bar")
            }

            // test css selections on element
            assertEquals(0, root.search(".doesNotExist").size)
            assertEquals(1, root.search(".another").size)
            assertEquals(2, root.search(".bar").size)
            assertEquals(2, root.search(".tiny").size)

            // element tag selections
            assertEquals(0, root.search("doesNotExist").size)
            assertEquals(0, root.search("foo").size)
            assertEquals(1, root.search("child").size)
            assertEquals(1, root.search("grandChild").size)

            // id selections
            assertEquals(1, root.search("#id1").size)
            assertEquals(1, root.search("#id2").size)
            assertEquals(1, root.search("#id3").size)

            // iterating through next element siblings
            for (e in root.nextElements()) {
                println("found element: $e")
            }

        }
        val grandChild = doc.search("grandChild").firstOrNull()
        if (grandChild != null) {
            assertEquals("Hello World!", grandChild.textContent)
            assertEquals(" bar tiny", grandChild.getAttribute("class"))

            // test the classSet
            val classSet = grandChild.classSet

            assertTrue(classSet.contains("bar"))
            assertTrue(classSet.contains("tiny"))
            assertTrue(classSet.size == 2 )
            assertFalse(classSet.contains("doesNotExist"))

            // lets add a new class and some existing classes
            grandChild.addClass("bar")
            grandChild.addClass("newThingy")
            assertEquals("bar tiny newThingy", grandChild.classes)

            // remove
            grandChild.removeClass("bar")
            assertEquals("tiny newThingy", grandChild.classes)

            grandChild.removeClass("tiny")
            assertEquals("newThingy", grandChild.classes)

        } else {
            fail("Not an Element $grandChild")
        }
        val child = doc.search("child").firstOrNull()
        if (child != null) {
            val gc1 = child.childElements("grandChild")
            assertEquals(1, gc1.size, "Expected a single child but found $gc1")
            val gc2 = child.childElements("grandChild2")
            assertEquals(1, gc2.size, "Expected a single child but found $gc2")
        } else {
            fail("No child found!")
        }
        val children = doc.documentElement.children()
        assertEquals(1, children.size)

    }
}
