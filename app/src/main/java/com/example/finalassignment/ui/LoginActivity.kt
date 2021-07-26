package com.example.finalassignment.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var tvsignup : TextView
    private lateinit var btnlogin : Button
    private lateinit var etuser : EditText
    private lateinit var etpass : EditText
    private lateinit var linearLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvsignup = findViewById(R.id.tvsignup)
        btnlogin = findViewById(R.id.btnlogin)
        etuser = findViewById(R.id.etuser)
        etpass = findViewById(R.id.etpass)
        linearLayout = findViewById(R.id.linearlayout)

        tvsignup.setOnClickListener(this)
        btnlogin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvsignup -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            R.id.btnlogin -> {
                checkUser()
            }
        }
    }

    private fun checkUser() {
        val username = etuser.text.toString().trim()
        val password = etpass.text.toString().trim()
        if (validateInput()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepository()
                    val response = repository.userLogin(username, password)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer ${response.token}"
                        ServiceBuilder.usertype = response.data!!.accounttype
                        ServiceBuilder.userID = response.data!!._id
                        ServiceBuilder.username = response.data!!.fullname
                        saveSharedPref()
                        showHighPriorityLoginNotification()
                        val intent = Intent(
                            this@LoginActivity,
                            Home_Menu_Activity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        withContext(Main) {
                            val snack =
                                Snackbar.make(
                                    linearLayout,
                                    "Invalid credentials",
                                    Snackbar.LENGTH_LONG
                                )
                            snack.setAction("OK", View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login error ${ex}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun showHighPriorityLoginNotification() {
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannel()

        val notification = NotificationCompat.Builder(this,notificationChannels.CHANNEL_1)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("User Login notification.")
            .setContentText("Hello ${ServiceBuilder.username}, Welcome to eKnowledge!!")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(1, notification)
    }

    fun validateInput(): Boolean {
        if (etuser.text.toString() == "") {
            etuser.setError("Please enter username")
            return false
        }
        if (etpass.text.toString() == "") {
            etpass.setError("Please enter password")
            return false
        }
        return true
    }
    private fun saveSharedPref() {
        val username = etuser.text.toString()
        val password = etpass.text.toString()
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }


}