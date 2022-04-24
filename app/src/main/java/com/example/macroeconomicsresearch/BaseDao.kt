package com.example.macroeconomicsresearch

import androidx.room.Insert

interface BaseDao<T> {

    @Insert
    fun insert(obj: T)

}