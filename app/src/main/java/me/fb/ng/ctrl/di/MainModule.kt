package me.fb.ng.ctrl.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.fb.ng.ctrl.BuildConfig
import me.fb.ng.ctrl.data.RouterApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
class MainModule {

    @Provides
    fun providesRouterApiRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .baseUrl("http://192.168.1.1")
            .build()
    }

    @Provides
    fun providesRouterApi(retrofit: Retrofit): RouterApi {
        return retrofit.create(RouterApi::class.java)
    }
}
