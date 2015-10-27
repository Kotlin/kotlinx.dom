package kotlinx.dom

import org.w3c.dom.*

var Element.className: String
    get() = classes
    set(value) {
        classes = value
    }
