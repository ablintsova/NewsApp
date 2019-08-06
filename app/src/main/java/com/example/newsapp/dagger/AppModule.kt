package com.example.newsapp.dagger

import android.app.Application
import android.content.Context
import com.example.newsapp.model.interactor.IMainInteractor
import com.example.newsapp.model.interactor.MainInteractor
import com.example.newsapp.presenter.IMainPresenter
import com.example.newsapp.presenter.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun providePresenter(interactor: IMainInteractor): IMainPresenter = MainPresenter(interactor)

    @Provides
    @Singleton
    fun provideInteractor(): IMainInteractor =
        MainInteractor()
}
