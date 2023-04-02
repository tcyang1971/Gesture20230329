package com.example.gesture

import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
public final class MyAppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity(), OnGestureListener, OnTouchListener {

    lateinit var txv: TextView
    lateinit var gDetector: GestureDetector
    var count:Int = 0
    lateinit var img1: ImageView
    lateinit var img2: ImageView
    lateinit var img3: ImageView
    lateinit var mper: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txv = findViewById(R.id.txv)
        txv.setTextColor(Color.parseColor("#eeee00"))
        txv.setBackgroundColor(Color.BLUE)
        txv.setTypeface(
            Typeface.createFromAsset(assets,
                "font/HanyiSentyFingerPainting.ttf"))
        txv.getBackground().setAlpha(50)  //0~255透明度值
        gDetector = GestureDetector(this, this)

        img1 = findViewById(R.id.img1)
        img2 = findViewById(R.id.img2)
        img1.setOnTouchListener(this)
        img2.setOnTouchListener(this)

        img3 = findViewById(R.id.img3)
        img3.visibility = View.GONE
        Glide.with(this)
            .load(R.drawable.happy)
            .circleCrop()
            .into(img3)
        mper = MediaPlayer()
        mper = MediaPlayer.create(this, R.raw.scenario)
        mper.start()

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (txv.text == "手勢辨別"){
            txv.text = "靜宜之美"
        }
        else{
            txv.text = "手勢辨別"
        }
        gDetector.onTouchEvent(event)
        return true
    }

    override fun onDown(p0: MotionEvent): Boolean {
        txv.text = "按下"
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
        txv.text = "按下後無後續動作"
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        txv.text = "短按"
        return true
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        if (e1.y < e2.y){
            txv.text = "向下拖曳"
        }
        else{
            txv.text = "向上拖曳"
        }

        return true
    }

    override fun onLongPress(p0: MotionEvent) {
        txv.text = "長按"
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.x < e2.x){
            txv.text = "往右快滑"
            count++
            if (count>5){
                count = 0
            }
        }
        else{
            txv.text = "往左快滑"
            count--
            if(count<0){
                count = 5
            }
        }
        txv.text = count.toString()
        var res:Int = getResources().getIdentifier("pu" + count.toString(),
            "drawable", getPackageName())
        findViewById<LinearLayout>(R.id.bg).setBackgroundResource(res)
        return true
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v==img1){
            txv.text ="精靈1"
        }
        else{
            txv.text = "精靈2"
        }
        if (event?.action == MotionEvent.ACTION_MOVE){
            v?.x = event.rawX - v!!.width/2
            v?.y = event.rawY - v!!.height/2
        }
        var r1: Rect = Rect(img1.x.toInt(), img1.y.toInt(),
            img1.x.toInt() + img1.width, img1.y.toInt() + img1.height)
        var r2: Rect = Rect(img2.x.toInt(), img2.y.toInt(),
            img2.x.toInt() + img2.width, img2.y.toInt() + img2.height)

        if(r1.intersect(r2)) {
            txv.text = "碰撞"
            img3.visibility = View.VISIBLE
        }
        else{
            img3.visibility = View.GONE
        }

        return true
    }

}