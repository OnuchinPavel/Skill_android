package ru.prsolution.winstrike.presentation.model.payment

import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

class PaymentResponseItem(val message: String?, val redirectUrl: String?)


fun PaymentResponseItem.mapToDomain(): PaymentResponse = PaymentResponse(
    message = this.message,
    redirectUrl = this.redirectUrl
)
