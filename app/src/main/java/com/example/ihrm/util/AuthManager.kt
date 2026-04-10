package com.example.ihrm.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.AppFeature
import com.example.ihrm.domain.session.LoginSessionResolver
import com.example.ihrm.util.AuthManager.init
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Holds auth state and tokens. Call [init] from [android.app.Application.onCreate].
 *
 * #coreFeature
 * migrate to version 2: migrate to DataStore
 */
object AuthManager {

    @Volatile
    private var prefs: SharedPreferences? = null

    private val _authEvents = MutableSharedFlow<AuthEvent>()
    val authEvents = _authEvents.asSharedFlow()

    /**
     * Must be called once (e.g. from [android.app.Application.onCreate]) before using login state.
     */
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(
                Constants.AUTH_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        }
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs?.edit()?.putBoolean(Constants.PREF_IS_LOGGED_IN, loggedIn)?.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return prefs?.getBoolean(Constants.PREF_IS_LOGGED_IN, false) == true
    }

    /**
     * Saves tokens and user session (including fullName, email, account tier, modifiable features)
     * after successful login. [LoginSessionResolver] merges API fields with mock fallbacks.
     */
    fun saveTokens(response: LoginResponse) {
        val session = LoginSessionResolver.resolve(response)
        val featuresCsv = session.modifiableFeatures.joinToString(",") { it.apiCode }
        prefs?.edit()
            ?.putString(Constants.PREF_ACCESS_TOKEN, response.accessToken)
            ?.putString(Constants.PREF_REFRESH_TOKEN, response.refreshToken)
            ?.putBoolean(Constants.PREF_IS_LOGGED_IN, true)
            ?.putString(Constants.PREF_USER_FULL_NAME, response.user.fullName)
            ?.putString(Constants.PREF_USER_EMAIL, response.user.email)
            ?.putString(Constants.PREF_USER_EMPLOYEE_ID, response.user.employeeId)
            ?.putString(Constants.PREF_USER_PHONE, response.user.phoneNumber.orEmpty())
            ?.putString(Constants.PREF_ACCOUNT_TYPE, session.accountType.toApiValue())
            ?.putString(Constants.PREF_MODIFIABLE_FEATURES, featuresCsv)
            ?.apply()
    }

    fun getAccessToken(): String? = prefs?.getString(Constants.PREF_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs?.getString(Constants.PREF_REFRESH_TOKEN, null)

    /** Logged-in user's full name, or null if not saved. */
    fun getUserFullName(): String? = prefs?.getString(Constants.PREF_USER_FULL_NAME, null)

    /** Logged-in user's email, or null if not saved. */
    fun getUserEmail(): String? = prefs?.getString(Constants.PREF_USER_EMAIL, null)

    /** Employee id from last login, or null if not saved. */
    fun getUserEmployeeId(): String? = prefs?.getString(Constants.PREF_USER_EMPLOYEE_ID, null)

    /** Phone from last login; may be blank. */
    fun getUserPhone(): String? = prefs?.getString(Constants.PREF_USER_PHONE, null)

    /**
     * Account tier from last login ([LoginSessionResolver]); defaults to [AccountType.Basic] if missing.
     */
    fun getAccountType(): AccountType {
        val raw = prefs?.getString(Constants.PREF_ACCOUNT_TYPE, null)
        return AccountType.fromApiValue(raw) ?: AccountType.Basic
    }

    /**
     * Features the current user may **modify** (from login response + mock); empty if none.
     */
    fun getModifiableFeatures(): Set<AppFeature> {
        val raw = prefs?.getString(Constants.PREF_MODIFIABLE_FEATURES, null) ?: return emptySet()
        if (raw.isBlank()) return emptySet()
        return raw.split(',').mapNotNull { AppFeature.fromApiCode(it.trim()) }.toSet()
    }

    fun canModify(feature: AppFeature): Boolean = feature in getModifiableFeatures()

    /**
     * Clears tokens and logged-in state (e.g. on logout).
     */
    fun clearTokens() {
        prefs?.edit()
            ?.remove(Constants.PREF_ACCESS_TOKEN)
            ?.remove(Constants.PREF_REFRESH_TOKEN)
            ?.remove(Constants.PREF_USER_FULL_NAME)
            ?.remove(Constants.PREF_USER_EMAIL)
            ?.remove(Constants.PREF_USER_EMPLOYEE_ID)
            ?.remove(Constants.PREF_USER_PHONE)
            ?.remove(Constants.PREF_ACCOUNT_TYPE)
            ?.remove(Constants.PREF_MODIFIABLE_FEATURES)
            ?.putBoolean(Constants.PREF_IS_LOGGED_IN, false)
            ?.apply()
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        prefs?.edit {
            putString(Constants.PREF_ACCESS_TOKEN, accessToken)
            putString(Constants.PREF_REFRESH_TOKEN, refreshToken)
            putBoolean(Constants.PREF_IS_LOGGED_IN, true)
        }
    }

    suspend fun emitLogoutEvent() {
        _authEvents.emit(AuthEvent.Logout)
    }
}

sealed class AuthEvent {
    object Logout : AuthEvent()
}
