package com.example.finalassignment.ui.fragments.post_course

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.ui.Home_Menu_Activity
import org.apache.commons.io.FileUtils.copyInputStreamToFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class PostCourse : Fragment(), View.OnClickListener {
    private lateinit var etcoursetitle : EditText
    private lateinit var etcoursedesc : EditText
    private lateinit var etcoursecategory : Spinner
    private lateinit var tvfile : ImageView
    private lateinit var etfiletitle : EditText
    private lateinit var btnpostcourse : Button
    private val category = arrayOf("Android","WebDevelopment", "IOT")
    private lateinit var selectedItem : String
    lateinit var selectedFile : Uri
    private lateinit var mFile: File
    lateinit var displayName:String
    private var REQUEST_FILE_CODE = 0
    private var fileUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_course, container, false)
        etcoursetitle = view.findViewById(R.id.etcoursetitle)
        etcoursedesc = view.findViewById(R.id.etcoursedesc)
        etcoursecategory = view.findViewById(R.id.etcoursecategory)
        etfiletitle = view.findViewById(R.id.etfiletitle)
        tvfile = view.findViewById(R.id.tvfile)
        btnpostcourse = view.findViewById(R.id.btnpostcourse)

        btnpostcourse.setOnClickListener(this)
        tvfile.setOnClickListener(this)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            category
        )
        etcoursecategory.adapter = adapter

        etcoursecategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    selectedItem = parent?.getItemAtPosition(position).toString()
                    //Toast.makeText(requireContext(), "$selectedItem", Toast.LENGTH_SHORT).show()
                }
            }
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnpostcourse -> {
                addCourse()
            }
        }
        when(v?.id){
            R.id.tvfile->{
                openStorage()
            }
        }
    }
    private fun addCourse(){
        val coursetitle = etcoursetitle.text.toString()
        val coursedesc = etcoursedesc.text.toString()
        val coursecategory = selectedItem
        val etfiletitle = etfiletitle.text.toString()

        val courseDetails = CourseDetails(courseTitle = coursetitle, courseDesc = coursedesc, courseCategory= coursecategory, fileTitle = etfiletitle)

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val courseRepository = CourseRepository()
                val response = courseRepository.addCourse(courseDetails)
                if (response.success == true){
                    val id = response.data!!._id.toString()
                    showHighPriorityPostNotification()
                    if(fileUrl != null) {
                        uploadFile(id)
                        withContext(Dispatchers.Main){
                            Toast.makeText(
                               requireContext(),
                            "Course added", Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    startActivity(Intent(context, Home_Menu_Activity::class.java))
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
//    // Load pop up menu
//    private fun loadPopUpMenu() {
//        val popupMenu = PopupMenu(requireContext(), tvfile)
//        popupMenu.menuInflater.inflate(R.menu.file_upload, popupMenu.menu)
//        popupMenu.setOnMenuItemClickListener { item ->
//            when (item.itemId){
//                R.id.browseStorage ->
//                    openStorage()
//            }
//            true
//        }
//        popupMenu.show()
//    }

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
    private fun importFile(uri: Uri):File {
        val fileName: String = getFileName(uri)
        Log.i("TAGGER", "Display Name: $fileName")
        // The temp file could be whatever you want
        val directory = requireContext().applicationContext.cacheDir
        val Tempfile  = File(directory,fileName);
        val fileCopy: File = copyToTempFile(uri, Tempfile)
        return fileCopy;
    }
    // return the fileName from given URI
    private fun getFileName(uri: Uri): String {
        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        val cursor: Cursor? = requireContext().contentResolver.query(
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
        val inputStream = requireContext().contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to obtain input stream from URI")
        // Copy the stream to the temp file
         copyInputStreamToFile(inputStream, tempFile);

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
                            Toast.makeText(requireContext(), "Uploaded",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }   catch (ex : Exception){
                    withContext(Dispatchers.Main) {
                        Log.d("Error uploading file ", ex.toString())
                        Toast.makeText(
                           requireContext(),
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
    private fun showHighPriorityPostNotification() {
        val notificationManager = NotificationManagerCompat.from(requireContext())

        val notificationChannels = NotificationChannels(requireContext())
        notificationChannels.createNotificationChannel()

        val notification = NotificationCompat.Builder(requireContext(),notificationChannels.CHANNEL_1)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("New Course Added.")
            .setContentText("Hello ${ServiceBuilder.username}, You have added a New Course!!")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(1, notification)
    }
}