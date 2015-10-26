package kotlinx.dom

import org.w3c.dom.events.*

public val Event.getCurrentTarget: EventTarget?
    get() = getCurrentTarget()

// TODO we can't use 'type' as the property name in Kotlin so we should fix it in JS
public val Event.eventType: String
    get() = getType()!!
