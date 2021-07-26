package com.example.finalassignment.ui.fragments.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.eknowledgelibrary.entities.Users
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.repository.UserRepository
import com.example.finalassignment.ui.Home_Menu_Activity
import com.example.finalassignment.ui.fragments.home.HomeFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileUpdateActivity : AppCompatActivity() {
    private lateinit var etPFullname : EditText
    private lateinit var etPUsername : EditText
    private lateinit var etPPhone : EditText
    private lateinit var etPAddress : EditText
    private lateinit var etPEmail : EditText
    private lateinit var etPBio : EditText
    private lateinit var btnPeditprofile : Button
    private lateinit var btnChangeImage : ImageView
    private lateinit var backButton : ImageView
    private lateinit var Uppimage : CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)
        etPFullname = findViewById(R.id.etPFullname)
        etPUsername = findViewById(R.id.etPUsername)
        etPPhone = findViewById(R.id.etPPhone)
        etPAddress = findViewById(R.id.etPAddress)
        etPEmail = findViewById(R.id.etPEmail)
        etPBio = findViewById(R.id.etPBio)
        btnPeditprofile = findViewById(R.id.btnPeditprofile)
        btnChangeImage = findViewById(R.id.btnChangeImage)
        backButton = findViewById(R.id.backButton)
        Uppimage = findViewById(R.id.Uppimage)
        getUserData()

        backButton.setOnClickListener {
            startActivity(Intent(this, Home_Menu_Activity::class.java))
        }
        btnPeditprofile.setOnClickListener {
            updateUser()
        }
        btnChangeImage.setOnClickListener{
            loadPopUpMenu()
        }

    }

    private fun getUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getUser()
                if(response.success == true){
                    withContext(Dispatchers.Main) {
                        val user = response.data!!
                        val image = ServiceBuilder.loadImagePath() + user.profile
                        etPFullname.setText("${user.fullname}")
                        etPUsername.setText("${user.username}")
                        etPEmail.setText("${user.email}")
                        etPAddress.setText("${user.address}")
                        etPPhone.setText("${user.phone}")
                        etPBio.setText("${user.bio}")
                        if (!user.profile.equals("")) {
                            Glide.with(this@ProfileUpdateActivity)
                                .load(image)
                                .fitCenter()
                                .into(Uppimage)
                        }
                    }
                }

            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@ProfileUpdateActivity,
                        "Error : ${ex}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
    private fun updateUser(){
        val fullname = etPFullname.text.toString()
        val username = etPUsername.text.toString()
        val email = etPEmail.text.toString()
        val address = etPAddress.text.toString()
        val phone = etPPhone.text.toString()
        val bio = etPBio.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = Users(fullname = fullname, username = username, email = email, address = address, phone = phone, bio = bio)
                val userRepository = UserRepository()
                //val uid = "605a14e53753d62534a59739"
                val uid = ServiceBuilder.userID!!
                val response = userRepository.updateUser(uid,user)
                if(response.success == true){
                    withContext(Dispatchers.Main) {
                    if (profileUrl != null){
                        uploadProfile(ServiceBuilder.userID!!)
                    }
                        val intent = Intent(
                            this@ProfileUpdateActivity,
                            Home_Menu_Activity::class.java
                        )
                        startActivity(intent)
                        Toast.makeText(
                            this@ProfileUpdateActivity,
                            "User Updated", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ProfileUpdateActivity,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this, Uppimage)
        popupMenu.menuInflater.inflate(R.menu.file_upload, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.browseStorage ->
                    openStorage()
            }
            true
        }
        popupMenu.show()
    }
    private var REQUEST_STORAGE_CODE = 0
    private var profileUrl: String? = null
    private fun openStorage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_STORAGE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_STORAGE_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                profileUrl = cursor.getString(columnIndex)
                Uppimage.setImageBitmap(BitmapFactory.decodeFile(profileUrl))
                cursor.close()
            }
        }
    }
    private fun uploadProfile(userID: String){
        if (profileUrl != null){
            val file = File(profileUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("image/"+file.extension.toLowerCase().replace("jpg","jpeg")),file)

            val body = MultipartBody.Part.createFormData("profile", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.uploadProfile(userID, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            
                        }
                    }
                }   catch (ex : Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProfileUpdateActivity,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}