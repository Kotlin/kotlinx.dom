package test.browser

import kotlinx.dom.*
import kotlinx.dom.build.*
import kotlin.browser.*

import kotlin.test.*
import org.junit.Test as test

class BrowserTest {

    @test fun accessBrowserDOM() {
        val doc = document
        doc.addElement("html") {
            addElement("body") {
                addElement("h1") {
                    appendText("Hello World!")
                }
                addElement("p") {
                    appendText("This is some text content")
                }
            }
        }

        val h1 = doc.search("h1").first()
        assertEquals("Hello World!", h1.textContent)
    }
}