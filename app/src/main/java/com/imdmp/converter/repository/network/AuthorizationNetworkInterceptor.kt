package com.imdmp.converter.repository.network

import com.imdmp.converter.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationNetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url: HttpUrl =
            request.url.newBuilder().addQueryParameter("apikey", BuildConfig.API_KEY).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }


}