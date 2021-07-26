package com.example.finalassignment.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.UserRepository
import com.example.finalassignment.ui.fragments.profile.ProfileUpdateActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Home_Menu_Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home__menu_)

        if (!hasPermission()) {
            requestPermission()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, sensor, 2 * 1000 * 1000)
        }


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val hView = navView.getHeaderView(0)
        val textViewName = hView.findViewById(R.id.textViewName) as TextView
        val textViewEmail = hView.findViewById(R.id.textViewEmail) as TextView
        val textuser = hView.findViewById(R.id.textuser) as TextView
        val navImage = hView.findViewById(R.id.imageView) as ImageView
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getUser()
                if(response.success == true){
                    val  user = response.data!!
                    withContext(Dispatchers.Main) {
                        val image = ServiceBuilder.loadImagePath() + user.profile
                        textViewName.setText("${user.fullname}")
                        textViewEmail.setText("${user.email}")
                        textuser.setText("${user.accounttype}")
                        if (!user.profile.equals("")) {
                            Glide.with(this@Home_Menu_Activity)
                                .load(image)
                                .fitCenter()
                                .into(navImage)
                        }
                    }
                }

            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@Home_Menu_Activity,
                        "Error : ${ex}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_home,
                R.id.nav_postcourse,
                R.id.nav_about,
                R.id.nav_profile,
                R.id.nav_savedcourse,
                R.id.nav_mycourse
            ), drawerLayout
        )
        if (ServiceBuilder.usertype == "Learner"){
            navView.menu.removeItem(R.id.nav_postcourse)
            navView.menu.removeItem(R.id.nav_mycourse)
        }
        if (ServiceBuilder.usertype == "Professor"){
            navView.menu.removeItem(R.id.nav_savedcourse)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.nav_logout -> {
                    getSharedPref()
                    ServiceBuilder.token.equals("")
                    ServiceBuilder.username.equals("")
                    ServiceBuilder.userID.equals("")
                    ServiceBuilder.usertype.equals("")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        navView.menu.findItem(R.id.nav_updateprofile).setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.nav_updateprofile -> {
                    startActivity(Intent(this, ProfileUpdateActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            flag = false
        }
        return flag
    }

    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home__menu_, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissions, 1
        )
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        val params = this.window.attributes

        if (values < sensor!!.maximumRange) {
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            params.screenBrightness = values / 255
            window.attributes = params
        } else {
            params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            params.screenBrightness = -10f
            window.attributes = params
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}