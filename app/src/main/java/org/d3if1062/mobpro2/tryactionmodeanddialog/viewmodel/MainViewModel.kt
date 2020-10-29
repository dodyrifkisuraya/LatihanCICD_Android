package org.d3if1062.mobpro2.tryactionmodeanddialog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.Mahasiswa
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.MahasiswaDao

class MainViewModel(private val db: MahasiswaDao) : ViewModel() {

    private var _dataMahasiswa = MutableLiveData<Mahasiswa>()
    val data = db.getData()

    fun insertData(mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(mahasiswa)
            }
        }
    }

    fun deleteData(ids: List<Int>) {
        val newIds = ids.toList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.deleteData(newIds)
            }
        }
    }

    fun updateData(mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.update(mahasiswa)
            }
        }
    }

    fun getData(key: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _dataMahasiswa.postValue(db.getMahasiswa(key))
            }
        }
    }

    val dataMahasiswa: LiveData<Mahasiswa> get() = _dataMahasiswa


}