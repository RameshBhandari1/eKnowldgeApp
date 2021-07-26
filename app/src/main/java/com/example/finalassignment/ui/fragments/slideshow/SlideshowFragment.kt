package com.example.finalassignment.ui.fragments.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalassignment.R
import com.example.finalassignment.ui.AboutUsActivity

class SlideshowFragment : Fragment() {
    private lateinit var map : ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        map = root.findViewById(R.id.map)

        map.setOnClickListener {
            startActivity(Intent(requireContext(), AboutUsActivity::class.java))
        }

        return root
    }
}