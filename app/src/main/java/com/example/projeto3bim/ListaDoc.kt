package com.example.projeto3bim

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListaDoc : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var listView: ListView
    private val documentList = mutableListOf<String>()
    private val documentUriMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listadoc)

        listView = findViewById(R.id.ListView)
        dbRef = FirebaseDatabase.getInstance().getReference("uploadPDF")

        fetchDocuments()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedDocument = documentList[position]
            val uri = documentUriMap[selectedDocument]
            uri?.let {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(it), "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                }
                startActivity(intent)
            } ?: Toast.makeText(this, "Failed to open document", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDocuments() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                documentList.clear()
                documentUriMap.clear()
                for (data in snapshot.children) {
                    val fileName = data.child("name").getValue(String::class.java)
                    val fileUri = data.child("url").getValue(String::class.java)
                    if (fileName != null && fileUri != null) {
                        documentList.add(fileName)
                        documentUriMap[fileName] = fileUri
                    }
                }
                updateListView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListaDoc, "Failed to load documents", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateListView() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, documentList)
        listView.adapter = adapter
    }
}