package com.example.petuniverse.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.R
import com.example.petuniverse.models.petsDetails
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class UserUploadedPetsAdapter
    (private val petLists: ArrayList<petsDetails>,private val documentID: ArrayList<String>)
    : RecyclerView.Adapter<UserUploadedPetsAdapter.ViewHolder>() {

    val imageRef = Firebase.storage.reference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_pet_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val petData = petLists[position]
        holder.petName.text = petData.petName
        holder.petDescription.text = petData.description
        holder.petPrice.text = holder.itemView.context.getString(R.string.price,petData.price.toString())
        (petData.gender + " | " + petData.category).also { holder.petInfo.text = it }
        holder.petPrice.setOnClickListener {
            Log.e("Document ID",petData.picture.toString())
        }
        if(!petData.picture.isNullOrEmpty()){
            holder.petImage.setImageResource(R.drawable.circle)
            val bytes = imageRef.child(petData.picture!!).getBytes(5L*1024*1024)
                .addOnSuccessListener {
                    val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                    val compressedBitmap = Bitmap.createScaledBitmap(bmp,600,600,true)
                    holder.petImage.setImageBitmap(compressedBitmap)
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context,it.toString(),Toast.LENGTH_LONG).show()
                    holder.petImage.setImageResource(R.color.backgroundcolor)
                }



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

    }

}