package test.dom

import kotlinx.browser.document
import kotlinx.dom.build.addElement
import kotlinx.dom.parseXml
import kotlinx.dom.toXmlString
import org.junit.Test
import org.xml.sax.InputSource
import javax.xml.transform.OutputKeys.*
import kotlin.test.assertFalse

class JVMTest {
    @Test fun writeAndParse() {
        var doc = document

        doc.addElement("a") {
            setAttribute("x", "1")
            setAttribute("y", "2")
            addElement("b") {
                setAttribute("x", "3")
            }
            addElement("b")
            addElement("c")
        }

        assertFalse(doc.toXmlString().startsWith("<?xml"))
        assert(doc.toXmlString(true).startsWith("<?xml"))

        assert(parseXml(InputSource(doc.toXmlString(true).reader())).isEqualNode(doc))
        assert(parseXml(InputSource(doc.toXmlString(mapOf(OMIT_XML_DECLARATION to "no", INDENT to "no")).reader())).isEqualNode(doc))
        assertFalse(parseXml(InputSource(doc.toXmlString(mapOf(OMIT_XML_DECLARATION to "no", INDENT to "yes")).reader())).isEqualNode(doc))
    }


}