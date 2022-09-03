package k2ddt.core

class EngineConfiguration(var windowTitle: String,
                          var resolutionWidth: Int,
                          var resolutionHeight: Int,
                          var enableVsync: Boolean) {

    companion object {
        fun default(): EngineConfiguration {
            return EngineConfiguration(
                "k2ddt",
                1280,
                720,
                true)
        }
    }
}