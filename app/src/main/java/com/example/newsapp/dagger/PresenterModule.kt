package com.example.newsapp.dagger

import com.example.newsapp.model.IMainInteractor
import com.example.newsapp.model.MainInteractor
import com.example.newsapp.presenter.IMainPresenter
import com.example.newsapp.presenter.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun providePresenter(interactor: IMainInteractor): IMainPresenter = MainPresenter(interactor)

    @Provides
    @Singleton
    fun provideInteractor(): IMainInteractor = MainInteractor()
}
