package br.com.dev360.simpleapplicationnetworking.core.network

import br.com.dev360.simpleapplicationnetworking.core.shared.exception.Failure
import br.com.dev360.simpleapplicationnetworking.core.wrapper.ResultWrapper

sealed class NetworkResponse<out T> {
    data class Success<out T>(val value: T) : NetworkResponse<T>()
    data class Error(val exception: Failure? = null) : NetworkResponse<Nothing>()
}

fun <T> NetworkResponse<T>.toResult(): ResultWrapper<T> =
    when (this) {
        is NetworkResponse.Success -> {
            ResultWrapper.Success(this.value)
        }

        is NetworkResponse.Error -> {
            ResultWrapper.Error(failure = this.exception ?: Failure.Unknown(null))
        }
    }