package com.example.projeto3bim

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class ListaImg : AppCompatActivity() {

    private val TAG = "WallpaperPermission"
    private val MY_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listaimg)

        makeRequest()
        setupPermissions()

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images")
        val imageList: ArrayList<Item> = ArrayList()
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        progressBar.visibility = View.VISIBLE

        val listAllTask: Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items
            //add cycle for add image url to list
            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    Log.d("item", "$it")
                    imageList.add(Item(it.toString()))
                }.addOnCompleteListener {
                    recyclerView.adapter = ImageAdapter(imageList, this)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "Permission to SET_WALLPAPER denied.")
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SET_WALLPAPER),
            MY_REQUEST_CODE)
    }
}