package fitnessapp.workout.homeworkout.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class CMEditTextView : AppCompatEditText {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }


    fun init() {
        try {
            typeface = Utils.getMedium(context)
        } catch (e: Exception) {
        }
    }
}