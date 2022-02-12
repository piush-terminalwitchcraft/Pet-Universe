package com.example.petuniverse.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.R
import com.example.petuniverse.adapters.PetItemsAdapter
import com.example.petuniverse.adapters.RecyclerAdapter
import com.example.petuniverse.adapters.UserUploadedPetsAdapter
import com.example.petuniverse.data.firestoreData
import com.example.petuniverse.models.petsDetails
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.common.collect.Lists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects


class HomeFragment : Fragment() {
//    lateinit var userdata : ArrayList<firestoreData>
//    lateinit var documentID : ArrayList<String>
//    lateinit var adapter : RecyclerAdapter
    private lateinit var recentItemsRecyclerView: RecyclerView
    private lateinit var recommendedItemsRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var recentPetList:ArrayList<petsDetails>
    private lateinit var recentPetListdocumentID : ArrayList<String>
    private lateinit var recentPetListAdapter: PetItemsAdapter
    private lateinit var recommendedPetList:ArrayList<petsDetails>
    private lateinit var recommendedPetListdocumentID : ArrayList<String>
    private lateinit var recommendedPetListAdapter: PetItemsAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var welcomeProfile: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        auth = FirebaseAuth.getInstance()
        recentPetList = arrayListOf()
        recentPetListdocumentID = arrayListOf()
        recommendedPetList = arrayListOf()
        recommendedPetListdocumentID = arrayListOf()

        welcomeProfile = view.findViewById(R.id.welcome_user)
        recentItemsRecyclerView = view.findViewById(R.id.recent_items_recyclerview)
        recommendedItemsRecyclerView = view.findViewById(R.id.reccommended_items_recyclerview)
        recentItemsRecyclerView.apply {
            LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        recommendedItemsRecyclerView.apply {
            LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        if(auth.currentUser != null){
            welcomeProfile.text = "Welcome " + auth.currentUser!!.displayName.toString()
        }

        recentPetListAdapter = PetItemsAdapter(recentPetList,recentPetListdocumentID)
        recentItemsRecyclerView.adapter = recentPetListAdapter
        EventChangeListener()

//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.setHasFixedSize(true)
//
//        userdata = arrayListOf()
//        documentID = arrayListOf()
//
//        adapter = RecyclerAdapter(userdata,documentID)
//        recyclerView.adapter = adapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Pets")
            .orderBy("timestamp",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error == null) {
                    if (value != null) {
                        for(dc in value.documentChanges){
                            if(dc.type == DocumentChange.Type.ADDED){
                                recentPetList.add(dc.document.toObject(petsDetails::class.java))
                                recentPetListdocumentID.add(dc.document.id)
                            }
                            else {
                                val idx = recentPetListdocumentID.indexOfFirst { it==dc.document.id }
                                if(idx != -1){
                                    if(dc.type == DocumentChange.Type.REMOVED){
                                        recentPetList.removeAt(idx)
                                        recentPetListdocumentID.removeAt(idx)
                                    }
                                    else{
                                        val obj = dc.document.toObject(petsDetails::class.java)
                                        recentPetList[idx] = obj
                                    }
                                }
                            }
                        }
                    }
                }
                recentPetListAdapter.notifyDataSetChanged()

            }
    }


}