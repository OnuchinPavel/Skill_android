package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse
import ru.prsolution.winstrike.domain.repository.ArenaRepository
import ru.prsolution.winstrike.presentation.model.payment.PaymentInfo
import ru.prsolution.winstrike.presentation.model.payment.mapToDomain

class PaymentUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun pay(paymentInfo: PaymentInfo): Resource<PaymentResponse>? =
        arenaRepository.pay(paymentInfo.mapToDomain())
}
