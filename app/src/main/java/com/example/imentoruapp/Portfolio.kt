package com.example.imentoruapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import com.example.imentoruapp.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Portfolio : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val images = arrayOf(
        R.drawable.blog, // Replace with your image resource IDs
        R.drawable.gallery,
        R.drawable.food,
        R.drawable.sport
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_portfolio, container, false)

        val imgSwitcher = view.findViewById<ImageSwitcher>(R.id.imgSwitcher)
        val btnPrev = view.findViewById<Button>(R.id.prev)
        val btnNext = view.findViewById<Button>(R.id.next)

        imgSwitcher.setFactory {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView
        }

        // Set the initial image
        imgSwitcher.setImageResource(images[currentIndex])

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex > 0) currentIndex - 1 else images.size - 1
            imgSwitcher.setImageResource(images[currentIndex])
        }

        btnNext.setOnClickListener {
            currentIndex = if (currentIndex < images.size - 1) currentIndex + 1 else 0
            imgSwitcher.setImageResource(images[currentIndex])
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Portfolio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
