package com.example.projeto3bim

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SelectDoc : AppCompatActivity() {

    private lateinit var stgRef: StorageReference
    private lateinit var dbRef: DatabaseReference
    private lateinit var btn: Button
    private lateinit var editText: EditText

    companion object {
        private const val REQUEST_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectdoc)

        btn = findViewById(R.id.btnUpDocs)
        val btnon: Button = findViewById(R.id.btnRetrive)
        editText = findViewById(R.id.editText)

        stgRef = FirebaseStorage.getInstance().reference
        dbRef = FirebaseDatabase.getInstance().getReference("uploadPDF")

        btn.isEnabled = false

        editText.setOnClickListener {
            selectPDF()
        }

        btnon.setOnClickListener {
            val intent = Intent(this@SelectDoc, ListaDoc::class.java)
            startActivity(intent)
        }
    }

    private fun selectPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        val chooser = Intent.createChooser(intent, "PDF FILE SELECT")
        startActivityForResult(chooser, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data?.data != null) {
            btn.isEnabled = true

            val uri: Uri = data.data!!
            val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "Unknown file"
            editText.setText(fileName)

            btn.setOnClickListener {
                uploadPDFFileFirebase(uri)
            }
        }
    }

    private fun uploadPDFFileFirebase(uri: Uri) {
        val progressDialog = ProgressDialog(this).apply {
            setTitle("File is loading...")
            show()
        }

        val fileReference = stgRef.child("uploadPDF${System.currentTimeMillis()}.pdf")

        fileReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        val downloadUri = uriTask.result
                        val putPDF = PdfModelo(editText.text.toString(), downloadUri.toString())
                        val key = dbRef.push().key
                        if (key != null) {
                            dbRef.child(key).setValue(putPDF)
                        }

                        Toast.makeText(this, "File Upload", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_LONG).show()
                    }
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(this, "Upload failed: ${exception.message}", Toast.LENGTH_LONG).show()
            }
            .addOnProgressListener { snapshot ->
                val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                progressDialog.setMessage("File Uploading.. $progress%")
            }
    }
}