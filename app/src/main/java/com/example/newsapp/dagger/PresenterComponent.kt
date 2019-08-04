package com.example.newsapp.dagger

import com.example.newsapp.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PresenterModule::class])
interface PresenterComponent {
    fun inject(target: MainActivity)
}