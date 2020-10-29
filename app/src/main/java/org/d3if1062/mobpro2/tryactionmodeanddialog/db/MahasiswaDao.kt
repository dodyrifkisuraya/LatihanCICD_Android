package org.d3if1062.mobpro2.tryactionmodeanddialog.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MahasiswaDao {
    @Insert
    fun insert(mahasiswa: Mahasiswa)

    @Query("SELECT * FROM mahasiswa ORDER BY nim")
    fun getData(): LiveData<List<Mahasiswa>>

    @Query("DELETE FROM mahasiswa WHERE id IN (:ids)")
    fun deleteData(ids: List<Int>)

    @Update
    fun update(mahasiswa: Mahasiswa)

    @Query("SELECT * FROM mahasiswa WHERE id = :key")
    fun getMahasiswa(key: Int): Mahasiswa
}