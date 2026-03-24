package com.example.ihrm.util

import android.content.Context
import android.content.SharedPreferences
import com.example.ihrm.data.remote.login.LoginResponse

/**
 * Holds auth state and tokens. Call [init] from [android.app.Application.onCreate].
 *
 * #coreFeature
 * migrate to version 2: migrate to DataStore
 */
object AuthManager {

    @Volatile
    private var prefs: SharedPreferences? = null

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
     * Saves tokens and user session (including fullName, email) after successful login.
     */
    fun saveTokens(response: LoginResponse) {
        prefs?.edit()
            ?.putString(Constants.PREF_ACCESS_TOKEN, response.accessToken)
            ?.putString(Constants.PREF_REFRESH_TOKEN, response.refreshToken)
            ?.putBoolean(Constants.PREF_IS_LOGGED_IN, true)
            ?.putString(Constants.PREF_USER_FULL_NAME, response.user.fullName)
            ?.putString(Constants.PREF_USER_EMAIL, response.user.email)
            ?.apply()
    }

    fun getAccessToken(): String? = prefs?.getString(Constants.PREF_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs?.getString(Constants.PREF_REFRESH_TOKEN, null)

    /** Logged-in user's full name, or null if not saved. */
    fun getUserFullName(): String? = prefs?.getString(Constants.PREF_USER_FULL_NAME, null)

    /** Logged-in user's email, or null if not saved. */
    fun getUserEmail(): String? = prefs?.getString(Constants.PREF_USER_EMAIL, null)

    /**
     * Clears tokens and logged-in state (e.g. on logout).
     */
    fun clearTokens() {
        prefs?.edit()
            ?.remove(Constants.PREF_ACCESS_TOKEN)
            ?.remove(Constants.PREF_REFRESH_TOKEN)
            ?.remove(Constants.PREF_USER_FULL_NAME)
            ?.remove(Constants.PREF_USER_EMAIL)
            ?.putBoolean(Constants.PREF_IS_LOGGED_IN, false)
            ?.apply()
    }
}
