package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.city.City

interface CityCacheDataSource {

    fun get(): Single<List<City>>

    fun set(list: List<City>?): Single<List<City>>

}

interface CityRemoteDataSource {

   suspend fun get(): Resource<List<City>>?

}
