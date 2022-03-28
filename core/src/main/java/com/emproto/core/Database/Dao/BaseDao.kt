package com.emproto.core.Database.Dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(t: T?)

    @Update
    fun update(t: T?)

    @Delete
    fun delete(t: T?)
}