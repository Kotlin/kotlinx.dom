package kotlinx.dom

import org.w3c.dom.Element
import org.w3c.dom.Document
import org.w3c.dom.DOMTokenList
import org.w3c.dom.HTMLCollection
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.util.*

/** Searches for elements using the element name, an element ID (if prefixed with dot) or element class (if prefixed with #) */
fun Document?.search(selector: String): List<HTMLElement> {
    return this?.querySelectorAll(selector)?.asList()?.filterElements() ?: emptyList()
}

/** Searches for elements using the element name, an element ID (if prefixed with dot) or element class (if prefixed with #) */
fun Element.search(selector: String): List<HTMLElement> {
    return querySelectorAll(selector).asList().filterElements()
}

private class HTMLCollectionListView(val collection: HTMLCollection) : AbstractList<HTMLElement>() {
    override val size: Int get() = collection.length

    override fun get(index: Int): HTMLElement =
            when {
                index in 0..size - 1 -> collection.item(index) as HTMLElement
                else -> throw IndexOutOfBoundsException("index $index is not in range [0 .. ${size - 1})")
            }
}

public fun HTMLCollection.asList(): List<HTMLElement> = HTMLCollectionListView(this)

private class DOMTokenListView(val delegate: DOMTokenList) : AbstractList<String>() {
    override val size: Int get() = delegate.length

    override fun get(index: Int) =
            when {
                index in 0..size - 1 -> delegate.item(index)!!
                else -> throw IndexOutOfBoundsException("index $index is not in range [0 .. ${size - 1})")
            }
}

public fun DOMTokenList.asList(): List<String> = DOMTokenListView(this)
internal fun HTMLCollection.asElementList(): List<HTMLElement> = asList()

@Suppress("UNCHECKED_CAST")
/** Returns an [Iterator] of all the next [Element] siblings */
fun Node.nextElements(): List<HTMLElement> = nextSiblings().filter { it.isElement } as List<HTMLElement>

@Suppress("UNCHECKED_CAST")
/** Returns an [Iterator] of all the previous [Element] siblings */
fun Node.previousElements(): List<HTMLElement> = nextSiblings().filter { it.isElement } as List<HTMLElement>

var Element.id: String
    get() = this.getAttribute("id") ?: ""
    set(value) {
        this.setAttribute("id", value)
    }

@Suppress("UNCHECKED_CAST")
fun List<Node>.filterElements(): List<HTMLElement> = filter { it.isElement } as List<HTMLElement>
fun NodeList.filterElements(): List<HTMLElement> = asList().filterElements()
