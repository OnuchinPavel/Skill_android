package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.payment.PaymentResponseEntity
import ru.prsolution.winstrike.datasource.model.payment.mapToDomain
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

class AuthResponseEntity(
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "token")
    val token: String?,
    @field:Json(name = "user")
    val user: UserEntity?
)

fun AuthResponseEntity.maptoDomain(): AuthResponse = AuthResponse(
    message = this.message,
    token = this.token,
    user = this.user
)


fun Resource<AuthResponseEntity>.mapToDomain(): Resource<AuthResponse> = Resource<AuthResponse>(
    state = state,
    data = data?.maptoDomain(),
    message = message
)
