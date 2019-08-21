package ru.prsolution.winstrike.presentation.model.login

import ru.prsolution.winstrike.domain.models.login.PasswordModel


class Password(
    val username: String?,
    val password: String?
)


fun Password.mapToDomain(): PasswordModel = PasswordModel(
    username = this.username,
    password = this.password
)
