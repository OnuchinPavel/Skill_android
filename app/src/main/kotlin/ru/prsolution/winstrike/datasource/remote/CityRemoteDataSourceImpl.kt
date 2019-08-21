package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.CityRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.city.mapToDomain
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.networking.CityApi

class CityRemoteDataSourceImpl constructor(
    private val api: CityApi
) : CityRemoteDataSource, BaseRepository() {

    override suspend fun get(): Resource<List<City>>? {
        val cityResponse = safeApiCall(
                call = { api.getCityAsync().await() },
                errorMessage = "Error Fetching Cities List"
        )

        return cityResponse?.mapToDomain()

    }

}
