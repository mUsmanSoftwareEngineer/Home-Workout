package fitnessapp.workout.homeworkout.stretching

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityRestDayBinding
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.utils.Utils

class RestDayActivity : BaseActivity(), CallbackListener {


    var binding: ActivityRestDayBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest_day)


        init()
    }


    private fun init() {

        binding!!.topbar.isBackShow = true
        binding!!.topbar.tvTitleText.text = getString(R.string.rest_day)
        binding!!.topbar.toolbarTitle.visibility = View.VISIBLE
        binding!!.topbar.toolbar.setBackgroundResource(R.color.green_seven_fit)
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()


        binding!!.btnFinished.setOnClickListener {
            finish()
        }

    }

    inner class TopClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getActivity(), view!!)

            if (value.equals(getString(R.string.back))) {
                finish()
            }

        }
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun onCancel() {
        TODO("Not yet implemented")
    }

    override fun onRetry() {
        TODO("Not yet implemented")
    }

    inner class ClickHandler{

    }
}