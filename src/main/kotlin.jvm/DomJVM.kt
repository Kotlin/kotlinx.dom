
/**
 * JVM specific API implementations using JAXP and so forth which would not be used when compiling to JS
 */
package kotlinx.dom

import org.w3c.dom.*
import org.xml.sax.*
import java.io.*
import java.util.*
import javax.xml.parsers.*
import javax.xml.transform.*
import javax.xml.transform.dom.*
import javax.xml.transform.stream.*

@Deprecated("Use search instead as there is ambigousity with JavaScript", ReplaceWith("this.search(selector)"))
operator fun Document?.get(selector: String): List<Element> = search(selector)

/** Searches for elements using the element name, an element ID (if prefixed with dot) or element class (if prefixed with #) */
fun Document?.search(selector: String): List<Element> {
    val root = this?.documentElement
    return if (root != null) {
        if (selector == "*") {
            elements()
        } else if (selector.startsWith(".")) {
            elements().filter{ it.hasClass(selector.substring(1)) }.toList()
        } else if (selector.startsWith("#")) {
            val id = selector.substring(1)
            val element = this?.getElementById(id)
            return if (element != null)
                arrayListOf(element)
            else
                emptyList()
        } else {
            //  assume its a vanilla element name
            elements(selector)
        }
    } else {
        emptyList()
    }
}

@Deprecated("Use search instead as there is ambigousity with JavaScript", ReplaceWith("this.search(selector)"))
operator fun Element.get(selector: String): List<Element> = search(selector)

/** Searches for elements using the element name, an element ID (if prefixed with dot) or element class (if prefixed with #) */
fun Element.search(selector: String): List<Element> {
    return if (selector == "*") {
        elements()
    } else if (selector.startsWith(".")) {
        elements().filter{ it.hasClass(selector.substring(1)) }.toList()
    } else if (selector.startsWith("#")) {
        val element = this.ownerDocument?.getElementById(selector.substring(1))
        return if (element != null)
            arrayListOf(element)
        else
            emptyList()
    } else {
        //  assume its a vanilla element name
        elements(selector)
    }
}

var Element.id: String
    get() = this.getAttribute("id") ?: ""
    set(value) {
        this.setAttribute("id", value)
        this.setIdAttribute("id", true)
    }

/**
 * Returns the HTML representation of the node
 */
val Node.outerHTML: String
    get() = toXmlString()

/**
 * Returns the HTML representation of the node
 */
val Node.innerHTML: String
    get() = childNodes.outerHTML


@JvmName("toXmlStringNullable")
@Deprecated("use non-nullable version instead", ReplaceWith("this?.toXmlString(xmlDeclaration) ?: \"\""))
fun NodeList?.toXmlString(xmlDeclaration: Boolean = false): String = this?.toXmlString(xmlDeclaration) ?: ""

/** Converts the node list to an XML String */
fun NodeList.toXmlString(xmlDeclaration: Boolean = false): String =
    nodesToXmlString(this.asList(), xmlDeclaration)

/** Converts the collection of nodes to an XML String */
fun nodesToXmlString(nodes: Iterable<Node>, xmlDeclaration: Boolean = false): String {
    return nodes.map { it.toXmlString(xmlDeclaration) }.joinToString()
}

/**
 * Returns the HTML representation of the nodes
 */
val NodeList.outerHTML: String
    get() = asList().map { it.innerHTML }.joinToString("")

/** Returns an [Iterator] of all the next [Element] siblings */
fun Node.nextElements(): List<Element> = nextSiblings().filterIsInstance<Element>()

/** Returns an [Iterator] of all the previous [Element] siblings */
fun Node.previousElements(): List<Element> = previousSiblings().filterIsInstance<Element>()

@Suppress("UNCHECKED_CAST")
fun List<Node>.filterElements(): List<Element> = filter { it.isElement } as List<Element>
fun NodeList.filterElements(): List<Element> = asList().filterElements()


/** Creates a new document with the given document builder*/
fun createDocument(builder: DocumentBuilder): Document {
    return builder.newDocument()
}

/** Creates a new document with an optional DocumentBuilderFactory */
fun createDocument(builderFactory: DocumentBuilderFactory = defaultDocumentBuilderFactory()): Document {
    return createDocument(builderFactory.newDocumentBuilder())
}

/**
 * Returns the default [DocumentBuilderFactory]
 */
fun defaultDocumentBuilderFactory(): DocumentBuilderFactory {
    return DocumentBuilderFactory.newInstance()!!
}

/**
 * Returns the default [DocumentBuilder]
 */
fun defaultDocumentBuilder(builderFactory: DocumentBuilderFactory = defaultDocumentBuilderFactory()): DocumentBuilder {
    return builderFactory.newDocumentBuilder()
}

/**
 * Parses the XML document using the given *file*
 */
fun parseXml(file: File, builder: DocumentBuilder = defaultDocumentBuilder()): Document {
    return builder.parse(file)!!
}

/**
 * Parses the XML document using the given *uri*
 */
fun parseXml(uri: String, builder: DocumentBuilder = defaultDocumentBuilder()): Document {
    return builder.parse(uri)!!
}

/**
 * Parses the XML document using the given *inputStream*
 */
fun parseXml(inputStream: InputStream, builder: DocumentBuilder = defaultDocumentBuilder()): Document {
    return builder.parse(inputStream)!!
}

/**
 * Parses the XML document using the given *inputSource*
 */
fun parseXml(inputSource: InputSource, builder: DocumentBuilder = defaultDocumentBuilder()): Document {
    return builder.parse(inputSource)!!
}


/** Creates a new TrAX transformer */
fun createTransformer(source: Source? = null, factory: TransformerFactory = TransformerFactory.newInstance()!!): Transformer {
    val transformer = if (source != null) {
        factory.newTransformer(source)
    } else {
        factory.newTransformer()
    }
    return transformer!!
}

/** Converts the node to an XML String */
fun Node.toXmlString(): String = toXmlString(false)

/** Converts the node to an XML String */
fun Node.toXmlString(xmlDeclaration: Boolean): String {
    val writer = StringWriter()
    writeXmlString(writer, xmlDeclaration)
    return writer.toString()
}

/** Converts the node to an XML String and writes it to the given [Writer] */
fun Node.writeXmlString(writer: Writer, xmlDeclaration: Boolean): Unit {
    val transformer = createTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, if (xmlDeclaration) "no" else "yes")
    transformer.transform(DOMSource(this), StreamResult(writer))
}
