package com.amiri.downloader

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amiri.downloader.databinding.ActivityMainBinding
import okhttp3.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        download()
    }

    var myDownloadID: Long = 0
    private fun download() {
        binding.btnDl.setOnClickListener {
            Toast.makeText(this, "آماده سازی برای دانلود...", Toast.LENGTH_SHORT).show()
            val fileFormat =
                binding.edtLink.text.toString().substringAfterLast(".").substringBeforeLast("/")
            Log.d("File format", fileFormat)
            var request =
                DownloadManager.Request(
                    Uri.parse(
                        binding.edtLink.text.toString()
                    )
                )
                    .setTitle(binding.edtName.text.toString())
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setAllowedOverMetered(true)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOCUMENTS,
                        "my_downloads/${binding.edtName.text}.$fileFormat"
                    )

            var dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            myDownloadID = dm.enqueue(request)
        }

        //see a message that shows download completed
        var br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == myDownloadID) {
                    Toast.makeText(applicationContext, "دانلود کامل شد", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}


