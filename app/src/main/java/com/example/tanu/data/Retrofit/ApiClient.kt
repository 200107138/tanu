package com.example.tanu.data.Retrofit

import android.content.Context
import com.example.tanu.SessionManager
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    private lateinit var apiService: ApiService

    fun getApiService(context: Context): ApiService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://us-central1-tanu-f4bc0.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context)) // Add our Okhttp client
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()

        // If login and password have been saved, add them to the request
        sessionManager.fetchLogin()?.let { login ->
            sessionManager.fetchPassword()?.let { password ->
                // You might need to adjust this part depending on your API's authentication method
                val credentials = Credentials.basic(login, password)
                requestBuilder.addHeader("Authorization", credentials)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}