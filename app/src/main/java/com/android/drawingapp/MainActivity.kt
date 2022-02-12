package com.android.drawingapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.databinding.DataBindingUtil
import com.android.drawingapp.databinding.ActivityMainBinding

import com.android.drawingapp.utils.Colorlistener
import com.android.drawingapp.utils.ViewDraw
import petrov.kristiyan.colorpicker.ColorPicker

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var colorlistener: Colorlistener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)



        binding.paletteImgview.setOnClickListener {

           val colorlist = ArrayList<String>(resources.getStringArray(R.array.color_array).toMutableList())

            val colorPicker = ColorPicker(this)
            colorPicker.setColors(colorlist)
            colorlistener = Colorlistener(colorPicker , this , colorlist)
            colorPicker.setOnChooseColorListener(colorlistener)
            colorPicker.show()
        }

        binding.pencilImgview.setOnClickListener {
            ViewDraw.setPolygonType(ViewDraw.Polygon.PENCIL.position)
        }


        binding.arrowImgview.setOnClickListener {
            ViewDraw.setPolygonType(ViewDraw.Polygon.ARROW.position)
        }

        binding.squareImgview.setOnClickListener {
            ViewDraw.setPolygonType(ViewDraw.Polygon.RECTANGLE.position)
        }

        binding.circleImgview.setOnClickListener {
            ViewDraw.setPolygonType(ViewDraw.Polygon.CIRCLE.position)

        }
    }
}