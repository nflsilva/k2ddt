package examples.shooter.domain

import examples.shooter.domain.level.Level
import k2ddt.core.dto.UpdateContext

data class GameUpdateContext(
    val baseContext: UpdateContext,
    val level: Level
)