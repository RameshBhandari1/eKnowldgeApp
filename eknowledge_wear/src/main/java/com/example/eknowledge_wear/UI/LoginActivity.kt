package com.example.eknowledge_wear.UI

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.eknowledge_wear.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : WearableActivity(), View.OnClickListener {
    private lateinit var etWearUsername : EditText
    private lateinit var etWearPassword : EditText
    private lateinit var btnWearLogin : Button
    private lateinit var wearlinearlayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etWearUsername = findViewById((R.id.etWearUsername))
        etWearPassword = findViewById((R.id.etWearPassword))
        btnWearLogin = findViewById((R.id.btnWearLogin))
        wearlinearlayout = findViewById((R.id.wearlinearlayout))

        btnWearLogin.setOnClickListener(this)

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnWearLogin ->{

                checkUsers()
            }
        }
    }

    private fun checkUsers() {
        val username = etWearUsername.text.toString().trim()
        val password = etWearPassword.text.toString().trim()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.userLogin(username, password)
                if (response.success == true) {
                    ServiceBuilder.token = "Bearer ${response.token}"
                    ServiceBuilder.username = response.data!!.fullname
                    ServiceBuilder.usertype = response.data!!.accounttype
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            WearDashboardActivity :: class.java)
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        val snack =
                            Snackbar.make(
                                wearlinearlayout,
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
                        "Please Entrer Valid Username or Password", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}