package org.d3if1062.mobpro2.tryactionmodeanddialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.MahasiswaDao

class MainViewModelFactory(private val dataSource: MahasiswaDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unable to construc ViewModel")
    }

}