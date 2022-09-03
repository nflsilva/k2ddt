package k2ddt.core.dto

import ui.dto.InputStateData

data class UpdateContext(
    val elapsedTime: Float,
    val input: InputStateData
)