package com.example.projeto3bim

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto3bim.models.UsuaModelo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class Atualizar : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private var usuaList: ArrayList<UsuaModelo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atualizar)
        val btn: Button = findViewById(R.id.btnUp)
        val btn2: Button = findViewById(R.id.btndesconectar)

        btn2.setOnClickListener {
            val intent = Intent(this@Atualizar, MainActivity::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {
            val nomeUP = findViewById<EditText>(R.id.nomUp).text.toString()
            val descrUp = findViewById<EditText>(R.id.descUp).text.toString()

            dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuaList.clear()
                    if (snapshot.exists()) {
                        for (favSnap in snapshot.children) {
                            val favData = favSnap.getValue(UsuaModelo::class.java)
                            usuaList.add(favData!!)
                        }
                        if (usuaList.isNotEmpty()) {
                            var id = usuaList[0].usuId.toString()
                            val usuaInfo = UsuaModelo(id, nomeUP, descrUp)
                            dbRef.child(id).setValue(usuaInfo)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                }
            })
        }

    }
}