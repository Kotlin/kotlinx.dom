package kotlinx.dom

import org.w3c.dom.*

@Deprecated("Use standard className instead", ReplaceWith("className"))
var Element.classes: String
    get() = className
    set(newValue) {
        className = newValue
    }

var Element.className: String
    get() = this.getAttribute("class") ?: ""
    set(value) {
        this.setAttribute("class", value)
    }
