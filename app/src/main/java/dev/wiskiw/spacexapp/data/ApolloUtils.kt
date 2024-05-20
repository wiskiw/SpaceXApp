package dev.wiskiw.spacexapp.data

import com.apollographql.apollo3.exception.ApolloException
import dev.wiskiw.spacexapp.repository.model.DataSourceException


@Throws(DataSourceException::class)
suspend fun <ResponseData> safeApolloRequest(
    requestBlock: suspend () -> ResponseData,
): ResponseData {
    try {
        return requestBlock.invoke()
    } catch (apolloException: ApolloException) {
        throw DataSourceException(apolloException)
    }
}
