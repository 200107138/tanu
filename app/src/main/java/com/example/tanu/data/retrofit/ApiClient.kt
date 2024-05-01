package com.example.tanu.data.retrofit

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import com.example.tanu.SessionManager
import com.example.tanu.data.models.RefreshTokenRequest
import com.example.tanu.ui.auth.AuthActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    private lateinit var apiService: ApiService
    private lateinit var noneAuthApiService: NoneAuthApiService
    private val BASE_URL = "https://api-tanu.onrender.com"

    fun getApiService(context: Context): ApiService {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context)) // Pass AuthInterceptor
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }
    fun getNoneAuthApiService(context: Context): NoneAuthApiService {
        // Initialize NoneAuthApiService if not initialized yet
        if (!::noneAuthApiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            noneAuthApiService = retrofit.create(NoneAuthApiService::class.java)
        }

        return noneAuthApiService
    }

    /**
     * Initialize OkHttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(context, getNoneAuthApiService(context)))
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}
class TokenAuthenticator(private val context: Context, private val noneAuthApiService: NoneAuthApiService) : Authenticator {
    private val sessionManager = SessionManager(context)

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sessionManager.fetchRefreshToken()

        // Refresh the access token using refreshToken
        val newAccessTokenResponse = runBlocking {
            noneAuthApiService.refreshAccessToken(RefreshTokenRequest(refreshToken))
        }


        val newAccessToken = newAccessTokenResponse.accessToken
        return if (newAccessToken != null) {
            sessionManager.updateAccessToken(newAccessToken)
            // Retry the request with the new access token
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } else {
            // If refresh token failed, clear session and navigate to login screen
            navigateToLoginScreen()
            null
        }
    }


    private fun navigateToLoginScreen() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}

class AuthInterceptor(private val context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val accessToken = sessionManager.fetchAccessToken()

        return if (accessToken != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(newRequest)
        } else{
            val newRequest = originalRequest.newBuilder()
                .build()
            chain.proceed(newRequest)
        }

    }
}
