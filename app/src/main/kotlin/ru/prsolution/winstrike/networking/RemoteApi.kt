package ru.prsolution.winstrike.networking

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import ru.prsolution.winstrike.datasource.model.arena.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.arena.ScheduleEntity
import ru.prsolution.winstrike.datasource.model.arena.SchedulersEntity
import ru.prsolution.winstrike.datasource.model.arena.SchemaEntity
import ru.prsolution.winstrike.datasource.model.city.CityListEntity
import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity
import ru.prsolution.winstrike.datasource.model.login.*
import ru.prsolution.winstrike.datasource.model.orders.OrdersListEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentResponseEntity
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse

interface AppApi {
    @GET("rooms")
    fun getVersion(
        @Query("v") appVersion: String,
        @Query("device") device: String = "android"
    ): Deferred<Response<MessageResponse>>
}


interface CityApi {
    // Получение  списка городов
    @GET("cities")
    fun getCityAsync(): Deferred<Response<CityListEntity>>
}

interface ArenaApi {
    // Получение  списка имеющихся арен
    @GET("rooms")
    fun getArenaAsync(): Deferred<Response<ArenaListEntity>>


    // Получение арены по  id  и диапазону времени
    @GET("room_layouts/{active_layout_pid}")
    fun getSchemaAsync(
        @Path(
            "active_layout_pid"
        ) active_layout_pid: String?,
        @QueryMap time: Map<String, String>
    ): Deferred<Response<SchemaEntity>>

    @GET("halls/{active_layout_pid}/layout")
    fun getSchemaHallAsync(
        @Path(
            "active_layout_pid"
        ) active_layout_pid: String?,
        @QueryMap time: Map<String, String>
    ): Deferred<Response<SchemaEntity>>

    //  Создание платежа для указанных мест и диапазона времени.
    //  В случае успеха в ответ приходит ссылка на Яндекс кассу для оплаты.
    @POST("payments")
    fun getPaymentAsync(
        @Body payment: PaymentEntity
    ): Deferred<Response<PaymentResponseEntity>>

    // Расписание работы клубов
    @GET("schedules")
    fun getSchedulesAsync(): Deferred<Response<SchedulersEntity>>


    // Получение списка оплаченных мест пользователя
    @GET("orders")
    fun getOrdersAsync(): Deferred<Response<OrdersListEntity>>

    // Отправка пуш уведомления пользователю перед концом сеанса
    @POST("fcm_codes")
    fun sendTockenAsync(
        @Body fcmEntity: FCMEntity
    ): Deferred<Response<MessageResponse>>



}

interface UserApi {
    // Создание пользователя
    @POST("users")
    fun getUserAsync(@Body newUserEntity: NewUserEntity): Deferred<Response<AuthResponseEntity>>

    // Подтверждение пользоватея по sms коду
    @POST("confirm_user/{sms_code}")
    fun confirmUserAsync(
        @Path(
            "sms_code") sms_code: String,
        @Body confirmModel:SmsEntity
    ): Deferred<Response<MessageResponse>>


    // Авторизация пользователя
    @POST("login")
    fun getLoginAsync(@Body loginModel: LoginEntity): Deferred<Response<AuthResponseEntity>>

    // Отправка смс c кодом подтверждения (повторно)
    @POST("confirm_codes")
    fun sendSmsAsync(@Body confirmModel: SmsEntity): Deferred<Response<MessageResponse>>


    // Обновление профиля (имя пользователя)
    @PUT("users/{public_id}")
    fun updateUserAsync(
        @Path(
            "public_id") public_id: String,
        @Body profileEntity: ProfileEntity
    ): Deferred<Response<MessageResponse>>


    // Смена пароля (в параметрах передается код подтверждения из СМС)
    @POST("refresh_password/{confirm_code}")
    fun changePasswordAsync(
        @Path(
            "confirm_code") confirmCode: String,
        @Body passwordEntity: PasswordEntity
    ): Deferred<Response<MessageResponse>>


}

