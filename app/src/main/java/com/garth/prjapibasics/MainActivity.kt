package com.garth.prjapibasics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtOutput = findViewById<TextView>(R.id.txtOutput)
        val btnFind = findViewById<Button>(R.id.btnFindLoan)
        val txtLoanID = findViewById<TextView>(R.id.txtLoanID)
        val btnFindAllLoans = findViewById<Button>(R.id.btnFindAllLoans)
        val fBtnLoans = findViewById<FloatingActionButton>(R.id.fBtnLoans)
        val fBtnMembers = findViewById<FloatingActionButton>(R.id.fBtnMembers)
        val fBtnCreate = findViewById<FloatingActionButton>(R.id.fBtnCreate)

        fBtnMembers.setOnClickListener{
            val intent = Intent(this@MainActivity, Members::class.java)
            startActivity(intent)
        }
        fBtnCreate.setOnClickListener {
            val intent = Intent(this@MainActivity, Create::class.java)
            startActivity(intent)
        }

        btnFind.setOnClickListener{
            txtOutput.setText("Test")
            val executor = Executors.newSingleThreadExecutor()
            executor.execute{
                try{
                    val url = URL("https://opsc20240710154110.azurewebsites.net/getloan/${txtLoanID.text}")
                    val json = url.readText()
                    val userList = Gson().fromJson(json, Loan::class.java)

                    Handler(Looper.getMainLooper()).post {
                        Log.d("AddNewUser", "Plain Json Vars :" + json.toString())
                        Log.d("AddNewUser", "Converted Json :" + userList.toString())
                        txtOutput.setText(userList.toString())
                    }
                }
                catch(e:Exception){
                    Log.d("AddNewUser", "Error: " + e.toString())
                    txtOutput.setText("Error: " + e.toString())
                }
            }
        }
        btnFindAllLoans.setOnClickListener{
            val executor = Executors.newSingleThreadExecutor()
            executor.execute{
                try{
                    val url = URL("https://opsc20240710154110.azurewebsites.net/GetAllLoans")
                    val json = url.readText()
                    val userList = Gson().fromJson(json, Array<Loan>::class.java).toList()

                    Handler(Looper.getMainLooper()).post {
                        Log.d("AddNewUser", "Plain Json Vars :" + json.toString())
                        Log.d("AddNewUser", "Converted Json :" + userList.toString())
                        txtOutput.setText(userList.toString())
                    }
                }
                catch(e:Exception){
                    Log.d("AddNewUser", "Error: " + e.toString())
                    txtOutput.setText("Error: " + e.toString())
                }
            }
        }
    }
}