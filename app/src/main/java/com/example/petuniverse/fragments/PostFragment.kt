package com.example.petuniverse.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.petuniverse.R
import com.example.petuniverse.models.AddViewModel
import com.example.petuniverse.models.Status
import com.example.petuniverse.models.petsDetails
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostFragment : Fragment() {

    private val addViewModel : AddViewModel by activityViewModels()
    val IMAGE_CODE = 16
    private var filePath: Uri? = null
    private lateinit var textFieldCategory: AutoCompleteTextView
    private lateinit var textFieldDescription : TextInputLayout
    private lateinit var Description : TextInputEditText
    private lateinit var Name : TextInputEditText
    private lateinit var Price : TextInputEditText
    private lateinit var Image : ImageView
    private lateinit var firebaseStore: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addViewModel.onCreatePostFragment()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        auth = Firebase.auth

        textFieldCategory = view.findViewById(R.id.category)
        textFieldDescription  = view.findViewById(R.id.textfield_category)
        Name = view.findViewById(R.id.name)
        Price = view.findViewById(R.id.price)
        Description = view.findViewById(R.id.description)
        Image = view.findViewById(R.id.pet_image2)
        val itemsCategory = listOf("Cats", "Dogs", "Birds", "Fish","Others")
        val adapterCategory = ArrayAdapter(requireContext(), R.layout.item_lists, itemsCategory)
        textFieldCategory.setAdapter(adapterCategory)

        if(Description.text?.length!! > 60){
            textFieldDescription.error = "Not More than 60 characters!"
            Toast.makeText(context,"Not more than 60 characters!",Toast.LENGTH_LONG).show()
        }
        else{
            textFieldDescription.error = null
        }



        addViewModel.status.observe(viewLifecycleOwner, Observer { it->
            if(it == Status.PENDING){
                Toast.makeText(context,"Pending",Toast.LENGTH_LONG).show()
                if(checkErrors()){
                    uploadImage()
                }
                else{
                    addViewModel.setstatus(Status.DONE)
                }

            }
        })

        Image.setOnClickListener { getPermissions() }
        return view
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context?.let { checkSelfPermission(it,Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED
                || context?.let { checkSelfPermission(it,Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 121)
                getPermissions()
            } else {
                openGallary()
            }
        }
    }

    private fun openGallary() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        filePath = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CODE)

    }

    private fun uploadData(imgPath: String) {
        Log.e("Here!" ,"UploadData")
        val PetsInfo = petsDetails(auth.currentUser!!.email.toString(),
            Price.text.toString().toInt(),imgPath,textFieldCategory.text.toString(),
            Description.text.toString(),Name.text.toString(),"Male")
        val db = FirebaseFirestore.getInstance().collection("Pets")
            .add(PetsInfo)
            .addOnSuccessListener {
                Toast.makeText(context,"Uploaded",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                addViewModel.setstatus(Status.DONE)
            }


    }

    private fun uploadImage() {
        if(filePath != null){
            val imgPath = "PetsImage/" + auth.currentUser!!.email.toString()+
                    SimpleDateFormat("dd.MM.yyyy'|'HH.mm.ss").format(Date()).toString()
            val ref = storageReference.child(imgPath)
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    uploadData(imgPath)
                }
                .addOnFailureListener{
                    Toast.makeText(context,"File Uploading Failed",Toast.LENGTH_SHORT).show()

                }

        }
    }

    private fun checkErrors(): Boolean {
        if(Price.text.isNullOrEmpty()){
            Toast.makeText(context,"Enter Price!",Toast.LENGTH_LONG).show()
            return false
        }
        if(Name.text.isNullOrEmpty()){
            Toast.makeText(context,"Enter Name of pet!",Toast.LENGTH_LONG).show()
            return false
        }
        if(textFieldCategory.text.isNullOrEmpty()){
            Toast.makeText(context,"Choose atleast one Category!",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver,filePath)
                val compressedBitmap = Bitmap.createScaledBitmap(bitmap,600,600,true)
                Image.setImageBitmap(compressedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addViewModel.setstatus(Status.INITIAL)
    }
}