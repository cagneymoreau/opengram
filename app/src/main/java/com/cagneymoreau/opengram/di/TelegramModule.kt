package com.cagneymoreau.opengram.di

import android.content.Context
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.telegramintegration.TelegramRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TelegramModule {

    @Provides
    @Singleton
    fun provideTelegramController(@ApplicationContext context: Context) : RepositoryInterface
    {
        val tele = TelegramRepository()
        tele.configure(context)
        return tele
    }








}