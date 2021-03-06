package com.example.macroeconomicsresearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(database:MacroEconomicsDatabase):ViewModel() {
    val notesDao = database.notesDao

    private val _notesLiveData = MutableLiveData<Notes>()
    val notesLiveData: LiveData<Notes>
        get() = _notesLiveData

    private val _notesListLiveData = MutableLiveData<MutableList<Notes>>()
    val notesListLiveData: LiveData<MutableList<Notes>>
        get() = _notesListLiveData

    fun insert(title: String, id: String) {
        viewModelScope.launch {
            val notes = Notes(id=id,title = title)
            _notesLiveData.value = notes
            withContext(Dispatchers.IO) {
                notesDao.insert(notes)
            }
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            _notesListLiveData.value = getAll()
        }

    }

    fun getAllNotesFinal() {
        viewModelScope.launch {
            getAll()
        }

    }
    private suspend fun getAll(): MutableList<Notes> {
        return withContext(Dispatchers.IO) {
            val notes: MutableList<Notes> = notesDao.getAllNotes()
            notes
        }

    }

    fun update(notes: Notes) {
        viewModelScope.launch {
            _notesLiveData.value = notes
            withContext(Dispatchers.IO) {
                notesDao.updateNote(notes)
            }

        }
    }
}