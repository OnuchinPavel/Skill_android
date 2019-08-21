package ru.prsolution.winstrike.datasource.model.payment

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.arena.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.arena.mapToDomain
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

class PaymentResponseEntity(

    @field:Json(name = "message")
    val message: String? = null,

    @field:Json(name = "redirect_url")
    val redirectUrl: String? = null
)

fun PaymentResponseEntity.mapToDomain(): PaymentResponse = PaymentResponse(
    message = this.message,
    redirectUrl = this.redirectUrl
)


fun Resource<PaymentResponseEntity>.mapToDomain(): Resource<PaymentResponse> = Resource<PaymentResponse>(
    state = state,
    data = data?.mapToDomain(),
    message = message
)
