package com.example.finalassignment.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    private var username : String = ""
    private var password : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!checkInternetConnection()) {
            Toast.makeText(
                this,
                "No Internet connection , please switch on the wifi or mobile data",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    else{
        getSharedPref()
        if (username!=""){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = UserRepository()
                    val response = repository.userLogin(username, password)
                    if (response.success == true) {
                        ServiceBuilder.token = "Bearer ${response.token}"
                        ServiceBuilder.usertype = response.data!!.accounttype
                        ServiceBuilder.userID = response.data!!._id
                        ServiceBuilder.username = response.data!!.fullname
                        val intent = Intent(
                            this@SplashActivity,
                            Home_Menu_Activity::class.java
                        )
                        startActivity(intent)
                    } else {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                LoginActivity::class.java
                            )
                        )
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@SplashActivity,
                            "Login error ${ex}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }else{
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }
        }
    }

    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        username = sharedPref.getString("username", "").toString()
        password = sharedPref.getString("password", "").toString()
    }
    private fun checkInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}