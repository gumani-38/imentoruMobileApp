package com.example.imentoruapp

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Sponsors : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var selectedProvince : String? = null
    private var selectedPartner : String? = null
    private var selectedInterest : String? = null
    private var selectedFound : String? = null

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
        val view = inflater.inflate(R.layout.fragment_sponsors, container, false)

        val provinces = view.findViewById<Spinner>(R.id.province)
        val partners = view.findViewById<Spinner>(R.id.partner)
        val interest = view.findViewById<Spinner>(R.id.interest)
        val found = view.findViewById<Spinner>(R.id.found)
        val provinceValues = arrayOf("select province", "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West", "Western Cape", "Other country")
        val partnerTypes = arrayOf("Select type of partner", "Partnership", "Sponsorship", "Both")
        val interestTypes = arrayOf("Select program you want to be part of it", "Career Mentorship", "Educational Support", "Community Services Support", "Language Learning", "STEMA (Sciences, Technology, Engineering, Mathematics & Agriculture) Education", "Sports & Recreation")
        val foundtypes = arrayOf("Select how did you find us", "Facebook", "Twitter", "Instagram", "Linkedin", "Whatsapp groups or individual", "Internet (Searching/browsing)", "YouTube", "Word of mouth", "Radio interviews", "Others")

        setupSpinner(provinces, provinceValues) { selectedProvince = it }
        setupSpinner(partners, partnerTypes) { selectedPartner = it }
        setupSpinner(interest, interestTypes) { selectedInterest = it }
        setupSpinner(found, foundtypes) { selectedFound = it }

        val url = "http://192.168.0.105/will_project/php/addPartner.php"
        val btn = view.findViewById<LinearLayout>(R.id.submit)
        val website = view.findViewById<EditText>(R.id.website)
        val phone = view.findViewById<EditText>(R.id.phone)
        val name = view.findViewById<EditText>(R.id.name)
        val email = view.findViewById<EditText>(R.id.email)
        val summary = view.findViewById<EditText>(R.id.summary)

        btn.setOnClickListener {
            val formData = mapOf(
                "id_number" to "", // Add ID number EditText if applicable
                "name" to name.text.toString(),
                "website" to website.text.toString(),
                "phone" to phone.text.toString(),
                "email" to email.text.toString(),
                "partnership_type" to (selectedPartner ?: ""),
                "province" to (selectedProvince ?: ""),
                "program_found_lead" to (selectedFound ?: ""),
                "program_interested" to (selectedInterest ?: ""),
                "summary" to summary.text.toString()
            )
            SubmitFormTask(url, formData).execute()
        }

        return view
    }

    private fun setupSpinner(spinner: Spinner, values: Array<String>, onSelect: (String) -> Unit) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, values)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onSelect(values[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sponsors().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private class SubmitFormTask(val url: String, val formData: Map<String, String>) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val postData = formData.map { "${it.key}=${it.value}" }.joinToString("&")
            val connection = URL(url).openConnection() as HttpURLConnection

            return try {
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                OutputStreamWriter(connection.outputStream).use { it.write(postData) }

                connection.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                e.message ?: "Error"
            } finally {
                connection.disconnect()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Assuming `context` is passed to the AsyncTask, for example, via constructor
            // Uncomment the next line to show a toast message
            // Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }
}
