package com.example.macroeconomicsresearch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class],version = 1,exportSchema = false)
abstract class MacroEconomicsDatabase: RoomDatabase(){
    abstract val notesDao:NotesDao
    companion object{

        @Volatile
        private var INSTANCE: MacroEconomicsDatabase? = null

        fun getInstance(context: Context): MacroEconomicsDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            MacroEconomicsDatabase::class.java,
                            "pebble_user_database"
                    ).fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}