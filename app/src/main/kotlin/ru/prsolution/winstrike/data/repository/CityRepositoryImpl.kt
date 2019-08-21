package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.CityCacheDataSource
import ru.prsolution.winstrike.data.datasource.CityRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.domain.repository.CityRepository

class CityRepositoryImpl constructor(
        private val cacheDataSource: CityCacheDataSource,
        private val remoteDataSource: CityRemoteDataSource
) : CityRepository {


    override suspend fun get(refresh: Boolean): Resource<List<City>>? =
            remoteDataSource.get()
}

