package com.example.petuniverse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.R
import com.example.petuniverse.data.firestoreData
import com.firebase.ui.database.FirebaseRecyclerAdapter

class RecyclerAdapter(val lists: ArrayList<firestoreData>,val documentID: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val user = lists[position]
        holder.txt1.text = user.ProfilePic
        holder.txt2.text = user.ab
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txt1 = itemView.findViewById<TextView>(R.id.profile_pic)
        val txt2 = itemView.findViewById<TextView>(R.id.ab)
    }


}