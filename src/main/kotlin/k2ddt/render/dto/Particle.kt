package k2ddt.render.dto

/**
 * Particle data class
 *
 * Holds data to represent a particle to be drawn.
 *
 * @param type particle type.
 * @param size particle size.
 * @param color shape draw color.
 */
data class Particle(
    val type: Type,
    val size: Float,
    val color: Color
) {
    /**
     * Particle Types data class
     *
     * Represents the different types of supported particles.
     *
     */
    enum class Type(val value: Int) {
        CIRCLE(0),
        OTHER(1),
    }
}