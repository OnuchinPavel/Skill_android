package ru.prsolution.winstrike.datasource.cache

import io.reactivex.Single
import ru.prsolution.winstrike.data.datasource.CityCacheDataSource
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.presentation.utils.cache.Cache

class CityCacheDataSourceImpl constructor(
    private val cache: Cache<List<City>>
) : CityCacheDataSource {
    val key = "city_list"

    override fun get(): Single<List<City>> =
        cache.load(key)

    override fun set(list: List<City>?): Single<List<City>> =
        cache.save(key, list)

}
