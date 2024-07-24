package com.garth.prjapibasics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.w3c.dom.Text
import java.util.concurrent.Executors

class Create : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val fBtnLoans = findViewById<FloatingActionButton>(R.id.fBtnLoans)
        val fBtnMembers = findViewById<FloatingActionButton>(R.id.fBtnMembers)
        val fBtnCreate = findViewById<FloatingActionButton>(R.id.fBtnCreate)
        val txtAmount = findViewById<TextView>(R.id.txtAmount)
        val txtMemberID = findViewById<TextView>(R.id.txtMemberID)
        val txtMessage = findViewById<TextView>(R.id.txtMessage)
        val btnCreate = findViewById<Button>(R.id.btnCreate)

        fBtnMembers.setOnClickListener{
            val intent = Intent(this@Create, Members::class.java)
            startActivity(intent)
        }
        fBtnLoans.setOnClickListener {
            val intent = Intent(this@Create, MainActivity::class.java)
            startActivity(intent)
        }
        btnCreate.setOnClickListener {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute{
                val user = LoanPost(txtAmount.text.toString(), txtMemberID.text.toString(), txtMessage.text.toString())
                val (_, _, result) = "https://opsc20240710154110.azurewebsites.net/AddLoan".httpPost()
                    .jsonBody(Gson().toJson(user ).toString())
                    .responseString()
                val Json = "[" + result.component1() + "]"
                val userList = Gson().fromJson(Json, Array<Loan>::class.java).toList()

                Handler(Looper.getMainLooper()).post{
                    var Text = findViewById<TextView>(R.id.txtOutput)
                    Text.setText(userList.toString())
                }
            }
        }
    }
}