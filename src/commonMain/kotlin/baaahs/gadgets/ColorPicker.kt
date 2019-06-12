package baaahs.gadgets

import baaahs.Color
import baaahs.Gadget
import kotlinx.serialization.Serializable
import kotlin.js.JsName

/** A gadget for picking a single color for a color palette. */
@Serializable
data class ColorPicker(
    /** The name for this color picker. */
    val name: String,

    /** The initial value for this color picker. */
    val initialValue: Color = Color.WHITE
) : Gadget() {

    /** The selected color. */
    @JsName("color")
    var color: Color by updatable("color", initialValue, Color.serializer())
}