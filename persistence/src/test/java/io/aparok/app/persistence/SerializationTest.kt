package io.aparok.app.persistence

import io.aparok.app.core_models.Id
import io.aparok.app.persistence.common.serializeWallpaperSettings
import io.aparok.app.persistence.model.WallpaperSetting
import io.aparok.app.test_utils.MockDataFactory
import kotlin.test.assertEquals
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test

class SerializationTest {

    @Test
    fun `should serialize-deserialize map with wallpaper settings`() {

        val id = MockDataFactory.randomUuid()
        val type = MockDataFactory.randomInt()
        val value = MockDataFactory.randomString()

        val givenMap = mapOf(
            id to WallpaperSetting(
                type = type,
                value = value
            )
        )

        val encoded = givenMap.serializeWallpaperSettings()

        val decoded = Json.decodeFromString<Map<Id, WallpaperSetting>>(encoded)

        assertEquals(
            expected = givenMap,
            actual = decoded
        )
    }

}