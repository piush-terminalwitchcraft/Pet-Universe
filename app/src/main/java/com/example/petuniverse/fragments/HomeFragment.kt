package com.example.petuniverse.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.R
import com.example.petuniverse.SearchActivity
import com.example.petuniverse.adapters.PetItemsAdapter
import com.example.petuniverse.models.petsDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class HomeFragment : Fragment() {
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
    private lateinit var search : EditText
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
        search = view.findViewById(R.id.search_edittext)
        recentItemsRecyclerView.apply {
            LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        recommendedItemsRecyclerView.apply {
            LinearLayoutManager(context)
            setHasFixedSize(true)
        }
// https:// curl -d "grant_type=client_credentials&client_id={CLIENT-ID}&client_secret={CLIENT-SECRET}"api.petfinder.com/v2/oauth2/token
        search.setOnClickListener {
            startActivity(Intent(context,SearchActivity::class.java))
        }

        if(auth.currentUser != null){
            ("Welcome back\n" + auth.currentUser!!.displayName.toString()).also { welcomeProfile.text = it }
        }

        recentPetListAdapter = PetItemsAdapter(recentPetList,recentPetListdocumentID)
        recentItemsRecyclerView.adapter = recentPetListAdapter
        EventChangeListener()

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