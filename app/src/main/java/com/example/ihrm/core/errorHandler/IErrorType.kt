package com.example.ihrm.core.errorHandler

interface IErrorType

sealed class GlobalErrorException(open val errorKey: String?) : IErrorType {

    /**
     * response code: 401
     * this means current token is expired or has not access token
     * if the refresh_token exist, should auto invoke refresh-token API endpoint to get new access_token
     */
    class UnauthorizedException(override val errorKey: String?) : GlobalErrorException(errorKey)


    /**
     * response code: 403
     * this means, this account is not allow to enter this feature
     */
    class NotPermissionException(override val errorKey: String?) : GlobalErrorException(errorKey)
}

sealed class CommonErrorException(open val errorKey: String?) : Exception(errorKey), IErrorType {
    /**
     * response code: 400
     * bad request, one or some input field was invalid
     *
     * currently, API responses error each field as one
     */
    class InvalidInputException(
        val errorField: String?,
        override val errorKey: String?,
        val errorParams: Map<String, Any>?
    ) : CommonErrorException(errorKey)


    /**
     * Code logic exception, to display UI to notify User
     */
    class InvalidLogicException(
        val errorField: String?,
        override val errorKey: String?,
        val errorParams: Map<String, Any>?
    ) : CommonErrorException(errorKey)


    /**
     * Network exception
     */
    class NetworkException(msg: String?) : CommonErrorException(msg ?: "network")

    /**
     * Server interrupt
     */
    class ServerException(msg: String?) : CommonErrorException(msg ?: "Server")

    /**
     * Unknown error
     */
    class UnknownException(msg: String?) : CommonErrorException(msg ?: "Unknown")
}
