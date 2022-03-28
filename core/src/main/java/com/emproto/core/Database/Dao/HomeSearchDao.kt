package com.emproto.core.Database.Dao

import androidx.room.*
import com.emproto.core.Database.TableModel.SearchModel


@Dao
interface HomeSearchDao : BaseDao<SearchModel>{

    @Query("SELECT * FROM search_table ORDER BY id DESC")
    fun  getAllSearchDetails() : List<SearchModel>


}


