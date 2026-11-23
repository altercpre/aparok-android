package io.aparok.app.data.auth.repo

class AuthDataStoreFactory(
    val cache: AuthCacheDataStore,
    val remote: AuthRemoteDataStore
)