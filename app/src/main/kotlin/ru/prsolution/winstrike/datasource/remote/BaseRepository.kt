package ru.prsolution.winstrike.datasource.remote

import android.util.Log
import retrofit2.Response
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import timber.log.Timber


/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): Resource<T>? {

        val result: Resource<T> = safeApiResource(call, errorMessage)
//        var data : Resource<T>? = null

        when (result.state) {
            is ResourceState.SUCCESS ->
                Timber.tag("$$$").d("${result.state}  - ${result.data.toString()}")
            is ResourceState.ERROR -> {
                Timber.tag("$$$").d("$errorMessage & Exception - ${result.message}")
            }
        }
        return result

    }

    private suspend fun <T : Any> safeApiResource(call: suspend () -> Response<T>, errorMessage: String): Resource<T> {
        var message = ""
        var code = ""
        try {

            val response = call.invoke()

            if (response.isSuccessful) return Resource(ResourceState.SUCCESS, response.body())
            Log.e("ERROR_CODE",response.code().toString() )

            try {
                val errorJsonString = response.errorBody()?.string()
                code = response.code().toString()
                message = errorJsonString.toString()
/*            message = JsonParser().parse(errorJsonString)
                .asJsonObject["message"]
                .asString*/
            } catch (e: Exception) {
                Timber.tag("$$$").d(e.message)
            }

        } catch (e: Exception){
            code = "0"
            message = "Network error"
            return Resource(ResourceState.ERROR, null, "$code - $message")
        }



        return Resource(ResourceState.ERROR, null, "$code - $message")
    }
}

