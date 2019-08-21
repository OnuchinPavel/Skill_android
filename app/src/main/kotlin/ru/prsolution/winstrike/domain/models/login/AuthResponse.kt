package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.UserEntity

class AuthResponse(
    val message: String?,
    val token: String?,
    val user: UserEntity?
)
