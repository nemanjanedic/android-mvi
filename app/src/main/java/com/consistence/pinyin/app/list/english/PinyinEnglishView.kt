package com.consistence.pinyin.app.list.english

import android.app.Application
import com.consistence.pinyin.api.DatabaseModule
import com.consistence.pinyin.api.NetworkModule
import com.consistence.pinyin.app.list.PinyinListView
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

interface PinyinEnglishView : PinyinListView

@Singleton
@Component(modules = [ NetworkModule::class, DatabaseModule::class ])
interface PinyinEnglishComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): PinyinEnglishComponent
    }

    fun inject(pinyinCharacterFragment: PinyinEnglishFragment)
}