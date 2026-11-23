package com.anytypeio.anytype.core_utils.tools

import javax.inject.Inject

interface AppInfo {
    val versionName: String
}

class DefaultAppInfo @Inject constructor(
    versionNameValue: String
): AppInfo {

    override val versionName: String = versionNameValue

}
