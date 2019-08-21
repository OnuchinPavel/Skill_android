package ru.prsolution.winstrike.datasource.cache

import io.reactivex.Single
import ru.prsolution.winstrike.data.datasource.ArenaCacheDataSource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.presentation.utils.cache.Cache

class ArenaCacheDataSourceImpl constructor(
    private val cache: Cache<List<Arena>>
) : ArenaCacheDataSource {
    val key = "arena_list"

    override fun get(): Single<List<Arena>> =
        cache.load(key)

    override fun set(list: List<Arena>?): Single<List<Arena>> =
        cache.save(key, list)

}
