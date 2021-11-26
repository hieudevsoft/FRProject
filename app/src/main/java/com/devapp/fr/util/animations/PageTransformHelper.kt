package com.devapp.fr.util.animations

import android.graphics.Camera
import android.graphics.Matrix
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


object PageTransformHelper {
    class FlipHorizontalPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, pos: Float) {
            val rotation = 180f * pos
            val alpha = if (rotation > 90f || rotation < -90f) 0 else 1
            page.alpha = alpha.toFloat()
            page.pivotX = page.width * 0.5f
            page.pivotY = page.height * 0.5f
            page.rotationY = rotation
        }
    }
    class TabletPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, pos: Float) {
            val rotation = (if (pos < 0) 30f else -30f) * abs(pos)
            page.translationX = getOffsetX(rotation, page.width, page.height)
            page.pivotX = page.width * 0.5f
            page.pivotY = 0f
            page.rotationY = rotation
        }

        private fun getOffsetX(rotation: Float, width: Int, height: Int): Float {
            MATRIX_OFFSET.reset()
            CAMERA_OFFSET.save()
            CAMERA_OFFSET.rotateY(abs(rotation))
            CAMERA_OFFSET.getMatrix(MATRIX_OFFSET)
            CAMERA_OFFSET.restore()
            MATRIX_OFFSET.preTranslate(-width * 0.5f, -height * 0.5f)
            MATRIX_OFFSET.postTranslate(width * 0.5f, height * 0.5f)
            TEMP_FLOAT_OFFSET[0] = width.toFloat()
            TEMP_FLOAT_OFFSET[1] = height.toFloat()
            MATRIX_OFFSET.mapPoints(TEMP_FLOAT_OFFSET)
            return (width - TEMP_FLOAT_OFFSET[0]) * if (rotation > 0.0f) 1.0f else -1.0f
        }

        companion object {
            private val MATRIX_OFFSET: Matrix = Matrix()
            private val CAMERA_OFFSET: Camera = Camera()
            private val TEMP_FLOAT_OFFSET = FloatArray(2)
        }
    }
    class CubeOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, pos: Float) {
            page.pivotX = (if (pos < 0f) page.width.toFloat() else 0f)
            page.pivotY = page.height * 0.5f
            page.rotationY = 90f * pos
        }
    }
}