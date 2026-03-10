package br.com.dev360.simpleapplicationnetworking.core.shared.exception

import retrofit2.HttpException
import java.io.IOException

sealed class Failure {
    data object NetworkConnection : Failure()
    data object NotFound: Failure()
    data object Generic: Failure()
    data class Unknown(val throwable: Throwable?) : Failure()

    companion object {
        private const val NOT_FOUND = 404

        fun fromException(e: Throwable): Failure = when (e) {
            is IOException -> NetworkConnection
            is HttpException -> when(e.code()) {
                NOT_FOUND -> NotFound
                else -> Generic
            }
            else -> Unknown(null)
        }
    }
}