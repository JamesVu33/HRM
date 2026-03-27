package com.example.ihrm.core.errorHandler

interface IErrorType

sealed class CommonErrorException(open val errorKey: String, open var errorMsg: String? = null) :
    Exception(errorKey), IErrorType {
    /**
     * response code: 400
     * bad request, one or some input field was invalid
     *
     * currently, API responses error each field as one
     */
    data class InvalidInputException(
        val errorField: String?, // base on request data class to detect which field is invalid
        override var errorMsg: String?,
        override val errorKey: String,
        val errorParams: Map<String, String?>?
    ) : CommonErrorException(errorKey, errorMsg)


    /**
     * Code logic exception, to display UI to notify User
     */
    data class InvalidLogicException(
        override val errorKey: String,
        override var errorMsg: String?,
    ) : CommonErrorException(errorKey, errorMsg)

    /**
     * response code: 401
     * this means current token is expired or has not access token
     * if the refresh_token exist, should auto invoke refresh-token API endpoint to get new access_token
     */
    data class UnauthorizedException(
        override val errorKey: String,
        override var errorMsg: String?,
    ) : CommonErrorException(errorKey, errorMsg)


    /**
     * response code: 403
     * this means, this account is not allow to enter this feature
     */
    data class NotPermissionException(
        override val errorKey: String,
        override var errorMsg: String?,
    ) :
        CommonErrorException(errorKey, errorMsg)

    /**
     * Network exception
     */
    data class NetworkException(override var errorMsg: String?) :
        CommonErrorException("network", errorMsg) {
    }

    /**
     * Server interrupt
     */
    data class ServerException(override var errorMsg: String?) :
        CommonErrorException("Server", errorMsg)

    /**
     * Unknown error
     */
    data class UnknownException(override var errorMsg: String?) :
        CommonErrorException("Unknown", errorMsg)
}
