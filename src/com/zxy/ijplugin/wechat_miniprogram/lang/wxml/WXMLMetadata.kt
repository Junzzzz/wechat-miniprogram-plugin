package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

data class WXMLElementDescriptor(
        val name: String,
        val attributeDescriptors: Array<WXMLElementAttributeDescriptor> = emptyArray(),
        val events: Array<String> = emptyArray(),
        val canOpen: Boolean = true,
        val canClose: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLElementDescriptor

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class WXMLElementAttributeDescriptor(
        val key: String,
        val types: Array<ValueType>,
        val default: Any? = null,
        val required: Boolean = false,
        val enums: Array<String> = emptyArray(),
        val requiredInEnums: Boolean = true
) {

    enum class ValueType {
        STRING, NUMBER, BOOLEAN,
        COLOR
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLElementAttributeDescriptor

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

typealias E = WXMLElementDescriptor
typealias A = WXMLElementAttributeDescriptor
typealias T = WXMLElementAttributeDescriptor.ValueType

object WXMLMetadata {
    val ELEMENT_DESCRIPTORS = arrayOf(
            E(
                    "view",
                    arrayOf(
                            A(
                                    "hover-class", arrayOf(T.STRING), "none"
                            ),
                            A(
                                    "hover-stop-propagation", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "hover-start-time", arrayOf(T.NUMBER), 50
                            ),
                            A(
                                    "hover-stay-time", arrayOf(T.NUMBER), 400
                            )
                    )
            ),
            E(
                    "cover-image",
                    arrayOf(
                            A(
                                    "src",
                                    arrayOf(T.STRING)
                            )
                    ),
                    arrayOf("load", "error")
            ),
            E(
                    "cover-view",
                    arrayOf(
                            A(
                                    "scroll-top",
                                    arrayOf(
                                            T.STRING,
                                            T.NUMBER
                                    )
                            )
                    )
            ),
            E(
                    "movable-area",
                    arrayOf(
                            A(
                                    "scale-area", arrayOf(T.BOOLEAN), false
                            )
                    )
            ),
            E(
                    "movable-view",
                    arrayOf(
                            A(
                                    "direction", arrayOf(T.STRING), "none"
                            ),
                            A(
                                    "inertia", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "out-of-bounds", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "x", arrayOf(T.NUMBER)
                            ),
                            A(
                                    "y", arrayOf(T.NUMBER)
                            ),
                            A(
                                    "damping", arrayOf(T.NUMBER), 20
                            ),
                            A(
                                    "friction", arrayOf(T.NUMBER), 2
                            ),
                            A(
                                    "disabled", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scale", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scale-min", arrayOf(T.NUMBER), 0.5
                            ),
                            A(
                                    "scale-max", arrayOf(T.NUMBER), 10
                            ),
                            A(
                                    "scale-value", arrayOf(T.NUMBER), 1
                            ),
                            A(
                                    "animation", arrayOf(T.BOOLEAN), true
                            )
                    ),
                    events = arrayOf("change", "scale", "htouchmove", "vtouchmove")
            ),
            E(
                    "scroll-view",
                    arrayOf(
                            A(
                                    "scroll-view", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scroll-y", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "upper-threshold",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    ), 50
                            ),
                            A(
                                    "lower-threshold",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    ), 50
                            ),
                            A(
                                    "scroll-top",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    )
                            ),
                            A(
                                    "scroll-bottom",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    )
                            ),
                            A(
                                    "scroll-into-view", arrayOf(T.STRING)
                            ),
                            A(
                                    "scroll-with-animation", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "enable-back-to-top", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "enable-flex", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scroll-anchoring", arrayOf(T.BOOLEAN), false
                            )
                    ),
                    arrayOf("scrolltoupper", "scrolltolower", "scroll")
            ),
            E(
                    "swiper",
                    arrayOf(
                            A("indicator-dots", arrayOf(T.BOOLEAN), false),
                            A("indicator-color", arrayOf(T.COLOR), "rgba(0, 0, 0, .3)"),
                            A("indicator-active-color", arrayOf(T.COLOR), "#000000"),
                            A("autoplay", arrayOf(T.BOOLEAN), false),
                            A("current", arrayOf(T.NUMBER), 0),
                            A("interval", arrayOf(T.NUMBER), 0),
                            A("interval", arrayOf(T.NUMBER), 5000),
                            A("duration", arrayOf(T.NUMBER), 500),
                            A("circular", arrayOf(T.BOOLEAN), false),
                            A("vertical", arrayOf(T.BOOLEAN), false),
                            A("previous-margin", arrayOf(T.STRING), "0px"),
                            A("next-margin", arrayOf(T.STRING), "0px"),
                            A("display-multiple-items", arrayOf(T.NUMBER), 1),
                            A("skip-hidden-item-layout", arrayOf(T.BOOLEAN), false),
                            A(
                                    "easing-function", arrayOf(T.STRING), "default", false,
                                    arrayOf("default", "linear", "easeInCubic", "easeOutCubic", "easeInOutCubic")
                            )
                    ),
                    arrayOf("change", "transition", "animationfinish")
            ),
            E("swiper-item", arrayOf(A("item-id", arrayOf(T.STRING))))
    )

    val COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS = arrayOf(
            A("id", arrayOf(T.STRING)),
            A("class", arrayOf(T.STRING)),
            A("style", arrayOf(T.STRING)),
            A("hidden", arrayOf(T.BOOLEAN), false)
    )

    val COMMON_ELEMENT_EVENTS = arrayOf(
            "touchstart", "touchmove", "touchcancel", "touchend", "tap", "longpress", "longtap", "transitionend",
            "animationstart", "animationiteration", "animationend", "touchforcechange"
    )

    val INNER_ELEMENT_NAMES = arrayOf("text")
}