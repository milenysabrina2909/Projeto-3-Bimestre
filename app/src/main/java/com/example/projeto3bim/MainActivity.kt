package com.example.projeto3bim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        val btn: Button = findViewById(R.id.btnlog)
        val btn2: Button = findViewById(R.id.btntro)

        btn2.setOnClickListener {
            val intent = Intent(this@MainActivity, Cadastrar::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val senha = findViewById<EditText>(R.id.senha).text.toString()


            if (email.isNotEmpty() && senha.isNotEmpty()) {

                if (email.isNotEmpty() && senha.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this@MainActivity, Inicio::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Campos vazios não são permitidos!!", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }
}