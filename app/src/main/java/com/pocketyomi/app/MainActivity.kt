package com.pocketyomi.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(36,60,36,30); setBackgroundColor(0xffffeaf3.toInt()) }
  fun label(s:String)=TextView(this).apply { text=s; textSize=20f; setPadding(0,10,0,20); root.addView(this) }
  fun btn(s:String, f:()->Unit)=Button(this).apply { text=s; setOnClickListener{f()}; root.addView(this) }
  label("🐧 Pocket Yomi · 뽀몽 에디션")
  label("핑크 테마 · 장난꾸러기\n첫 버전은 화면 위에 뜨는 펫과 드래그를 시험해요.")
  btn("1. 화면 위 표시 권한 허용") { startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) }
  btn("2. 뽀몽 입주시키기") { if(Settings.canDrawOverlays(this)) startService(Intent(this,PetService::class.java)) else startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) }
  btn("3. 펫 숨기기") { stopService(Intent(this,PetService::class.java)) }
  label("참고: 이 소스에는 임시 펭귄 아이콘이 들어 있어요. 공식 뽀몽이 투명 PNG를 pet.png로 넣으면 교체돼요.")
  setContentView(root)
 }
}
