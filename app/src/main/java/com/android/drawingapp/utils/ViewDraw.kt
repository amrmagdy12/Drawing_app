package com.android.drawingapp.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import android.util.TypedValue
import android.view.SurfaceView
import kotlin.math.*


class ViewDraw(context: Context, attr: AttributeSet) : View(context, attr) {

    private lateinit var path2: Path
    private var path: Path = Path()
    private var paint: Paint = Paint()
    private lateinit var drawcanvas: Canvas
    private lateinit var canvasbitmap: Bitmap

    private var dpSize = 2

    var startx: Float = 0F
    var starty: Float = 0F
    var endx: Float = 0F
    var endy: Float = 0F

    private val TOUCH_TOLERANCE = 4

    // used for getting the exact touch down point when drawing circle
    val radius = 30

    private val dm = resources.displayMetrics
    private val strokeWidth =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize.toFloat(), dm)


    init {
        paint = Paint(Paint.DITHER_FLAG)
        paint.color = Color.parseColor(paintcolor)
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true

        paint.strokeWidth = strokeWidth
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND

    }

    companion object {
        private var paintcolor: String = "#FF000000"
        private var polygonType = -1

        fun setPolygonType(type: Int) {
            this.polygonType = type
        }

        fun setColor(color: String) {
            this.paintcolor = color
        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        canvasbitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawcanvas = Canvas(canvasbitmap)

    }


    override fun onDraw(canvas: Canvas?) {
        val left = 0
        val top = 0


        canvas?.drawBitmap(canvasbitmap, left.toFloat(), top.toFloat(), paint)
        //canvas?.drawPath(path, paint)


        when (polygonType) {

            1 -> drawRect(canvas)
            2 -> drawArrow(canvas)
            3 -> drawCircle(canvas)
        }


    }


    fun drawRect(canvas: Canvas?) {
        paint.color = Color.parseColor(paintcolor)
        canvas?.drawRect(
            startx,
            starty,
            endx,
            endy,
            paint
        )
    }

    fun drawArrow(canvas: Canvas?) {
        paint.color = Color.parseColor(paintcolor)

        // arrow line
        canvas?.drawLine(startx, starty, endx, endy, paint)

        val dx = (endx - startx)
        val dy = (endy - starty)

        val length = sqrt(dx * dx + dy * dy)

        val unitdx = dx / length
        val unitdy = dy / length

        val arrowsize = 30

        val p1 = PointF(
            (endx) - unitdx * arrowsize - unitdy * arrowsize,
            endy - unitdy * arrowsize + unitdx * arrowsize
        )

        val p2 = PointF(
            (endx) - unitdx * arrowsize + unitdy * arrowsize,
            endy - unitdy * arrowsize - unitdx * arrowsize
        )


        // draw a triangle [arrow head ]
        path2 = Path()

        path2.fillType = Path.FillType.EVEN_ODD

        path2.moveTo(endx, endy)


        path2.lineTo(p1.x, p1.y)

        path2.moveTo(endx, endy)

        path2.lineTo(p2.x, p2.y)

        canvas?.drawPath(path2, paint)

        path2.close()


    }

    fun drawCircle(canvas: Canvas?) {
        paint.color = Color.parseColor(paintcolor)
        val path2 = Path()
        path2.addOval(startx, starty, endx, endy, Path.Direction.CCW)

        path2.close()
        canvas?.drawPath(path2, paint)
    }

    private fun touchStart(x: Float, y: Float) {
        paint.color = Color.parseColor(paintcolor)

        path.moveTo(x, y)
        startx = x
        starty = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx: Float = Math.abs(x - startx)
        val dy: Float = Math.abs(y - starty)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(startx, starty, (x + startx) / 2, (y + starty) / 2)

            // Reset mX and mY to the last drawn point.
            startx = x
            starty = y

            drawcanvas.drawPath(path, paint)
        }
    }

    private fun touchUp() {
        path.reset()
    }


    private fun onActionMoveConfig(x: Float?, y: Float?) {
        when (polygonType) {

            1 -> {
                // rectangle

                endx = x!!
                endy = y!!
            }
            2 -> {
                // arrow
                endx = x!!
                endy = y!!
            }
            3 -> {
                // circle
                endx = x!! - radius / 2
                endy = y!! - radius / 2

            }

            4 -> {
                // pencil
                touchMove(x!!, y!!)

            }
            else -> {
                return
            }

        }
    }


    fun onActionUpConfig(x: Float?, y: Float?) {

        // saving the other point coordinates
        endx = x!!
        endy = y!!

        if (polygonType == 4)
            touchUp()
    }

    fun onActionDownConfig(x: Float?, y: Float?) {
        when (polygonType) {

            1 -> {
                // rectangle
                startx = x!!
                starty = y!!

            }
            2 -> {
                // arrow
                startx = x!!
                starty = y!!
            }
            3 -> {
                // circle
                startx = x!! - radius / 2
                starty = y!! - radius / 2
            }
            4 -> {
                // pencil
                touchStart(x!!, y!!)

            }

            else -> {
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

                onActionDownConfig(touchX, touchY)

                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {

                onActionMoveConfig(touchX, touchY)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                onActionUpConfig(touchX, touchY)
            }
            else -> {
                return false
            }
        }
        return true
    }


    enum class Polygon(val position: Int) {
        RECTANGLE(1), ARROW(2), CIRCLE(3), PENCIL(4)
    }
}