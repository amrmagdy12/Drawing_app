package com.android.drawingapp.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import com.android.drawingapp.R
import petrov.kristiyan.colorpicker.ColorPicker

class Colorlistener(private val colorPicker: ColorPicker ,val context: Context , val colorlist:ArrayList<String>) : ColorPicker.OnChooseColorListener  {

    var color:String = "#FF000000"

    private fun savecolor(color:String) {
        this.color = color
        ViewDraw.setColor(color)
    }


    override fun onChooseColor(position: Int, color: Int) {

        if (position != -1 )
            savecolor(colorlist.get(position))
        colorPicker.dismissDialog()

    }

    override fun onCancel() {
        colorPicker.dismissDialog()
    }


}