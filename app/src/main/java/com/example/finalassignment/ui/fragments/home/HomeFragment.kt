package com.example.finalassignment.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalassignment.R
import com.example.finalassignment.ui.CoursesActivity

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var txtiotnext : TextView
    private lateinit var txtandroidnext : TextView
    private lateinit var txtwebnext : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        txtiotnext = view.findViewById(R.id.txtiotnext)
        txtandroidnext = view.findViewById(R.id.txtandroidnext)
        txtwebnext = view.findViewById(R.id.txtwebnext)

        txtandroidnext.setOnClickListener(this)
        txtwebnext.setOnClickListener(this)
        txtiotnext.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtandroidnext->{
                android()
            }
            R.id.txtwebnext->{
                web()
            }
            R.id.txtiotnext->{
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