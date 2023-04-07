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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amiri.downloader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val download = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            download()
        } catch (e: Exception) {
            Toast.makeText(this, "خطا رخ داد", Toast.LENGTH_LONG).show()
        }

    }
    var myDownloadID: Long = 0
    private fun download() {

        binding.btnDl.setOnClickListener {
            Toast.makeText(this, "آماده سازی برای دانلود...", Toast.LENGTH_SHORT).show()
            try {
                val fileFormat =
                    binding.edtLink.text.toString().substringAfterLast(".").substringBeforeLast("/")
                Log.d("File format", fileFormat)
                val request =
                    DownloadManager.Request(
                        Uri.parse(
                            binding.edtLink.text.toString()
                        )
                    )
                        .setTitle(binding.edtName.text.toString())
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setAllowedOverMetered(true)
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOCUMENTS,
                            "my_downloads/${binding.edtName.text}.$fileFormat"
                        )
                val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                myDownloadID = dm.enqueue(request)

                downloadComplete()

                val name =  binding.edtName.text.toString()
                download.add(name)
                val dlNameAdapter: ArrayAdapter<String> =
                    ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, download)

                binding.dlList.adapter = dlNameAdapter

            } catch (illigal: IllegalArgumentException) {

                Toast.makeText(this, "خطا رخ داد", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun downloadComplete() {
        //see a message that shows download completed
        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == myDownloadID) {
                    Toast.makeText(applicationContext, "دانلود کامل شد", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

}


