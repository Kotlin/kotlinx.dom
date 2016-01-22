package kotlinx.dom

// TODO should not need this - its here for the JS stuff
import org.w3c.dom.*
import java.util.*

// Properties

/** Returns the children of the element as a list */
fun Element?.children(): List<Node> {
    return this?.childNodes?.asList() ?: emptyList()
}

/** Returns the child elements of this element */
fun Element?.childElements(): List<Element> = this?.childNodes?.filterElements() ?: emptyList()

/** Returns the child elements of this element with the given name */
fun Element?.childElements(name: String): List<Element> = this?.childNodes?.filterElements()?.filter { it.nodeName == name } ?: emptyList()

/** The descendant elements of this document */
@Deprecated("Use elements() function instead", ReplaceWith("this.elements()"))
val Document?.elements: List<Element>
    get() = this.elements()

/** The descendant elements of this elements */
@Deprecated("Use elements() function instead", ReplaceWith("this?.elements() ?: emptyList()"))
val Element?.elements: List<Element>
    get() = this?.elements() ?: emptyList()

//@JvmName("elementsNullable")
//@Deprecated("Use non-nullable receiver version elements()", ReplaceWith("this?.elements(localName) ?: emptyList()"))
//fun Element?.elements(localName: String): List<Element> = this?.elements(localName) ?: emptyList()

/** Returns all the descendant elements given the local element name */
fun Element.elements(localName: String = "*"): List<Element> {
    return this.getElementsByTagName(localName).asElementList()
}

/** Returns all the descendant elements given the local element name */
fun Document?.elements(localName: String = "*"): List<Element> {
    return this?.getElementsByTagName(localName)?.asElementList() ?: emptyList()
}

//@JvmName("elementsNullable")
//@Deprecated("Use non-nullable elements function instead", ReplaceWith("this?.elements(namespaceUri, localName) ?: emptyList()"))
//fun Element?.elements(namespaceUri: String, localName: String): List<Element> = this?.elements(namespaceUri, localName) ?: emptyList()

/** Returns all the descendant elements given the namespace URI and local element name */
fun Element.elements(namespaceUri: String, localName: String): List<Element> {
    return this.getElementsByTagNameNS(namespaceUri, localName).asElementList()
}

/** Returns all the descendant elements given the namespace URI and local element name */
fun Document?.elements(namespaceUri: String, localName: String): List<Element> {
    return this?.getElementsByTagNameNS(namespaceUri, localName)?.asElementList() ?: emptyList()
}

//@JvmName("asListNullable")
//@Deprecated("Use non-null function instead with elvis", ReplaceWith("this?.asList() ?: emptyList()"))
//fun NodeList?.asList(): List<Node> = this?.asList() ?: emptyList()

fun NodeList.asList() : List<Node> = NodeListAsList(this)

@Deprecated("Use asElementList() instead", ReplaceWith("this?.asElementList() ?: emptyList()"))
fun NodeList?.toElementList(): List<Element> = this?.asElementList() ?: emptyList()

/**
 * Returns view with assumption that it contains only elements. Will crash in runtime if there are non-element nodes in
 * the list during access such items. So [filterElements] would be better solution.
 */
fun NodeList.asElementList(): List<Element> = if (length == 0) emptyList() else ElementListAsList(this)

private class NodeListAsList(private val delegate: NodeList) : AbstractList<Node>() {
    override val size: Int get() = delegate.length

    override fun get(index: Int): Node = when {
        index in 0..size - 1 -> delegate.item(index)!!
        else -> throw IndexOutOfBoundsException("index $index is not in range [0 .. ${size - 1})")
    }
}

private class ElementListAsList(private val nodeList: NodeList) : AbstractList<Element>() {
    override fun get(index: Int): Element {
        val node = nodeList.item(index)
        if (node == null) {
            throw IndexOutOfBoundsException("NodeList does not contain a node at index: " + index)
        } else if (node.nodeType == Node.ELEMENT_NODE) {
            return node as Element
        } else {
            throw IllegalArgumentException("Node is not an Element as expected but is $node")
        }
    }

    override val size: Int get() = nodeList.length
}

/** Returns an [Iterator] over the next siblings of this node */
fun Node.nextSiblings(): Iterable<Node> = NextSiblings(this)

private class NextSiblings(private var node: Node) : Iterable<Node> {
    override fun iterator(): Iterator<Node> = object : AbstractIterator<Node>() {
        override fun computeNext(): Unit {
            val nextValue = node.nextSibling
            if (nextValue != null) {
                setNext(nextValue)
                node = nextValue
            } else {
                done()
            }
        }
    }
}

/** Returns an [Iterator] over the next siblings of this node */
fun Node.previousSiblings(): Iterable<Node> = PreviousSiblings(this)

private class PreviousSiblings(private var node: Node) : Iterable<Node> {
    override fun iterator(): Iterator<Node> = object : AbstractIterator<Node>() {
        override fun computeNext(): Unit {
            val nextValue = node.previousSibling
            if (nextValue != null) {
                setNext(nextValue)
                node = nextValue
            } else {
                done()
            }
        }
    }
}

var Element.style: String
    get() = this.getAttribute("style") ?: ""
    set(value) {
        this.setAttribute("style", value)
    }

/**
 * it is *true* when [Node.nodeType] is TEXT_NODE or CDATA_SECTION_NODE
 */
val Node.isText : Boolean
    get() = nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE


/**
 * `true` if it's an element node
 */
val Node.isElement: Boolean
    get() = nodeType == Node.ELEMENT_NODE

/** Returns the attribute value or empty string if its not present */
@Deprecated("Use getAttribute with elvis operator", ReplaceWith("this.getAttribute(name) ?: \"\""))
fun Element.attribute(name: String): String {
    return this.getAttribute(name) ?: ""
}

@Deprecated("Use asList().firstOrNull() instead", ReplaceWith("this?.asList()?.firstOrNull()"))
val NodeList?.head: Node?
    get() = this?.asList()?.firstOrNull()

@Deprecated("Use asList().firstOrNull() instead", ReplaceWith("this?.asList()?.firstOrNull()"))
val NodeList?.first: Node?
    get() = this?.asList()?.firstOrNull()

@Deprecated("Use asList().lastOrNull() instead", ReplaceWith("this?.asList()?.lastOrNull()"))
val NodeList?.last: Node?
    get() = this?.asList()?.lastOrNull()

@Deprecated("Use asList().lastOrNull() instead instead", ReplaceWith("last"))
val NodeList?.tail: Node?
    get() = this?.asList()?.lastOrNull()
