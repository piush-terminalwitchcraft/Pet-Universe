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
import com.example.petuniverse.adapters.UserUploadedPetsAdapter
import com.example.petuniverse.data.firestoreData
import com.example.petuniverse.models.petsDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var UserPetsRecyclerView: RecyclerView
    private lateinit var UserPetsAdapter: UserUploadedPetsAdapter
    private lateinit var UserPetList:ArrayList<petsDetails>
    private lateinit var documentID : ArrayList<String>
    private lateinit var UserName : TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        auth = Firebase.auth
        UserPetsRecyclerView = view.findViewById(R.id.user_uploaded_pets_recyclerview)
        UserName = view.findViewById(R.id.user_name)
        if(auth.currentUser!= null){
            UserName.text = auth.currentUser!!.displayName
        }
        UserPetsRecyclerView.layoutManager = LinearLayoutManager(context)
        UserPetsRecyclerView.setHasFixedSize(true)
        UserPetList = arrayListOf()
        documentID = arrayListOf()

        UserPetsAdapter = UserUploadedPetsAdapter(UserPetList,documentID)
        UserPetsRecyclerView.adapter = UserPetsAdapter

        EventChangeListener()
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Pets").whereEqualTo("user",auth.currentUser?.email.toString())
            .addSnapshotListener { value, error ->
                if(error==null){
                    if (value != null) {
                        for(dc in value.documentChanges){
                            if(dc.type == DocumentChange.Type.ADDED){
                                UserPetList.add(dc.document.toObject(petsDetails::class.java))
                                documentID.add(dc.document.id)
                            } else {
                                val idx = documentID.indexOfFirst { it==dc.document.id }
                                if(idx != -1){
                                    if(dc.type == DocumentChange.Type.REMOVED){
                                        UserPetList.removeAt(idx)
                                        documentID.removeAt(idx)
                                    } else{
                                        val obj = dc.document.toObject(petsDetails::class.java)
                                        UserPetList[idx] = obj
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    Log.e("ERROR","firestore error")
                }
                UserPetsAdapter.notifyDataSetChanged()
            }
    }

}