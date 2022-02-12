package com.example.petuniverse

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor.compress
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import io.grpc.Compressor
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class LogOut : AppCompatActivity() {

    val IMAGE_CODE = 16
    private var filePath: Uri? = null
    private lateinit var name : EditText
    private lateinit var Email : TextView
    private lateinit var userImageView: ImageView
    private lateinit var changeUserImageView: ImageView
    private lateinit var updateProfile : CardView
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseStore: FirebaseStorage
    private lateinit var storageReference: StorageReference
    var changedProfilePic = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)
        supportActionBar?.hide()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        name = findViewById(R.id.user_name_edittext_alg)
        Email = findViewById(R.id.user_email_alg)
        userImageView = findViewById(R.id.user_profile_pic_alg)
        changeUserImageView = findViewById(R.id.change_profile_pic_alg)
        updateProfile = findViewById(R.id.update_cardview)

        changeUserImageView.setOnClickListener {
            getPermissions()
            changedProfilePic = true
        }

        auth = Firebase.auth
        name.setText(auth.currentUser!!.displayName)

        Email.text = getString(R.string.Email,auth.currentUser!!.email)

        val deleteUser: TextView = findViewById(R.id.deleteUser)
        deleteUser.setOnClickListener {
                GetPassword()
        }

        val logOutUser: TextView = findViewById(R.id.log_out_user)
        logOutUser.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

        updateProfile.setOnClickListener {
            chkUserNameChanged()
            if(changedProfilePic) {
                updateProfilePic()
            }
        }
    }

    private fun updateProfilePic() {
        val imgPath = "UserProfilePic/" + auth.currentUser!!.email.toString()
        val ref = storageReference.child(imgPath)
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val bytes = baos.toByteArray()
        ref.putBytes(bytes).addOnSuccessListener {
                ToastMessage("Uploaded Successfully")
            }
            .addOnFailureListener{
                ToastMessage(it.message.toString())
            }

    }

    private fun chkUserNameChanged() {
        if(name.text.toString()!= auth.currentUser!!.displayName){
            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(name.text.toString())
                .build()

            auth.currentUser!!.updateProfile(update)
        }
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (applicationContext?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.CAMERA
                    )
                } == PackageManager.PERMISSION_DENIED
                || applicationContext?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
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
        filePath = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                val compressedBitmap = Bitmap.createScaledBitmap(bitmap,600,600,true)
                userImageView.setImageBitmap(compressedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun GetPassword()  {
        val inputEditTextField = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Enter the password to confirm")
            .setMessage("Password")
            .setView(inputEditTextField)
            .setPositiveButton("OK") { _, _ ->
                val password = inputEditTextField .text.toString()
                verifyCredentialsAndDeleteAccount(password)

            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()

    }

    private fun verifyCredentialsAndDeleteAccount(password: String) {
        val user  = auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(user?.email.toString(),password)
        user!!.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d("Reauthenticate", "User re-authenticated.")
                user.delete()
                    .addOnSuccessListener {
                        ToastMessage("Deleted successfully")
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        ToastMessage(it.message.toString())
                    }
                }
                .addOnFailureListener {
                    ToastMessage(it.message.toString())
                }

    }


    private fun ToastMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}