package k2ddt.render.dto

import java.lang.Float.max
import java.lang.Float.min

/**
 * Color data class
 *
 * Holds data to represent an RGBA color.
 *
 * @param r red component.
 * @param g green component.
 * @param b blue component.
 * @param a alpha component.
 */
data class Color(var r: Float, var g: Float, var b: Float, var a: Float) {

    /**
     * Creates a color with all components set to provided value.
     * @param all all components.
     */
    constructor(all: Float) : this(all, all, all, all)

    init {
        r = clampedIfNeeded(r)
        g = clampedIfNeeded(g)
        b = clampedIfNeeded(b)
        a = clampedIfNeeded(a)
    }

    /**
     * Clamps the values to stay between 0-1 or 0-255.
     * @param value the value to clamp.
     */
    private fun clampedIfNeeded(value: Float): Float {
        return if (value > 1f) value / 255f
        else max(min(value, 1.0f), 0.0f)
    }
}
