package com.lucasxvirtual.financasmercado.di

import com.lucasxvirtual.financasmercado.data.remote.FinancasMercadoApi
import com.lucasxvirtual.financasmercado.data.remote.FinancasMercadoApiMock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideService(
        financasMercadoApiMock: FinancasMercadoApiMock
    ): FinancasMercadoApi {
        return financasMercadoApiMock
//        return Retrofit.Builder()
//            .baseUrl("http://142.93.49.158")
//            .client(
//                OkHttpClient()
//                    .newBuilder()
//                    .build()
//            )
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(FinancasMercadoApi::class.java)
    }
}
