package ru.prsolution.winstrike.domain.models.login

import ru.prsolution.winstrike.datasource.model.login.PasswordEntity
import ru.prsolution.winstrike.presentation.model.login.Password


class PasswordModel(
    val username: String?,
    val password: String?
)


fun PasswordModel.mapToDataSource(): PasswordEntity = PasswordEntity(
    username = this.username,
    new_password = this.password
)

fun PasswordModel.mapToPresentation(): Password = Password(
    username  = this.username,
    password = this.password
)
