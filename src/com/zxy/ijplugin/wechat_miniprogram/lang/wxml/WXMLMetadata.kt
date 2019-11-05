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
        STRING, NUMBER, BOOLEAN
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

object WXMLMetadata {
    val ELEMENT_DESCRIPTORS = arrayOf(
            WXMLElementDescriptor(
                    "view",
                    arrayOf(
                            WXMLElementAttributeDescriptor(
                                    "hover-class", arrayOf(WXMLElementAttributeDescriptor.ValueType.STRING), "none"
                            ),
                            WXMLElementAttributeDescriptor(
                                    "hover-stop-propagation", arrayOf(WXMLElementAttributeDescriptor.ValueType.BOOLEAN),
                                    false
                            ),
                            WXMLElementAttributeDescriptor(
                                    "hover-start-time", arrayOf(WXMLElementAttributeDescriptor.ValueType.NUMBER), 50
                            ),
                            WXMLElementAttributeDescriptor(
                                    "hover-stay-time", arrayOf(WXMLElementAttributeDescriptor.ValueType.NUMBER), 400
                            )
                    )
            ),
            WXMLElementDescriptor(
                    "cover-image",
                    arrayOf(
                            WXMLElementAttributeDescriptor(
                                    "src",
                                    arrayOf(WXMLElementAttributeDescriptor.ValueType.STRING)
                            )
                    ),
                    arrayOf("load", "error")
            ),
            WXMLElementDescriptor(
                    "cover-view",
                    arrayOf(
                            WXMLElementAttributeDescriptor(
                                    "scroll-top",
                                    arrayOf(
                                            WXMLElementAttributeDescriptor.ValueType.STRING,
                                            WXMLElementAttributeDescriptor.ValueType.NUMBER
                                    )
                            )
                    )
            )
    )

    val COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS = arrayOf(
            WXMLElementAttributeDescriptor("id", arrayOf(WXMLElementAttributeDescriptor.ValueType.STRING)),
            WXMLElementAttributeDescriptor("class", arrayOf(WXMLElementAttributeDescriptor.ValueType.STRING)),
            WXMLElementAttributeDescriptor("style", arrayOf(WXMLElementAttributeDescriptor.ValueType.STRING)),
            WXMLElementAttributeDescriptor("hidden", arrayOf(WXMLElementAttributeDescriptor.ValueType.BOOLEAN), false)
    )

    val COMMON_ELEMENT_EVENTS = arrayOf(
            "touchstart", "touchmove", "touchcancel", "touchend", "tap", "longpress", "longtap", "transitionend",
            "animationstart", "animationiteration", "animationend", "touchforcechange"
    )
}