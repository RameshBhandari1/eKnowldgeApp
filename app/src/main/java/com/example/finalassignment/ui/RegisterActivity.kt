package com.example.finalassignment.ui

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.eknowledgelibrary.entities.Users
import com.example.finalassignment.R
import com.example.finalassignment.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvsignin: TextView
    private lateinit var btnregister: Button
    private lateinit var etfullname: EditText
    private lateinit var etemail: EditText
    private lateinit var etusername: EditText
    private lateinit var etpassword: EditText
    private lateinit var etcpassword: EditText
    private lateinit var rgaccounttype: RadioGroup
    private lateinit var rbprofessor: RadioButton
    private lateinit var rblearner: RadioButton
    private lateinit var linearLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvsignin = findViewById(R.id.tvsignin)
        btnregister = findViewById(R.id.btnregister)
        etfullname = findViewById(R.id.etfullname)
        etemail = findViewById(R.id.etemail)
        etusername = findViewById(R.id.etusername)
        etpassword = findViewById(R.id.etpassword)
        etcpassword = findViewById(R.id.etcpassword)
        rgaccounttype = findViewById(R.id.rgaccounttype)
        rbprofessor = findViewById(R.id.rbprofessor)
        rblearner = findViewById(R.id.rblearner)
        linearLayout = findViewById(R.id.linearlayout1)

        tvsignin.setOnClickListener(this)
        btnregister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvsignin -> {
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
            }
            R.id.btnregister -> {
                insertUser()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun insertUser() {
        val fullname = etfullname.text.toString()
        val email = etemail.text.toString().trim()
        val username = etusername.text.toString().trim()
        val password = etpassword.text.toString().trim()
        val confirmPassword = etcpassword.text.toString().trim()
        var accountType = ""
        when {
            rbprofessor.isChecked -> {
                accountType = "Professor"
            }
            rblearner.isChecked -> {
                accountType = "Learner"
            }
        }

        if (password != confirmPassword) {
            etpassword.error = "Password does not match!!"
            etpassword.requestFocus()
            return
        } else {
            val users = Users(
                fullname = fullname,
                email = email,
                username = username,
                password = password,
                accounttype = accountType
            )
            CoroutineScope(Dispatchers.IO).launch {
                // for API
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.userRegister(users)
                    if (response.success == true) {
                        withContext(Main) {

                        }
                    }
                } catch (ex: Exception) {
                    withContext(Main) {
                        val snack =
                            Snackbar.make(
                                linearLayout,
                                "Username and Email cannot be Duplicate!!",
                                Snackbar.LENGTH_SHORT
                            )
                        snack.show()
                    }
                }
            }
        }
    }
}