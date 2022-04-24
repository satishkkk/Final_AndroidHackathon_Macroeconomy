package com.example.salestracking.employee.notes

import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.macroeconomicsresearch.Notes
import com.example.macroeconomicsresearch.R

class NotesAdapter( var notes: List<Notes>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_layout, parent, false)
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.title.text = notes[position].title

    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        var title =view.findViewById<TextView>(R.id.text_view_title)

    }
    fun updateList(list: List<Notes>){
        notes=list
        notifyDataSetChanged()
    }
}