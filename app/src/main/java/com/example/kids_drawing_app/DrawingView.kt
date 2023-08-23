package com.example.kids_drawing_app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths= ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        Log.d("DrawingSetup", "This is working")
        mDrawPaint = Paint()
        mDrawPaint!!.color = color
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    // Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        for (path in mPaths){
            mDrawPaint!!.strokeWidth = path!!.brushThickness
            mDrawPaint!!.color = path!!.color
            canvas.drawPath(path!!, mDrawPaint!!)

        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }
    private val TAG = "TouchEvents"

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "Action DOWN at position: x=$touchX, y=$touchY")

                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()

                if (touchY != null) {
                    if (touchX != null) {
                        mDrawPath!!.moveTo(touchX, touchY)
                        Log.d(TAG, "Moved path to position: x=$touchX, y=$touchY")
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "Action MOVE at position: x=$touchX, y=$touchY")

                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)
                        Log.d(TAG, "Lined path to position: x=$touchX, y=$touchY")
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "Action UP at position: x=$touchX, y=$touchY")

                mDrawPath = CustomPath(color, mBrushSize)
                mPaths.add((mDrawPath!!))
            }

            else -> {
                Log.d(TAG, "Unrecognized action")
                return false
            }
        }
invalidate()
        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path() {


    }

}