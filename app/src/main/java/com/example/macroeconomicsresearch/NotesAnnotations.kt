package com.example.macroeconomicsresearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private lateinit var viewModel: NotesViewModel
private lateinit var noteTitle: EditText
private lateinit var save_button: FloatingActionButton
class NotesAnnotations : AppCompatActivity(), View.OnClickListener {
    private var checkedStrings: String? = null
    private var selectedTable: Int = 0
    private var selectedUser: Int = 0
    private var selectedCountry: String? = null
    var macroEconomicsDatabase: MacroEconomicsDatabase? = null
    var notesList:  MutableList<Notes> =ArrayList();
    var noteUpdated:  Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_notes)
            macroEconomicsDatabase = MacroEconomicsDatabase.getInstance(this)
            viewModel = NotesViewModel(macroEconomicsDatabase!!)
            val intent = intent
            selectedCountry = intent.getStringExtra("selectedCountry")
            selectedUser = intent.getIntExtra("selectedUser", 0)
            selectedTable = intent.getIntExtra("selectedTable", 0)
            checkedStrings = getIntent().getStringExtra("checkedStrings")
            noteTitle=findViewById(R.id.notes_title)
            save_button=findViewById(R.id.save_button)
            save_button.setOnClickListener(this)
            viewModel.getAllNotes();
            viewModel.notesListLiveData.observe(this, { notes ->
                notesList = notes
            })
        }

        override fun onClick(view: View?) {
            when(view?.id){
                R.id.save_button -> {
                    val title = noteTitle.text.toString().trim()
                    if (title.isEmpty()) {
                        noteTitle.error = "Note required"
                        noteTitle.requestFocus()
                        return
                    }
                    for (notes in notesList) {
                        if (notes.id.contains(checkedStrings.toString())) {
                            viewModel.update(Notes(checkedStrings.toString(), title))
                            noteUpdated = true
                        }
                    }
                    if (!noteUpdated) {
                        viewModel.insert(title, checkedStrings.toString())
                    }
                    finish()


                }
            }
        }
    }
