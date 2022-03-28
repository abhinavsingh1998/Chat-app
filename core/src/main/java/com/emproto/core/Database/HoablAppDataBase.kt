package com.emproto.core.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emproto.core.Constants
import com.emproto.core.Database.Dao.HomeSearchDao
import com.emproto.core.Database.TableModel.SearchModel


@Database(entities = [SearchModel::class], version = 1)
abstract class HoablAppDataBase : RoomDatabase() {

    abstract fun homeSearchDao(): HomeSearchDao

    companion object {
        private var instance: HoablAppDataBase? = null

        // on below line we are getting instance for our database.
        open fun getAppDataBaseInstance(context: Context): HoablAppDataBase? {
            // below line is to check if the instance is null or not.
            if (instance == null) {
                // if the instance is null we are creating a new instance for creating a instance for our database
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    HoablAppDataBase::class.java,
                    Constants.DATABASE_NAME.toString()
                )
                    // below line is use to add fall back to destructive migration to our database.
                    .allowMainThreadQueries()
                    //  .addCallback(roomCallback) // below line is to add callback to our database.
                    .build()
            }
            // after creating an instance we are returning our instance
            return instance!!
        }

    }

}