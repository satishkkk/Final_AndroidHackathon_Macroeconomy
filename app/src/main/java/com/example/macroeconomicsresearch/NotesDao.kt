package com.example.macroeconomicsresearch

import androidx.room.*

@Dao
interface NotesDao:BaseDao<Notes> {
    @Insert
    fun addNote(note: Notes)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): MutableList<Notes>

    @Update
    fun updateNote(note: Notes)

}