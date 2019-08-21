package ru.prsolution.winstrike.domain.models.payment

import ru.prsolution.winstrike.presentation.model.payment.PaymentResponseItem


class PaymentResponse(
    val message: String?,
    val redirectUrl: String?
)


fun PaymentResponse.mapToPresentation(): PaymentResponseItem = PaymentResponseItem(
    message = message,
    redirectUrl = redirectUrl
)
