package com.example.imentoruapp

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

class Home : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val headerTxt = view.findViewById<TextView>(R.id.txtTitle)
        val paint = headerTxt.paint
        val width = paint.measureText(headerTxt.text.toString())
        headerTxt.paint.shader = LinearGradient(
            0f,0f,width,headerTxt.textSize, intArrayOf(
                Color.parseColor("#00aee0"),
                Color.parseColor("#00fedc"),
                Color.parseColor("#00aee0")
            ),null, Shader.TileMode.REPEAT
        )
        val btnMentee = view.findViewById<Button>(R.id.btnMentee)
        val btnMentor = view.findViewById<Button>(R.id.btnMentor)
        val btnSubscribe = view.findViewById<Button>(R.id.btnSubscribe)
        btnMentee.setOnClickListener {
            navigateToFragment(Mentee())
        }
        btnMentor.setOnClickListener {
            navigateToFragment(Mentors())
        }

        btnSubscribe.setOnClickListener {
            navigateToFragment(NewsLetter())
        }


        return  view
    }
    private fun navigateToFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }



}