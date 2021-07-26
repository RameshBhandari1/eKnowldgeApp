package com.example.finalassignment.ui.fragments.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Profile : Fragment() {
    private lateinit var tvFullname : TextView
    private lateinit var tvUsername : TextView
    private lateinit var tvPhone : TextView
    private lateinit var tvAddress : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvUserType : TextView
    private lateinit var tvcount : TextView
    private lateinit var tvBio : TextView
    private lateinit var editprofile : Button
    private lateinit var ppimage : CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        tvFullname = view.findViewById(R.id.tvFullname)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvPhone = view.findViewById(R.id.tvPhone)
        ppimage = view.findViewById(R.id.ppimage)
        tvUserType = view.findViewById(R.id.tvUsertype)
        tvcount = view.findViewById(R.id.tvcount)
        tvBio = view.findViewById(R.id.tvBio)
        editprofile = view.findViewById(R.id.editprofile)

        editprofile.setOnClickListener {
             startActivity(Intent(context, ProfileUpdateActivity::class.java))
        }
        getUserData()

        return view
    }

    private fun getUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val userRepository = UserRepository()
                val response = userRepository.getUser()
                if(response.success == true){
                  val  user = response.data!!
                    val image = ServiceBuilder.loadImagePath() + user.profile
                    withContext(Dispatchers.Main) {
                        if (user.accounttype == "Professor") {
                            val CourseRepository = CourseRepository()
                            val courseResponse = CourseRepository.getMyCourses()
                            val course = courseResponse.count!!
                            tvcount.setText("Total number of courses: ${course}")
                            tvFullname.setText("${user.fullname}")
                            tvUsername.setText("${user.username}")
                            tvEmail.setText("${user.email}")
                            tvUserType.setText("${user.accounttype}")
                            tvAddress.setText("${user.address}")
                            tvPhone.setText("${user.phone}")
                            tvBio.setText("${user.bio}")
                            if (!user.profile.equals("")) {
                                Glide.with(this@Profile)
                                    .load(image)
                                    .fitCenter()
                                    .into(ppimage)
                            }
                        }else if (user.accounttype == "Learner"){
                            tvcount.visibility = View.GONE
                            tvFullname.setText("${user.fullname}")
                            tvUsername.setText("${user.username}")
                            tvEmail.setText("${user.email}")
                            tvUserType.setText("${user.accounttype}")
                            tvAddress.setText("${user.address}")
                            tvPhone.setText("${user.phone}")
                            tvBio.setText("${user.bio}")
                            if (!user.profile.equals("")) {
                                Glide.with(this@Profile)
                                    .load(image)
                                    .fitCenter()
                                    .into(ppimage)
                            }
                        }


                    }
                }
            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        context,
                        "Error : ${ex}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}