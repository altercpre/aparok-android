package io.aparok.app

interface CrashReporter {
    fun init()
    fun setUser(userId: String)
}

class NoOpCrashReporter : CrashReporter {
    override fun init() {}
    override fun setUser(userId: String) {}
}
