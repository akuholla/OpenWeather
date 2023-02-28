package com.akshayholla.openweather.common

data class Response<out T>(val status: Status, val data: T?, val message: String?, val errorType: ErrorType?) {
    companion object {
        fun <T> success(data: T): Response<T> {
            return Response(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String, errorType: ErrorType): Response<T> {
            return Response(Status.ERROR, null, msg, errorType)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
}

enum class ErrorType {
    NETWORK,
    LOCATION_ACCESS,
    PARSE_ERROR
}