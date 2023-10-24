package com.lucasxvirtual.financasmercado.di

import com.lucasxvirtual.financasmercado.Constants.DEFAULT
import com.lucasxvirtual.financasmercado.Constants.IO
import com.lucasxvirtual.financasmercado.Constants.MAIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {
    @Provides
    @Named(IO)
    fun provideCoroutineDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(DEFAULT)
    fun provideCoroutineDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Named(MAIN)
    fun provideCoroutineDispatcherMain(): CoroutineDispatcher = Dispatchers.Main
}
