package com.example.mediahub

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView

class MainActivity : Activity() {
    private lateinit var player: VideoView
    private lateinit var status: TextView
    private val pickMedia = 41
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(15,18,24); window.navigationBarColor = Color.rgb(15,18,24)
        val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(22,30,22,22); setBackgroundColor(Color.rgb(15,18,24)) }
        val title = TextView(this).apply { text="MEDIA HUB"; textSize=25f; setTextColor(Color.WHITE); gravity=Gravity.CENTER; setPadding(0,0,0,18) }
        status = TextView(this).apply { textSize=14f; setTextColor(Color.LTGRAY); gravity=Gravity.CENTER; setPadding(0,8,0,12) }
        player = VideoView(this).apply { setBackgroundColor(Color.BLACK) }
        root.addView(title, LinearLayout.LayoutParams(-1,-2)); root.addView(status, LinearLayout.LayoutParams(-1,-2)); root.addView(player, LinearLayout.LayoutParams(-1,0,1f))
        root.addView(makeButton("בחירת קובץ וידיאו") { chooseMedia() })
        root.addView(makeButton("פתיחת הגדרות Wi‑Fi ורשתות") { startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)) })
        root.addView(makeButton("רענון מצב חיבור") { refreshNetwork() })
        setContentView(root); refreshNetwork()
    }
    private fun makeButton(label:String, action:()->Unit)=Button(this).apply { text=label; isAllCaps=false; setOnClickListener { action() }; layoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin=8 } }
    private fun chooseMedia() { val i=Intent(Intent.ACTION_OPEN_DOCUMENT).apply { addCategory(Intent.CATEGORY_OPENABLE); type="video/*" }; startActivityForResult(i,pickMedia) }
    @Deprecated("Legacy callback for compatibility")
    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?) { super.onActivityResult(requestCode,resultCode,data); if(requestCode==pickMedia && resultCode==RESULT_OK) data?.data?.let { uri -> player.setVideoURI(uri); player.setOnPreparedListener { player.start() } } }
    private fun refreshNetwork() { val cm=getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager; val caps=cm.getNetworkCapabilities(cm.activeNetwork); status.text=if(caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)==true) "מחובר לרשת Wi‑Fi" else "לא מחובר כרגע דרך Wi‑Fi" }
    override fun onResume() { super.onResume(); if(::status.isInitialized) refreshNetwork() }
}
