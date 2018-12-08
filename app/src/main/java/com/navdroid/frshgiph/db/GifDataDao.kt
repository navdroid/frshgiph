package com.navdroid.frshgiph.db

import android.arch.persistence.room.*
import com.navdroid.frshgiph.model.Data
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GifDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Data)

    @Delete()
    fun delete(data: Data)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: ArrayList<Data>)

    @Query("SELECT * from gif_table")
    fun getAll(): Flowable<List<Data>>


    @Query("SELECT * from gif_table where uid=:id")
    fun getById(id: String): Single<List<Data>>

    @Query("SELECT * from gif_table where isFavorite=1")
    fun getAllFavorite(): Flowable<List<Data>>

    @Query("UPDATE gif_table SET isFavorite=:isFavorite   WHERE uid = :id")
    fun updateLoads(id: String, isFavorite: Boolean = true)
}
