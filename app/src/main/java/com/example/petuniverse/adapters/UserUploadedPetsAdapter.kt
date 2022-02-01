package com.example.petuniverse.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.example.petuniverse.R
import com.example.petuniverse.models.petsDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserUploadedPetsAdapter(private val petLists: ArrayList<petsDetails>,private val documentID: ArrayList<String>)
    : RecyclerView.Adapter<UserUploadedPetsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_pet_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val petData = petLists[position]
        holder.petName.text = petData.petName
        holder.petDescription.text = petData.description
        ("₹" + petData.price.toString()).also { holder.petPrice.text = it }
        (petData.gender + " | " + petData.category).also { holder.petInfo.text = it }
        holder.petPrice.setOnClickListener {
            Log.e("Document ID",petData.picture.toString())
        }
        if(!petData.picture.isNullOrEmpty()){
            val pic = holder.firebaseStore.getReference(petData.picture!!)
            Glide.with(holder.itemView.context).asBitmap().load(pic.downloadUrl)
                .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {

                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })

        }
        else{
            holder.petImage.setImageResource(R.color.secondary)
        }

    }

    override fun getItemCount(): Int {
        return petLists.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val petName = itemView.findViewById<TextView>(R.id.pet_name)
        val petPrice = itemView.findViewById<TextView>(R.id.pet_price)
        val petDescription = itemView.findViewById<TextView>(R.id.pet_description)
        val petInfo = itemView.findViewById<TextView>(R.id.pet_info)
        val petImage = itemView.findViewById<ImageView>(R.id.pet_image)
        val firebaseStore: FirebaseStorage = FirebaseStorage.getInstance()
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    }

}