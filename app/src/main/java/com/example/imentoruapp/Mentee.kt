package com.example.imentoruapp

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Mentee : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var selectedProvince: String? = null
    private var selectedLanguage: String? = null
    private var selectedInterest: String? = null
    private var selectedFound: String? = null
    private var selectedGender: String? = null

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
        val view = inflater.inflate(R.layout.fragment_mentee, container, false)
        val headerTxt = view.findViewById<TextView>(R.id.txtTitle)
        val paint = headerTxt.paint
        val width = paint.measureText(headerTxt.text.toString())
        headerTxt.paint.shader = LinearGradient(
            0f, 0f, width, headerTxt.textSize, intArrayOf(
                Color.parseColor("#00aee0"),
                Color.parseColor("#00fedc"),
                Color.parseColor("#00aee0")
            ), null, Shader.TileMode.REPEAT
        )

        val provinces = view.findViewById<Spinner>(R.id.province)
        val provinceValues = arrayOf("select province", "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal", "Limpopo", "Mpumalanga", "Northern Cape", "North West", "Western Cape", "Other country")
        setUpSpinner(provinces, provinceValues) { selectedProvince = it }

        val language = view.findViewById<Spinner>(R.id.language)
        val languageTypes = arrayOf("Select your home language", "Afrikaans", "English", "isiNdebele", "isiXhosa", "isiZulu", "Sepedi", "Sesotho", "Setswana", "siSwati", "Tshivenda", "Xitsonga", "Other")
        setUpSpinner(language, languageTypes) { selectedLanguage = it }

        val interest = view.findViewById<Spinner>(R.id.interest)
        val interestTypes = arrayOf("Select program you want to be part of it", "Career Mentorship", "Educational Support", "Community Services Support", "Language Learning", "STEMA(Sciences, Technology, Engineering, Mathematics & Agriculture) Education", "Sports & Recreation")
        setUpSpinner(interest, interestTypes) { selectedInterest = it }

        val found = view.findViewById<Spinner>(R.id.found)
        val foundtypes = arrayOf("Select how did you find us", "Facebook", "Twitter", "Instagram", "Linkedin", "Whatsapp groups or individual", "Internet (Searching/browsing)", "YouTube", "Word of mouth", "Radio interviews", "Others")
        setUpSpinner(found, foundtypes) { selectedFound = it }

        val gender = view.findViewById<Spinner>(R.id.gender)
        val genderValues = arrayOf("Select your gender", "male", "female", "others")
        setUpSpinner(gender, genderValues) { selectedGender = it }

        val position = view.findViewById<EditText>(R.id.position)
        val benefited = view.findViewById<EditText>(R.id.benefited)
        val phone = view.findViewById<EditText>(R.id.phone)
        val occupation = view.findViewById<EditText>(R.id.occupation)
        val town = view.findViewById<EditText>(R.id.town)
        val name = view.findViewById<EditText>(R.id.name)
        val lastName = view.findViewById<EditText>(R.id.lastname)
        val idNumber = view.findViewById<EditText>(R.id.idnumber)
        val email = view.findViewById<EditText>(R.id.email)
        val summary = view.findViewById<EditText>(R.id.summary) // Add summary field

        val btn = view.findViewById<LinearLayout>(R.id.submit)
        btn.setOnClickListener {
            val formData = mapOf(
                "id_number" to idNumber.text.toString(),
                "first_name" to name.text.toString(), // Assuming first name is not captured in the form
                "last_name" to lastName.text.toString(),
                "gender" to (selectedGender ?: ""),
                "phone" to phone.text.toString(),
                "email" to email.text.toString(),
                "occupation" to occupation.text.toString(),
                "province" to (selectedProvince ?: ""),
                "vacant_application" to position.text.toString(),
                "benefited_already" to benefited.text.toString(),
                "program_interest" to (selectedInterest ?: ""),
                "program_found_lead" to (selectedFound ?: ""),
                "Home_Language" to (selectedLanguage ?: ""),
                "summary" to summary.text.toString(),
                "town_village" to town.text.toString()
            ).filterValues { it.isNotEmpty() } as Map<String, String> // Filter out any empty values

            SubmitFormTask().execute(formData)
        }

        return view
    }

    private fun setUpSpinner(spinner: Spinner, items: Array<String>, onItemSelected: (String) -> Unit) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onItemSelected(items[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private inner class SubmitFormTask : AsyncTask<Map<String, String>, Void, String>() {
        override fun doInBackground(vararg params: Map<String, String>): String {
            val formData = params[0]
            val url = URL("http://192.168.0.105/will_project/php/addMentee.php")
            val urlConnection = url.openConnection() as HttpURLConnection

            try {
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                val postData = formData.map { "${it.key}=${it.value}" }.joinToString("&")
                val outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
                outputStreamWriter.write(postData)
                outputStreamWriter.flush()
                outputStreamWriter.close()

                val responseCode = urlConnection.responseCode
                return if (responseCode == HttpURLConnection.HTTP_OK) {
                    "mentee successfully added"
                } else {
                    "Error: no mentee added, response code: $responseCode"
                }
            } finally {
                urlConnection.disconnect()
            }
        }

        override fun onPostExecute(result: String) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Mentee().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
