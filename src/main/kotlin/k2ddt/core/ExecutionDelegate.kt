package k2ddt.core

import k2ddt.core.dto.UpdateContext

abstract class ExecutionDelegate {

    lateinit var executionContext: ExecutionContext

    open fun onStart() {}
    abstract fun onUpdate(updateContext: UpdateContext)
    abstract fun onFrame()
    open fun onCleanUp() {}
}