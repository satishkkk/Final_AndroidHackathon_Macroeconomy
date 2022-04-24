package com.example.macroeconomicsresearch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
private lateinit var viewModel: NotesViewModel
private lateinit var noteTitle: EditText
private lateinit var save_button: Button
class NotesActivity : AppCompatActivity(), View.OnClickListener {
    var macroEconomicsDatabase: MacroEconomicsDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        macroEconomicsDatabase = MacroEconomicsDatabase.getInstance(this)
        viewModel = NotesViewModel(macroEconomicsDatabase!!)
        noteTitle=findViewById(R.id.notes_title)
        save_button=findViewById(R.id.save_button)
        save_button.setOnClickListener (this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.save_button -> {
                val title=noteTitle.text.toString().trim()
                if(title.isEmpty()){
                    noteTitle.error="Note required"
                    noteTitle.requestFocus()
                }
                    viewModel.insert(title,"string")
                    Toast.makeText(this,"Note Saved", Toast.LENGTH_SHORT).show()

                //else{
//                    notes!!.title=title
//                    notes!!.note=description
//                    Log.i("notes data","${notes!!.id}${notes!!.note}${notes!!.title}")
//                    viewModel.update(notes!!)
//                    Toast.makeText(this.requireContext()," Notes Updated", Toast.LENGTH_SHORT).show()
               // }

            }
        }
    }
}