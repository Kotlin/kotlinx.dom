package test.dom

import kotlinx.dom.*
import kotlin.*
import kotlin.test.*
import org.w3c.dom.*
import kotlinx.browser.*
import org.junit.Test as test

class DomTest {

    @test fun addText() {
        var doc = document
        assertNotNull(doc, "Should have created a document")

        val e = doc.createElement("foo")
        e + "hello"

        assertEquals("hello", e.textContent)
    }

    @test fun testCreateDocument() {
        var doc = document
        assertNotNull(doc, "Should have created a document")

        val e = doc.createElement("foo")
        assertCssClass(e, "")

        // now lets update the cssClass property
        e.classes = "foo"
        assertCssClass(e, "foo")

        // now using the attribute directly
        e.setAttribute("class", "bar")
        assertCssClass(e, "bar")
    }

    @test fun testAddClassMissing() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class1"

        e.addClass("class1", "class2")

        assertEquals("class1 class2", e.classes)
    }

    @test fun testAddClassPresent() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class2 class1"

        e.addClass("class1", "class2")

        assertEquals("class2 class1", e.classes)
    }

    @test fun testAddClassUndefinedClasses() {
        val e = document.createElement("e")
        e.addClass("class1")
        assertEquals("class1", e.classes)
    }

    @test fun testRemoveClassMissing() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class2 class1"

        e.removeClass("class3")

        assertEquals("class2 class1", e.classes)
    }

    @test fun testRemoveClassPresent1() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class2 class1"

        e.removeClass("class2")

        assertEquals("class1", e.classes)
    }

    @test fun testRemoveClassPresent2() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class2 class1"

        e.removeClass("class1")

        assertEquals("class2", e.classes)
    }

    @test fun testRemoveClassPresent3() {
        val e = document.createElement("e")
        assertNotNull(e)

        e.classes = "class2 class1 class3"

        e.removeClass("class1")

        assertEquals("class2 class3", e.classes)
    }

    @test fun testRemoveClassUndefinedClasses() {
        val e = document.createElement("e")
        e.removeClass("class1")
        assertEquals("", e.classes)
    }

    @test fun testRemoveFromParent() {
        val doc = document

        val parent = doc.createElement("pp")
        val child = doc.createElement("cc")
        parent.appendChild(child)

        assertEquals(parent, child.parentNode)
        assertEquals(listOf(child), parent.childNodes.asList())

        child.removeFromParent()

        assertNull(child.parentNode)
        assertEquals(emptyList<Node>(), parent.childNodes.asList())

        child.removeFromParent()
        assertNull(child.parentNode)
    }

    @test fun testRemoveFromParentOrphanNode() {
        val child = document.createElement("cc")

        child.removeFromParent()

        assertNull(child.parentNode)
    }


    @test fun testChildElements() {
        val doc = document
        val parent = doc.createElement("a")
        val child1 = doc.createElement("b")
        val child2 = doc.createElement("b")
        val child3 = doc.createElement("c")

        parent + child1
        parent + child2
        parent + child3

        assertEquals(listOf(child1, child2, child3), parent.childElements())
        assertEquals(listOf(child1, child2), parent.childElements("b"))
        assertEquals(listOf(child3), parent.childElements("c"))

        assertEquals(child1, parent.firstChildElement())
        assertEquals(null, child1.firstChildElement())
        assertEquals(null, null.firstChildElement())

        assertEquals(child1, parent.firstChildElement("b"))
        assertEquals(child3, parent.firstChildElement("c"))
        assertEquals(null, parent.firstChildElement("d"))
        assertEquals(null, null.firstChildElement("b"))
    }

    private fun assertCssClass(e: Element, value: String?): Unit {
        val cl = e.classes
        val cl2 = e.getAttribute("class") ?: ""

        assertEquals(value, cl, "value of element.cssClass")
        assertEquals(value, cl2, "value of element.getAttribute(\"class\")")
    }

}
