package com.ltei.audiodownloader.ui

import javafx.geometry.Insets
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import kotlin.math.roundToInt

fun Color(r: Int, g: Int, b: Int, a: Int): Color {
    if (r !in 0..255) throw IllegalArgumentException("r value ($r) not in range 0..255")
    if (g !in 0..255) throw IllegalArgumentException("g value ($g) not in range 0..255")
    if (b !in 0..255) throw IllegalArgumentException("b value ($b) not in range 0..255")
    if (a !in 0..255) throw IllegalArgumentException("a value ($a) not in range 0..255")
    return Color.color(r / 255.0, g / 255.0, b / 255.0, a / 255.0)
}

fun Color(r: Int, g: Int, b: Int): Color = Color(r, g, b, 255)
fun Color(lum: Int): Color = Color(lum, lum, lum)

fun Color(hex: String): Color = when {
    hex.length == 1 -> Color(16 * Integer.valueOf(hex.substring(0..0), 16))
    hex.length == 2 -> Color(Integer.valueOf(hex.substring(0..1), 16))
    hex.length == 3 -> Color(
        16 * Integer.valueOf(hex.substring(0..0), 16),
        16 * Integer.valueOf(hex.substring(1..1), 16),
        16 * Integer.valueOf(hex.substring(2..2), 16)
    )
    hex.length == 4 -> Color(
        16 * Integer.valueOf(hex.substring(0..0), 16),
        16 * Integer.valueOf(hex.substring(1..1), 16),
        16 * Integer.valueOf(hex.substring(2..2), 16),
        16 * Integer.valueOf(hex.substring(3..3), 16)
    )
    hex.length == 6 -> Color(
        Integer.valueOf(hex.substring(0..1), 16),
        Integer.valueOf(hex.substring(2..3), 16),
        Integer.valueOf(hex.substring(4..5), 16)
    )
    hex.length == 8 -> Color(
        Integer.valueOf(hex.substring(0..1), 16),
        Integer.valueOf(hex.substring(2..3), 16),
        Integer.valueOf(hex.substring(4..5), 16),
        Integer.valueOf(hex.substring(6..7), 16)
    )
    else -> throw IllegalArgumentException("Wrong hex format.")
}

fun Color.toBackgroundFill(
    radii: CornerRadii = CornerRadii.EMPTY,
    insets: Insets = Insets.EMPTY
) = BackgroundFill(this, radii, insets)

fun Color.toHexString() = String.format(
    "#%02x%02x%02x",
    (red * 255).roundToInt(),
    (green * 255).roundToInt(),
    (blue * 255).roundToInt()
)

fun Color.toTintStyleString(): String {
    val rgb = "rgb(${(red * 255).roundToInt()}, ${(green * 255).roundToInt()}, ${(blue * 255).roundToInt()})"
    return "-fx-base: $rgb;\n" +
            "-fx-background: $rgb;"
}