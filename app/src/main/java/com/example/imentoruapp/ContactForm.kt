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
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactForm.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactForm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var selectedProvince : String? = null

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
        val view = inflater.inflate(R.layout.fragment_contact_form, container, false)

        val provinces = view.findViewById<Spinner>(R.id.province)
        val provinceValues = arrayOf("select province","Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West", "Western Cape","Other country")

        if (provinces != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,provinceValues
            )
            provinces.adapter = adapter
        }
        provinces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedProvince = provinceValues[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        val url = "http://192.168.0.105/will_project/php/addContact.php"
        val btn = view.findViewById<LinearLayout>(R.id.submit)
        val email = view.findViewById<EditText>(R.id.email)
        val subject = view.findViewById<EditText>(R.id.subject)
        val phone = view.findViewById<EditText>(R.id.phone)
        val message = view.findViewById<EditText>(R.id.message)
        val name = view.findViewById<EditText>(R.id.name)

        btn.setOnClickListener {
            val emailText = email.text.toString()
            val subjectText = subject.text.toString()
            val nameText = name.text.toString()
            val messageText = message.text.toString()
            val phoneText = phone.text.toString()
            val provinceText = selectedProvince ?: ""

            SubmitFormTask(url, emailText, subjectText, phoneText,nameText, provinceText,messageText).execute()
        }

        return view
    }
    private class SubmitFormTask(
        private val url: String,
        private val email: String,
        private val subject: String,
        private val phone: String,
        private val name: String,
        private val province: String,
        private val message: String
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            val postData = "email=${URLEncoder.encode(email, "UTF-8")}" +
                    "&subject=${URLEncoder.encode(subject, "UTF-8")}" +
                    "&phone=${URLEncoder.encode(phone, "UTF-8")}" +
                    "&name=${URLEncoder.encode(name, "UTF-8")}" +
                    "&province=${URLEncoder.encode(province, "UTF-8")}" +
                    "&message=${URLEncoder.encode(message, "UTF-8")}"

            val urlConnection = URL(url).openConnection() as HttpURLConnection
            return try {
                urlConnection.apply {
                    requestMethod = "POST"
                    doOutput = true
                    outputStream.use { os: OutputStream ->
                        os.write(postData.toByteArray())
                    }
                }

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    urlConnection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "Error: $responseCode"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Exception: ${e.message}"
            } finally {
                urlConnection.disconnect()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Handle the result here (e.g., show a message to the user)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactForm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}