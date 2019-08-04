package com.example.newsapp.dagger

import com.example.newsapp.model.IMainInteractor
import com.example.newsapp.model.MainInteractor
import com.example.newsapp.presenter.IMainPresenter
import com.example.newsapp.presenter.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun providePresenter(interactor: IMainInteractor): IMainPresenter = MainPresenter(interactor)

    @Provides
    fun provideInteractor(): IMainInteractor = MainInteractor()
}
