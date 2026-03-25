package com.example.ihrm.core.errorHandler

interface IErrorType

sealed class CommonErrorException(open val errorKey: String?) : Exception(errorKey), IErrorType {
    /**
     * response code: 400
     * bad request, one or some input field was invalid
     *
     * currently, API responses error each field as one
     */
    data class InvalidInputException(
        val errorField: String?, // base on request data class to detect which field is invalid
        val errorMsg: String?,
        override val errorKey: String?,
//        val errorParams: Map<String, Any>?
    ) : CommonErrorException(errorKey)


    /**
     * Code logic exception, to display UI to notify User
     */
    data class InvalidLogicException(
        override val errorKey: String?,
        val errorMsg: String?,
    ) : CommonErrorException(errorKey)

    /**
     * response code: 401
     * this means current token is expired or has not access token
     * if the refresh_token exist, should auto invoke refresh-token API endpoint to get new access_token
     */
    data class UnauthorizedException(override val errorKey: String?) : CommonErrorException(errorKey)


    /**
     * response code: 403
     * this means, this account is not allow to enter this feature
     */
    data class NotPermissionException(override val errorKey: String?) : CommonErrorException(errorKey)

    /**
     * Network exception
     */
    data class NetworkException(val msg: String?) : CommonErrorException(msg ?: "network")

    /**
     * Server interrupt
     */
    data class ServerException(val msg: String?) : CommonErrorException(msg ?: "Server")

    /**
     * Unknown error
     */
    data class UnknownException(val msg: String?) : CommonErrorException(msg ?: "Unknown")
}
