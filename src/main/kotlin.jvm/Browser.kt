package kotlinx.browser

import kotlinx.dom.*
import org.w3c.dom.*

private var _document: Document? = null

/**
 *
 * Provides a simple typesafe API for accessing the browser context which can be used when Kotlin is compiled
 * into JavaScript or when Kotlin is used on a JVM for
 * example using [JavaFX](https://github.com/koolapp/koolapp/tree/master/koolapp-javafx),
 * custom browser widgets such as in SWT or Qt, with remote control browser APIs like GWT or Selenium.
 */
public var document: Document
    get() = _document ?: createDocument()
    set(value) {
        _document = value
    }
