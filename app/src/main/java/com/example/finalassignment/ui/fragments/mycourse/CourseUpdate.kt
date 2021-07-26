package com.example.finalassignment.ui.fragments.mycourse

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.ui.Home_Menu_Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

class CourseUpdate : AppCompatActivity(), View.OnClickListener {
    private lateinit var etupdatecoursetitle : EditText
    private lateinit var etupdatecoursedesc : EditText
    private lateinit var etupdatecoursecategory : Spinner
    private lateinit var tvupdatefile : ImageView
    private lateinit var etupdatefiletitle : EditText
    private lateinit var btnupdatecourse : Button
    private lateinit var backbutton : ImageView
    private val category = arrayOf("Android","WebDevelopment", "IOT")
    private lateinit var selectedItem : String
    lateinit var selectedFile : Uri
    private lateinit var mFile: File
    lateinit var displayName:String
    private var REQUEST_FILE_CODE = 0
    private var fileUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_update)

        etupdatecoursetitle = findViewById(R.id.etupdatecoursetitle)
        etupdatecoursedesc = findViewById(R.id.etupdatecoursedesc)
        etupdatecoursecategory = findViewById(R.id.etupdatecoursecategory)
        etupdatefiletitle = findViewById(R.id.etupdatefiletitle)
        tvupdatefile = findViewById(R.id.tvupdatefile)
        backbutton = findViewById(R.id.backbutton)
        btnupdatecourse = findViewById(R.id.btnupdatecourse)

        val course = intent.getParcelableExtra<CourseDetails>("course")
        if(course != null){
            etupdatecoursetitle.setText(course.courseTitle)
            etupdatecoursedesc.setText(course.courseDesc)
            selectedItem = "${course.courseCategory}"
            etupdatefiletitle.setText(course.fileTitle)
        }

        btnupdatecourse.setOnClickListener(this)
        backbutton.setOnClickListener(this)
        tvupdatefile.setOnClickListener(this)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            category
        )
        etupdatecoursecategory.adapter = adapter

        etupdatecoursecategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedItem = parent?.getItemAtPosition(position).toString()
                    Toast.makeText(this@CourseUpdate, "$selectedItem", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnupdatecourse -> {
                updateCourse()
            }
        }
        when(v?.id) {
            R.id.backbutton -> {
                startActivity(Intent(this, Home_Menu_Activity::class.java))
            }
        }
        when(v?.id){
            R.id.tvupdatefile->{
                openStorage()
            }
        }
    }

    private fun updateCourse() {
        val courseTitle = etupdatecoursetitle.text.toString()
        val courseDesc = etupdatecoursedesc.text.toString()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            category
        )
        etupdatecoursecategory.adapter = adapter

        etupdatecoursecategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedItem = parent?.getItemAtPosition(position).toString()
                    Toast.makeText(this@CourseUpdate, "$selectedItem", Toast.LENGTH_SHORT).show()
                }
            }
        val fileTitle = etupdatefiletitle.text.toString()



        CoroutineScope(Dispatchers.IO).launch {
            try {
                val course = CourseDetails(courseTitle= courseTitle, courseCategory = selectedItem, courseDesc = courseDesc, fileTitle = fileTitle)
                val courses = intent.getParcelableExtra<CourseDetails>("course")
                val courseRepository = CourseRepository()
                //val id = "607c25aec2eea0187c04d3ea"
                val id = courses?._id!!
                val response = courseRepository.updateCourse(id,course)
                if(response.success == true){
                    if(fileUrl != null) {
                        uploadFile(id)
                    }
                    val intent = Intent(
                        this@CourseUpdate,
                        Home_Menu_Activity::class.java
                    )
                    startActivity(intent)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CourseUpdate,
                            "Courses Updated", Toast.LENGTH_SHORT
                        ).show()
                    }
                    showHighPriorityPostNotification()

                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CourseUpdate,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun openStorage() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/*"
        intent = Intent.createChooser(intent, "Choose a file")
        startActivityForResult(intent, REQUEST_FILE_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FILE_CODE && data != null) {
            selectedFile = data.data!!
            data.data?.also {
                fileUrl = it.path
                mFile = importFile(it);

            }
        }
    }
    //return a new file created in application cache directory
    private fun importFile(uri: Uri): File {
        val fileName: String = getFileName(uri)
        Log.i("TAGGER", "Display Name: $fileName")
        // The temp file could be whatever you want
        val directory = applicationContext.cacheDir
        val Tempfile  = File(directory,fileName);
        val fileCopy: File = copyToTempFile(uri, Tempfile)
        return fileCopy;
    }
    // return the fileName from given URI
    private fun getFileName(uri: Uri): String {
        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null
        )
        cursor?.use {
            // moveToFirst() returns false if the cursor has 0 rows. Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (it.moveToFirst()) {

                // Note it's called "Display Name". This is
                // provider-specific, and might not necessarily be the file name.
                val fileName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                displayName = fileName
            }
        }
        return displayName;
    }
    //copies the inputStream from given file to a temporary file.
    @Throws(IOException::class)
    private fun copyToTempFile(uri: Uri, tempFile: File): File {
        // Obtain an input stream from the uri
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to obtain input stream from URI")
        // Copy the stream to the temp file
        FileUtils.copyInputStreamToFile(inputStream, tempFile);

        return tempFile
    }
    private fun uploadFile(courseID: String){
        println(mFile)
        val reqFile =
            RequestBody.create(MediaType.parse("file/${mFile.extension}"), mFile)
        val body = MultipartBody.Part.createFormData("courseFile", mFile.name, reqFile)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val courseRepository = CourseRepository()
                val response = courseRepository.uploadFile(courseID, body)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CourseUpdate, "Uploaded",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }   catch (ex : Exception){
                withContext(Dispatchers.Main) {
                    Log.d("Error uploading file ", ex.toString())
                    Toast.makeText(
                        this@CourseUpdate,
                        ex.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
    private fun showHighPriorityPostNotification() {
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannel()

        val notification = NotificationCompat.Builder(this,notificationChannels.CHANNEL_1)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Course Updated.")
            .setContentText("Hello ${ServiceBuilder.username}, You have updated a Course!!")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(1, notification)
    }
}