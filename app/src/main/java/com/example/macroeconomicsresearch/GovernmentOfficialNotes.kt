package com.example.macroeconomicsresearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.employee.notes.NotesAdapter

class GovernmentOfficialNotes : AppCompatActivity() {
    private lateinit var salesDatabase:MacroEconomicsDatabase
    private lateinit var viewModel: NotesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView: View
    private  var notesList: List<Notes> =ArrayList()
    private var searchList: MutableList<Notes> = ArrayList()
    private lateinit var searchEditText: EditText
    private lateinit var adapter: NotesAdapter
    private lateinit var noNotes : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_government_official_notes)
        salesDatabase= MacroEconomicsDatabase.getInstance(this)
        viewModel = NotesViewModel(salesDatabase)
        viewModel.getAllNotes()
        recyclerView=findViewById(R.id.recyclerview_notes)
        noNotes=findViewById(R.id.no_orders)
        recyclerView.setHasFixedSize(true)
        adapter= NotesAdapter(notesList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this)
        //recyclerView.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        viewModel.notesListLiveData.observe(this, Observer {notes->
            notesList=notes
            adapter.notes=notes
            adapter.notifyDataSetChanged()
            if(notesList.isEmpty()){
                noNotes.visibility=View.VISIBLE;
            }
            else{
                noNotes.visibility=View.GONE;
            }
        })

    }
}