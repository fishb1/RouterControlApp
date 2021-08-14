package me.fb.ng.ctrl.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.fb.ng.ctrl.BuildConfig
import me.fb.ng.ctrl.data.RouterApi
import me.fb.ng.ctrl.model.settings.SettingsStorage
import me.fb.ng.ctrl.model.settings.ShredPrefSettingsStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun providesSettingsStorage(storage: ShredPrefSettingsStorage): SettingsStorage = storage

    @Provides
    fun providesRouterApiRetrofit(settingsStorage: SettingsStorage): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        // todo: figure out how to update retrofit instances when ip is changed
        val ip = settingsStorage.getSettings().routerIp
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .baseUrl("http://$ip")
            .build()
    }

    @Provides
    fun providesRouterApi(retrofit: Retrofit): RouterApi {
        return retrofit.create(RouterApi::class.java)
    }
}
