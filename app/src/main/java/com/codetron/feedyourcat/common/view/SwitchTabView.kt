package com.codetron.feedyourcat.common.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.viewpager2.widget.ViewPager2
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.ViewTabSwitchBinding

class SwitchTabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr, defStyleRes
) {

    private var _binding: ViewTabSwitchBinding? = null
    val view get() = _binding

    private var vpRef: ViewPager2? = null

    private val pagerCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setSelectedTab(position)
            }
        }
    }

    init {
        _binding = ViewTabSwitchBinding.inflate(LayoutInflater.from(context), this)

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SwitchTabView,
            defStyleAttr,
            defStyleRes
        )

        setAttr(typedArray)

        typedArray.recycle()
    }

    override fun onDetachedFromWindow() {
        view?.buttonFirst?.setOnClickListener(null)
        view?.buttonSecond?.setOnClickListener(null)
        vpRef?.unregisterOnPageChangeCallback(pagerCallback)
        _binding = null
        super.onDetachedFromWindow()
    }

    private fun setAttr(typedArray: TypedArray) {
        val textOne = typedArray.getString(R.styleable.SwitchTabView_stv_text_tab_one)
        val textTwo = typedArray.getString(R.styleable.SwitchTabView_stv_text_tab_two)
        val selectedTab = typedArray.getInt(R.styleable.SwitchTabView_stv_tab_selected, 0)

        setTextTabOne(textOne)
        setTextTabTwo(textTwo)
        setSelectedTab(selectedTab)
    }

    fun setTextTabOne(text: String?) {
        view?.buttonFirst?.text = text
    }

    fun setTextTabTwo(text: String?) {
        view?.buttonSecond?.text = text
    }

    fun setSelectedTab(index: Int) {
        when (index) {
            Tab.ONE.ordinal -> {
                setTabSelected(view?.buttonFirst, true)
                setTabSelected(view?.buttonSecond, false)
            }
            Tab.TWO.ordinal -> {
                setTabSelected(view?.buttonFirst, false)
                setTabSelected(view?.buttonSecond, true)
            }
        }
    }

    fun setOnTabClickListener(listener: ((Tab) -> Unit)?) {
        view?.buttonFirst?.setOnClickListener {
            listener?.invoke(Tab.ONE)
            vpRef?.currentItem = Tab.ONE.ordinal
        }

        view?.buttonSecond?.setOnClickListener {
            listener?.invoke(Tab.TWO)
            vpRef?.currentItem = Tab.TWO.ordinal
        }
    }

    fun setEnabledTab(isEnabled: Boolean) {
        view?.buttonFirst?.isEnabled = isEnabled
        view?.buttonSecond?.isEnabled = isEnabled
    }

    fun attachWithViewPager2(viewPager: ViewPager2) {
        if (viewPager.size > 2) throw IllegalStateException("SwitchTabView only accept two view child")

        vpRef = viewPager

        vpRef?.registerOnPageChangeCallback(pagerCallback)
        setOnTabClickListener(null)
    }

    private fun setTabSelected(button: Button?, isSelected: Boolean) {
        if (isSelected) {
            button?.setTextColor(ContextCompat.getColor(context, R.color.black))
            button?.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange_primary))
        } else {
            button?.setTextColor(ContextCompat.getColor(context, R.color.white))
            button?.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey))
        }
    }

    enum class Tab {
        ONE,
        TWO
    }

}