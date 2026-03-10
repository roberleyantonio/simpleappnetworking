package br.com.dev360.simpleapplicationnetworking.core.wrapper

import br.com.dev360.simpleapplicationnetworking.core.shared.exception.Failure

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val failure: Failure) : ResultWrapper<Nothing>()

    fun <M> map(mapper: (originalData: T) -> M): ResultWrapper<M> = when (this) {
        is ResultWrapper.Success -> Success(mapper(value))
        is ResultWrapper.Error -> Error(failure = failure)
    }

    inline fun <X> withSuccessAndError(success: (T) -> X, error: (Failure) -> X): X = when (this) {
        is ResultWrapper.Success -> success(this.value)
        is ResultWrapper.Error -> error(this.failure)
    }
}