package k2ddt.render.dto

import k2ddt.render.model.BitmapFont

/**
 * Text data class
 *
 * Holds data to represent a string to be rendered.
 *
 * @param data the text to be rendered.
 * @param font the font to render the text.
 */
data class Text(val data: String,
                val font: BitmapFont
)