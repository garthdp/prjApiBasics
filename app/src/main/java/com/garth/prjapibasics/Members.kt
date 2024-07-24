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

class Members : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_members)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fBtnLoans = findViewById<FloatingActionButton>(R.id.fBtnLoans)
        val fBtnMembers = findViewById<FloatingActionButton>(R.id.fBtnMembers)
        val fBtnCreate = findViewById<FloatingActionButton>(R.id.fBtnCreate)
        val txtMemberID = findViewById<TextView>(R.id.txtMemberID)
        val btnFindMember = findViewById<Button>(R.id.btnFindMember)

        fBtnLoans.setOnClickListener{
            val intent = Intent(this@Members, MainActivity::class.java)
            startActivity(intent)
        }
        fBtnCreate.setOnClickListener {
            val intent = Intent(this@Members, Create::class.java)
            startActivity(intent)
        }

        btnFindMember.setOnClickListener {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute{
                try{
                    val url = URL("https://opsc20240710154110.azurewebsites.net/GetLoansByMember/${txtMemberID.text}")
                    val json = url.readText()
                    val userList = Gson().fromJson(json, Array<Loan>::class.java).toList()

                    Handler(Looper.getMainLooper()).post {
                        Log.d("AddNewUser", "Plain Json Vars :" + json.toString())
                        Log.d("AddNewUser", "Converted Json :" + userList.toString())
                        var Text = findViewById<TextView>(R.id.txtOutput)
                        Text.setText(userList.toString())
                    }
                }
                catch(e:Exception){
                    Log.d("AddNewUser", "Error: " + e.toString())
                    var Text = findViewById<TextView>(R.id.txtOutput)
                    Text.setText("Error: " + e.toString())
                }
            }
        }
    }
}