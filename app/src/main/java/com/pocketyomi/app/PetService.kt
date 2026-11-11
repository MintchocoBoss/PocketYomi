package com.pocketyomi.app

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.graphics.drawable.GradientDrawable
import kotlin.math.abs

class PetService:Service() {
 private lateinit var wm:WindowManager
 private var bubble:View?=null
 private var params:WindowManager.LayoutParams?=null
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {
  if(!Settings.canDrawOverlays(this)) { stopSelf();return START_NOT_STICKY }
  if(bubble!=null)return START_NOT_STICKY
  wm=getSystemService(WINDOW_SERVICE) as WindowManager
  val prefs=getSharedPreferences("pet",MODE_PRIVATE)
  val frame=FrameLayout(this)
  val img=ImageView(this)
  val drawableId=resources.getIdentifier("pet","drawable",packageName)
  if(drawableId!=0) img.setImageResource(drawableId)
  else {
   val fallback=TextView(this).apply {text="🐧";textSize=44f;gravity=Gravity.CENTER}
   frame.addView(fallback,FrameLayout.LayoutParams(120,120))
  }
  if(drawableId!=0)frame.addView(img,FrameLayout.LayoutParams(120,120))
  val bg=GradientDrawable().apply {setColor(0x44ffc1dc);cornerRadius=60f}
  frame.background=bg
  val p=WindowManager.LayoutParams(120,120,WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
   WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
   PixelFormat.TRANSLUCENT).apply {gravity=Gravity.TOP or Gravity.START;x=prefs.getInt("x",30);y=prefs.getInt("y",260)}
  var startX=0;var startY=0;var downX=0f;var downY=0f
  frame.setOnTouchListener { _,event -> when(event.actionMasked){
   MotionEvent.ACTION_DOWN->{startX=p.x;startY=p.y;downX=event.rawX;downY=event.rawY;true}
   MotionEvent.ACTION_MOVE->{p.x=startX+(event.rawX-downX).toInt();p.y=startY+(event.rawY-downY).toInt();wm.updateViewLayout(frame,p);true}
   MotionEvent.ACTION_UP->{prefs.edit().putInt("x",p.x).putInt("y",p.y).apply();if(abs(event.rawX-downX)<12 && abs(event.rawY-downY)<12){frame.animate().rotationBy(15f).setDuration(150).withEndAction{frame.animate().rotation(0f).duration=150}.start()};true}
   else->false
  }}
  wm.addView(frame,p);bubble=frame;params=p
  return START_NOT_STICKY
 }
 override fun onDestroy(){bubble?.let{wm.removeView(it)};bubble=null;super.onDestroy()}
}
