package com.example.imentoruapp

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsLetter : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_news_letter, container, false)
        val email = view.findViewById<EditText>(R.id.email)
        val btn = view.findViewById<Button>(R.id.btnAdd)

        val url = "http://192.168.0.105/will_project/php/subscribe.php"
        btn.setOnClickListener {
            if (email.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_LONG).show()
            } else {
                val emailText = email.text.toString()
                SendPostRequest().execute(url, emailText)
            }
        }
        return view
    }

    private inner class SendPostRequest : AsyncTask<String, Void, String?>() {
        override fun doInBackground(vararg params: String): String? {
            val url = URL(params[0])
            val email = params[1]
            val postData = "email=$email"

            return try {
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true

                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(postData)
                writer.flush()
                writer.close()

                val responseCode = conn.responseCode
                val responseMessage = conn.responseMessage
                Log.d("SendPostRequest", "Response Code: $responseCode")
                Log.d("SendPostRequest", "Response Message: $responseMessage")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    conn.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "Error: $responseMessage (Code: $responseCode)"
                }
            } catch (e: Exception) {
                Log.e("SendPostRequest", "Error: ${e.message}")
                e.printStackTrace()
                "Exception: ${e.message}"
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null && result.startsWith("New record created successfully")) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), result ?: "Failed to subscribe", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsLetter().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
