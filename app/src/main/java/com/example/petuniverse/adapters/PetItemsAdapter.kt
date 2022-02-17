package com.example.petuniverse.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petuniverse.LogIn
import com.example.petuniverse.R
import com.example.petuniverse.SignIn
import com.example.petuniverse.ViewPetDetails
import com.example.petuniverse.models.petsDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson


class PetItemsAdapter
    (private val petLists : ArrayList<petsDetails>, private val documentID: ArrayList<String>)
    : RecyclerView.Adapter<PetItemsAdapter.ViewHolder>(){

    val imageRef = Firebase.storage.reference
    val auth = Firebase.auth
    override fun getItemCount(): Int {
        return petLists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pet_item_lists,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val petData = petLists[position]
        holder.petName.text = petData.petName.toString()
        holder.petPrice.text =  holder.itemView.context.getString(R.string.price,petData.price.toString())
        holder.petCategory.text = petData.category.toString()
        when(petData.status){
            1-> {
                holder.status.apply {
                    background = ResourcesCompat.getDrawable(resources,R.color.red,null)
                    text = "Sold"
                }
            }
            else-> {
                holder.status.visibility = View.GONE
            }
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
                    Toast.makeText(holder.itemView.context,it.toString(), Toast.LENGTH_LONG).show()
                    holder.petImage.setImageResource(R.color.backgroundcolor)
                }
        }
        else{
            holder.petImage.setImageResource(R.color.secondary)
        }

        holder.cardView.setOnClickListener {
           if(auth.currentUser?.email  != petData.user){
               val gson = Gson()
               val intent = Intent(holder.itemView.context,ViewPetDetails::class.java)
               intent.putExtra("PetDetail",gson.toJson(petData))
               holder.itemView.context.startActivity(intent)
           }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val petName = itemView.findViewById<TextView>(R.id.pet_name_rv)
        val petCategory = itemView.findViewById<TextView>(R.id.pet_category_rv)
        val petPrice = itemView.findViewById<TextView>(R.id.pet_price_rv)
        val petImage = itemView.findViewById<ImageView>(R.id.pet_image_view_rv)
        val cardView = itemView.findViewById<CardView>(R.id.pet_item_list_cardview)
        val status = itemView.findViewById<TextView>(R.id.pet_status_rv)
    }

}