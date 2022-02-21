package com.devapp.fr.ui.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.devapp.fr.data.entities.googles.FingerPath
import com.devapp.fr.data.models.interfaces.HandFingerCallback
import java.util.*
import kotlin.math.abs
import kotlin.properties.Delegates
import com.devapp.fr.data.entities.googles.Point
class CanvasHandFinger:View{
    //Material
    var BRUSH_SIZE = 5f
    val TOUCH_TOLERANCE = 4
    var DEFAULT_BACKGROUND = Color.parseColor("#FF018786")
    var COLOR_PEN = Color.parseColor("#FF000000")
    //Element when draw
    private lateinit var mPaint: Paint
    private lateinit var mPath: Path
    private lateinit var listFingerPath: MutableList<FingerPath>
    private lateinit var listPoint: MutableList<Point>
    private lateinit var listTime: MutableList<Int>
    private lateinit var listPath: MutableList<Path>
    private var startTime by Delegates.notNull<Long>()
    private var isFirstDraw by Delegates.notNull<Boolean>()

    //Element Init draw
    private lateinit var mCanvas: Canvas
    private lateinit var bitmap: Bitmap
    private lateinit var mBitmapPaint : Paint

    //Current position
    private lateinit var currentPoint : Point


    //Call back when touch up
    lateinit var handFingerCallback: HandFingerCallback
    fun setCallback(callback: HandFingerCallback){
        this.handFingerCallback = callback
    }
    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        //Init ListPath
        listPath = mutableListOf()

        //Init listFingerPath
        listFingerPath = mutableListOf()

        //Init List Point
        listPoint = mutableListOf()

        //Init List Time
        listTime = mutableListOf()

        // Enables dithering when blitting
        mBitmapPaint = Paint()
        mBitmapPaint.flags = Paint.DITHER_FLAG


        mPaint = Paint()
        //Smooth path
        mPaint.isAntiAlias = true
        //Dither
        mPaint.isDither = true
        //Settings style
        mPaint.color = COLOR_PEN
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.style = Paint.Style.STROKE
        mPaint.xfermode = null
        mPaint.alpha = 0xFF

        //First Draw
        isFirstDraw = true



    }

    fun init(metrics: DisplayMetrics){
        //Get width and height of device by pixels
        val height =metrics.heightPixels
        val width = metrics.widthPixels

        //Init element bitmap and canvas
        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(bitmap)
    }



    fun clear(){
        listPath.clear()
        if(mPath!=null)mPath.reset()
        if(listFingerPath!=null) listFingerPath.clear()
        if(listPath!=null) listPath.clear()
        if(listPoint!=null) listPoint.clear()
        if(listTime!=null) listTime.clear()
        if(handFingerCallback!=null)
            handFingerCallback.onDrawFinger(listFingerPath)
        invalidate()
    }

    fun setBackground(color:Int){
        DEFAULT_BACKGROUND = color
        mCanvas.drawColor(DEFAULT_BACKGROUND)
        invalidate()
    }
    fun setFingerProperty(color:Int?,brushSize:Float?){
        if(color!=null) COLOR_PEN = color
        if(brushSize!=null) BRUSH_SIZE = brushSize
        listPath.forEach {
            mPaint.color = COLOR_PEN
            mPaint.strokeWidth = BRUSH_SIZE
            mPaint.maskFilter = null
            mCanvas.drawPath(it,mPaint)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBackground(DEFAULT_BACKGROUND)
        setFingerProperty(COLOR_PEN, BRUSH_SIZE)
        canvas?.drawBitmap(bitmap,0f,0f,mBitmapPaint)
    }

    private fun startTouch(point: Point){
        mPath = Path()
        listPath.add(mPath)
        mPath.reset()
        if(isFirstDraw){
            listTime.add(0)
            startTime = Date().time
        }else listTime.add((Date().time-startTime).toInt())
        mPath.moveTo(point.x,point.y)
        Log.d("TAG", "startTouch: startTouch")
        currentPoint = Point(point.x,point.y)
        listPoint.add(point)
    }
    private fun touchMove(point: Point){
        val dx = abs(point.x-currentPoint.x)
        val dy = abs(point.y-currentPoint.y)
        if(dx>= TOUCH_TOLERANCE||dy>= TOUCH_TOLERANCE){
            mPath.quadTo(point.x,point.y,(point.x+currentPoint.x)/2,(point.y+currentPoint.y)/2)
            currentPoint.x = point.x
            currentPoint.y = point.y
        }
        listPoint.add(point)
        isFirstDraw =false
        listTime.add((Date().time-startTime).toInt())
    }
    private fun touchUp(){
        listFingerPath.add(FingerPath(listPoint,listTime))
        listPoint = mutableListOf()
        listTime = mutableListOf()
        mPath.lineTo(currentPoint.x,currentPoint.y)
        Log.d("TAG", "touchUp: $listFingerPath")
        if(handFingerCallback!=null)
            handFingerCallback.onDrawFinger(listFingerPath)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(bitmap)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startTouch(Point(x!!,y!!))
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(Point(x!!,y!!))
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                Log.d("TAG", "onTouchEvent: Touch up")
                invalidate()
            }
        }
        return true
    }
    fun undo(){
        listFingerPath.remove(listFingerPath[listFingerPath.size-1])
        listPath.remove(listPath[listPath.size-1])
        if(handFingerCallback!=null)
            handFingerCallback.onDrawFinger(listFingerPath)
        invalidate()
    }
}