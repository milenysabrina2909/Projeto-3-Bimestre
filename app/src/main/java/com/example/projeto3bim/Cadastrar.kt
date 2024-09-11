package com.example.projeto3bim

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Cadastrar : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastrar)
        firebaseAuth = FirebaseAuth.getInstance()

        val btn: Button = findViewById(R.id.btnCad)

        btn.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtemail).text.toString()
            val senha = findViewById<EditText>(R.id.txtsenha).text.toString()
            val confsenha = findViewById<EditText>(R.id.txtconfirm).text.toString()


            if (email.isNotEmpty() && senha.isNotEmpty() && confsenha.isNotEmpty()) {

                if (senha == confsenha) {

                    firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Usuarios::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "A senha não corresponde", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Campos vazios não são permitidos!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
