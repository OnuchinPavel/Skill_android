package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.domain.repository.CityRepository

class CityUseCase constructor(private val cityRepository: CityRepository) {

    suspend fun get(refresh: Boolean): Resource<List<City>>? =
        cityRepository.get(refresh)
}
