package examples.game.domain

import examples.game.domain.level.Level
import k2ddt.core.dto.UpdateContext

data class GameUpdateContext(
    val baseContext: UpdateContext,
    val level: Level
)