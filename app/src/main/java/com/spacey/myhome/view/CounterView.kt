package com.spacey.myhome.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.spacey.myhome.R

class CounterView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val minusButton: ImageButton
    private val plusButton: ImageButton
    private val counterText: TextView

    private var onCountChangeListener: OnCountChangedListener? = null

    var counter: Int = 0
        set(value) {
            field = value
            counterText.text = value.toString()
            onCountChangeListener?.onCountChanged(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.counter_view_layout, this, true)

        minusButton = findViewById(R.id.minus_button)
        plusButton = findViewById(R.id.plus_button)
        counterText = findViewById(R.id.counter_text)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterView)
        counter = typedArray.getInt(R.styleable.CounterView_count, 0)
        typedArray.recycle()

        plusButton.setOnClickListener { ++counter }
        minusButton.setOnClickListener { --counter }
    }

    fun setOnCountChangeListener(onCountChanged: OnCountChangedListener) {
        onCountChangeListener = onCountChanged
    }

    fun interface OnCountChangedListener {
        fun onCountChanged(newCount: Int)
    }

}