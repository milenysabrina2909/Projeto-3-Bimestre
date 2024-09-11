package com.example.projeto3bim

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto3bim.models.UsuaModelo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Usuarios : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.usuarios)

        val btnon: Button = findViewById(R.id.btninsi)

        btnon.setOnClickListener{
            dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")

            val nomeusu = findViewById<EditText>(R.id.txtnome).text.toString()
            val descreiusu = findViewById<EditText>(R.id.txtdesc).text.toString()

            if (nomeusu.isEmpty()){
                findViewById<EditText>(R.id.txtnome).error ="Insira seu Nome pfv :)"
            }
            if (descreiusu.isEmpty()){
                findViewById<EditText>(R.id.txtdesc).error ="Insira sua Descrição pfv :)"
            }

            val usuid = dbRef.push().key?: ""


            val usuaa = UsuaModelo(usuid, nomeusu,descreiusu)

            dbRef.child(usuid).setValue(usuaa)
                .addOnCompleteListener{
                    Toast.makeText(this,"Dado inserido com sucesso", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener{ err->
                    Toast.makeText(this,"Erro ${err.message}", Toast.LENGTH_SHORT).show()
                }



            val intent = Intent(this@Usuarios, Inicio::class.java)
            startActivity(intent)

        }

    }
}