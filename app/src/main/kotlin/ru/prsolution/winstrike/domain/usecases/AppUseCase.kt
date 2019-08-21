package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.repository.AppRepository

class AppUseCase constructor(private val appRepository: AppRepository) {

    suspend fun get(versionName: String): Resource<MessageResponse>? =
        appRepository.get(versionName)
}
