package com.example.finalassignment.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.ui.CoursesActivity

class Dashboard : Fragment(), View.OnClickListener {
    private lateinit var textdashboard: TextView
    private lateinit var txtandroidbtn: TextView
    private lateinit var txtwebbtn: TextView
    private lateinit var txtiotbtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        textdashboard = view.findViewById(R.id.textdashboard)
        txtandroidbtn = view.findViewById(R.id.txtandroidbtn)
        txtwebbtn = view.findViewById(R.id.txtwebbtn)
        txtiotbtn = view.findViewById(R.id.txtiotbtn)

        txtandroidbtn.setOnClickListener(this)
        txtiotbtn.setOnClickListener(this)
        txtwebbtn.setOnClickListener(this)


        val username = ServiceBuilder.username!!.split(" ").toTypedArray()
        textdashboard.setText("Welcome back \n${username[0]}!")
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtandroidbtn->{
                android()
            }
            R.id.txtwebbtn->{
                web()
            }
            R.id.txtiotbtn->{
                iot()
            }
        }

    }

    private fun iot() {
        val intent = Intent(context, CoursesActivity::class.java)
        intent.putExtra("cetagory", "IOT")
        startActivity(intent)
    }

    private fun web() {
        val intent = Intent(context, CoursesActivity::class.java)
        intent.putExtra("cetagory", "WebDevelopment")
        startActivity(intent)
    }

    private fun android() {
        val intent = Intent(context, CoursesActivity::class.java)
        intent.putExtra("cetagory", "Android")
        startActivity(intent)
    }
}