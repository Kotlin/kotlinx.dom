package test.dom

import kotlinx.dom.*
import kotlinx.dom.build.*
import kotlinx.browser.*
import kotlin.test.*
import org.junit.Test as test

class NextSiblingTest {

    @test fun nextSibling() {
        val doc = document

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
                    id = "id4"
                    classes = "tiny thing bar "
                    appendText("Hello World!")
                // TODO support neater syntax sugar for adding text?
                // += "Hello World!"
                }
            }
        }

        val elems = doc.search("#id3")
        val element = elems.first()
        val elements = element.nextElements()
        val nodes = element.nextSiblings().toList()

        assertEquals(1, elements.size)
        assertEquals(1, nodes.size)
    }
}
