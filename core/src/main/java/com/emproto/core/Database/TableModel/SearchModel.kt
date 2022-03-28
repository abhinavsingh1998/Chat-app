package com.emproto.core.Database.TableModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_table")
data class SearchModel(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") val id: Int=0,
    @ColumnInfo(name = "searchname") val searchName: String
    )