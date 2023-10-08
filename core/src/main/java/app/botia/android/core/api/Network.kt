package app.botia.android.core.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

private const val MESSAGE_NULL_DATA = "Null data"
private const val MESSAGE_NO_EXCEPTION_MESSAGE = "Exception with no message"
const val MESSAGE_UNKNOWN_HOST_EXCEPTION = "No internet connection"

interface Network {
    fun <T> makeNetworkCall(api: suspend () -> T): Flow<ApiResponseStatus<T>>
}

class NetworkImpl @Inject constructor(private val dispatcherIO: CoroutineDispatcher) : Network {
    override fun <T> makeNetworkCall(api: suspend () -> T): Flow<ApiResponseStatus<T>> = flow {
        emit(ApiResponseStatus.Loading())
        emit(apiCall(api))
    }

    private suspend fun <T> apiCall(
        api: suspend () -> T,
    ): ApiResponseStatus<T> = withContext(context = dispatcherIO) {
        val result = runCatching {
            api.invoke()
        }

        if (result.isSuccess) {
            val data = result.getOrNull() ?: return@withContext ApiResponseStatus.Error(
                MESSAGE_NULL_DATA
            )

            return@withContext ApiResponseStatus.Success(data = data)
        }

        val message: String = when (val exceptionOrNull = result.exceptionOrNull()) {
            is UnknownHostException -> MESSAGE_UNKNOWN_HOST_EXCEPTION
            is HttpException -> exceptionOrNull.message ?: MESSAGE_NO_EXCEPTION_MESSAGE
            else -> exceptionOrNull?.message ?: MESSAGE_NO_EXCEPTION_MESSAGE
        }

        return@withContext ApiResponseStatus.Error(message)
    }
}