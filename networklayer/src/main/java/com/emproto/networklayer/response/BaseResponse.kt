package com.emproto.networklayer.response

import androidx.annotation.Keep
import com.emproto.networklayer.response.enums.Status


@Keep
class BaseResponse<T> private constructor(val status: Status, val data: T?, var message: String?) {

    companion object {

        fun <T> success(data: T): BaseResponse<T> {
            return BaseResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): BaseResponse<T> {
            return BaseResponse(Status.ERROR, null, message)
        }

        fun <T> loading(): BaseResponse<T> {
            return BaseResponse(Status.LOADING, null, null)
        }

        fun <T> loading(data: T?): BaseResponse<T> {
            return BaseResponse(Status.LOADING, data, null)
        }

        fun <T> error(message: String, data: T?): BaseResponse<T> {
            return BaseResponse(Status.ERROR, data, message)
        }
    }
}
