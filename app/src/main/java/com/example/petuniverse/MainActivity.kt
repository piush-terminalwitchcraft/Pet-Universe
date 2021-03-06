package com.example.petuniverse

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.petuniverse.fragments.HomeFragment
import com.example.petuniverse.fragments.PostFragment
import com.example.petuniverse.fragments.ProfileFragment
import com.example.petuniverse.models.AddViewModel
import com.example.petuniverse.models.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    lateinit var floatingActionButton: FloatingActionButton
    private lateinit var navigationView: NavigationView
    private lateinit var progressBar: ProgressBar
    private lateinit var Auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private val addViewModel : AddViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        floatingActionButton = findViewById(R.id.add_items)
        navigationView = findViewById(R.id.navigation_view)
        progressBar = findViewById(R.id.circular_progress_bar)
        progressBar.visibility = View.INVISIBLE
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_activity_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        supportActionBar?.hide()

        Auth = Firebase.auth
        storageReference = FirebaseStorage.getInstance().reference

        val headerView = navigationView.getHeaderView(0)
        val userName = headerView.findViewById<TextView>(R.id.user_name)
        val userEmail = headerView.findViewById<TextView>(R.id.user_email)
        val userProfilePic = headerView.findViewById<ImageView>(R.id.user_profie_picture)
        if(Auth.currentUser != null){
            userName.text = Auth.currentUser!!.displayName.toString()
            userEmail.text = Auth.currentUser!!.email.toString()
            val reference = storageReference.child("UserProfilePic/" + Auth.currentUser!!.email.toString())
            reference.downloadUrl.addOnSuccessListener {
                val bytes = reference.getBytes(5L*1024*1024)
                    .addOnSuccessListener {
                        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
                        val compressedBitmap = Bitmap.createScaledBitmap(bmp,600,600,true)
                        userProfilePic.setImageBitmap(compressedBitmap)
                    }
                    .addOnFailureListener {
                        ToastMessage(it.message.toString())
                    }
            }
                .addOnFailureListener {
                    ToastMessage(it.message.toString())
                }

        }

        bottomNavigationView.setOnItemSelectedListener {
            val destcodes = when(it.itemId) {
                R.id.bottom_nav_home -> 1
                R.id.bottom_nav_add -> 2
                else -> 3
            }
            val currcodes = getCodes(navController.currentDestination?.label)
            if(destcodes!=currcodes && addViewModel.status.value != Status.PENDING){
                navController.navigate(getFragmentTranscationID(currcodes,destcodes))

            }
            true
        }

        floatingActionButton.setOnClickListener {
            if(Auth.currentUser == null){
                Toast.makeText(this,"Login to access this section!",Toast.LENGTH_SHORT).show()
            }
            else{
                val currcodes = getCodes(navController.currentDestination?.label)
                if(2!=currcodes){
                    addViewModel.setstatus(Status.START)
                    setIcon()
                    navController.navigate(getFragmentTranscationID(currcodes,2))
                }
                else{
                    when (addViewModel.status.value) {
                        Status.START -> addViewModel.setstatus(Status.PENDING)
                        Status.PENDING -> addViewModel.setstatus(Status.DONE)
                        else -> addViewModel.setstatus(Status.PENDING)
                    }
                }
            }
        }



        addViewModel.status.observe(this, Observer { it->
            setIcon()
        })

        navigationView.setNavigationItemSelectedListener { menuItem->
            val destinationActivity = when(menuItem.itemId){
                R.id.nav_log_in_sign_out -> ActivityUser()
                R.id.nav_chats -> ChatActivity::class.java
                else -> MainActivity::class.java
            }
            startActivity(Intent(this,destinationActivity))

            true
        }
    }

    private fun ActivityUser(): Class<*>? {
        if(Auth.currentUser==null) return SignIn::class.java
        else return LogOut::class.java
    }

    private fun setIcon() {
        val resourceID = when(addViewModel.status.value){
            Status.INITIAL -> R.drawable.ic_baseline_add_24
            Status.PENDING -> {
                progressBar.visibility = View.VISIBLE
                R.drawable.circle
            }
            Status.DONE -> {
                progressBar.visibility = View.INVISIBLE
                R.drawable.ic_baseline_arrow_upward_24
            }
            else -> R.drawable.ic_baseline_arrow_upward_24
        }

        floatingActionButton.setImageDrawable(ResourcesCompat.getDrawable(resources,
            resourceID,null))

    }

    private fun getCodes(fragment_name: CharSequence?): Int {
        return when(fragment_name){
            "fragment_home" -> 1
            "fragment_post"-> 2
            else -> 3
        }
    }

    private fun getFragmentTranscationID( a:Int, b:Int): Int{
        if(a==b){ return -1 }
        else{
            if(a==1){
                return if(b==2){
                    addViewModel.setstatus(Status.START)
                    R.id.action_homeFragment_to_postFragment
                } else{
                    R.id.action_homeFragment_to_profileFragment
                }
            }
            else if(a==2){
                addViewModel.setstatus(Status.INITIAL)
                return if(b==1){
                    R.id.action_postFragment_to_homeFragment
                } else{
                    R.id.action_postFragment_to_profileFragment
                }
            }
            else{
                return if(b==1){
                    R.id.action_profileFragment_to_homeFragment
                } else {
                    addViewModel.setstatus(Status.START)
                    R.id.action_profileFragment_to_postFragment
                }
            }
        }
    }

    private fun ToastMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}