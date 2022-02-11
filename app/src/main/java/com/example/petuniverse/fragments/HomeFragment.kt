package com.example.petuniverse.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.R
import com.example.petuniverse.adapters.RecyclerAdapter
import com.example.petuniverse.data.firestoreData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.common.collect.Lists
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObjects


class HomeFragment : Fragment() {
    lateinit var userdata : ArrayList<firestoreData>
    lateinit var documentID : ArrayList<String>
    lateinit var adapter : RecyclerAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)




//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.setHasFixedSize(true)
//
//        userdata = arrayListOf()
//        documentID = arrayListOf()
//
//        adapter = RecyclerAdapter(userdata,documentID)
//        recyclerView.adapter = adapter
//        EventChangeListener()
        return view
    }

//    private fun EventChangeListener() {
//        db = FirebaseFirestore.getInstance()
//        db.collection("User")
//            .addSnapshotListener { value, error ->
//                if(error == null) {
//                    if (value != null) {
//                        for(dc in value.documentChanges){
//                            if(dc.type == DocumentChange.Type.ADDED){
//                                userdata.add(dc.document.toObject(firestoreData::class.java))
//                                documentID.add(dc.document.id)
//                            }
//                            else {
//                                val idx = documentID.indexOfFirst { it==dc.document.id }
//                                if(idx != -1){
//                                    if(dc.type == DocumentChange.Type.REMOVED){
//                                        userdata.removeAt(idx)
//                                        documentID.removeAt(idx)
//                                    }
//                                    else{
//                                        val obj = dc.document.toObject(firestoreData::class.java)
//                                        userdata[idx].ProfilePic = obj.ProfilePic
//                                        userdata[idx].ab = obj.ab
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged()
//
//            }
//    }


}