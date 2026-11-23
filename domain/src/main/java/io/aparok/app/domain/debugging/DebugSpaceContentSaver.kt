package io.aparok.app.domain.debugging

import java.io.File

interface DebugSpaceContentSaver {
    fun save(content: String) : File
}