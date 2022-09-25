package k2ddt.core.dto

import k2ddt.core.ExecutionContext
import k2ddt.ui.dto.InputStateData

data class UpdateContext(
    val elapsedTime: Float,
    val input: InputStateData
)