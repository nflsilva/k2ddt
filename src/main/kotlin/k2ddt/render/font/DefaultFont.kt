package k2ddt.render.font

import k2ddt.render.model.BitmapFont
import org.joml.Vector2f

class DefaultFont(): BitmapFont(
    "/font/default.png",
    10,
    10
) {

    init {
        setRow(0, " !\"#\$%&\'()")
        setRow(1, "*+,-./0123")
        setRow(2, "456789:;<=")
        setRow(3, ">?@ABCDEFG")
        setRow(4, "HIJKLMNOPQ")
        setRow(5, "RSTUVWXYZ[")
        setRow(6, "\\]^_`abcde")
        setRow(7, "fghijklmno")
        setRow( 8, "pqrstuvwxy")
        setRow( 9, "z{|}~")
    }

}