package br.com.fbsantos.baseapp.di

import android.content.Context
import androidx.room.Room
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            AppDatabase::class.java,
            AppConfig.APP_DB
        ).build()
        //para resetar o banco, adicionar antes do build .fallbackToDestructiveMigration(true)
    }
}