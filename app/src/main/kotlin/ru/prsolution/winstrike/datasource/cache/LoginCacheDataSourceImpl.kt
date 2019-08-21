package ru.prsolution.winstrike.datasource.cache

import io.reactivex.Single
import ru.prsolution.winstrike.data.datasource.LoginCacheDataSource
import ru.prsolution.winstrike.domain.models.login.UserModel
import ru.prsolution.winstrike.presentation.utils.cache.Cache

class LoginCacheDataSourceImpl constructor(
    private val cache: Cache<UserModel>
) : LoginCacheDataSource {
    val key = "user"

    override fun get(): Single<UserModel> =
        cache.load(key)

    override fun set(user: UserModel?): Single<UserModel> =
        cache.save(key, user)

}
