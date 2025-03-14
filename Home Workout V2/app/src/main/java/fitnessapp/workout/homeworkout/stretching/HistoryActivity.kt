package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityHistoryBinding
import fitnessapp.workout.homeworkout.stretching.adapter.HistoryExpandableAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import java.text.SimpleDateFormat
import java.util.*


class HistoryActivity : BaseActivity(), CallbackListener {

    var binding: ActivityHistoryBinding? = null
    var historyExpandableAdapter: HistoryExpandableAdapter? = null
    private val dateFormatForMonth = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

//        AdUtils.loadBannerAd(binding!!.adView,this)
        AdUtils.loadGoogleBannerAd(this,binding!!.llAdView,Constant.BANNER_TYPE)

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility=View.GONE
            binding!!.llAdView.visibility=View.VISIBLE
        }

//        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
//            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
//            binding!!.llAdViewFacebook.visibility=View.GONE
//            binding!!.llAdView.visibility=View.VISIBLE
//        }else if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK) {
//            AdUtils.loadFacebookBannerAd(this,binding!!.llAdViewFacebook)
//            binding!!.llAdViewFacebook.visibility=View.VISIBLE
//            binding!!.llAdView.visibility=View.GONE
//        }else{
//            binding!!.llAdView.visibility=View.GONE
//            binding!!.llAdViewFacebook.visibility=View.GONE
//        }
//
//
//        if (Utils.isPurchased(this)) {
//            binding!!.llAdView.visibility=View.GONE
//            binding!!.llAdViewFacebook.visibility = View.GONE
//        }
        initIntentParam()
        init()
    }

    private fun initIntentParam() {
        try {


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding!!.topbar.isBackShow = true
        binding!!.topbar.toolbarTitle.visibility=View.VISIBLE
        binding!!.topbar.tvTitleText.text = getString(R.string.history)
        binding!!.topbar.toolbar.setBackgroundResource(R.color.green_seven_fit)
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()

        historyExpandableAdapter = HistoryExpandableAdapter(this,arrayListOf())
        binding!!.rvHistory.layoutManager = LinearLayoutManager(this)
        binding!!.rvHistory.setAdapter(historyExpandableAdapter)

        historyExpandableAdapter!!.setEventListener(object : HistoryExpandableAdapter.EventListener {
            override fun OnMenuClick(parentPosition: Int, childPosition: Int) {

                val childItem = historyExpandableAdapter!!.getMenuSubItem(parentPosition,childPosition)

                if (childItem!!.planDetail!!.planType.equals(Constant.PlanTypeMyTraining)) {
                    val i = Intent(this@HistoryActivity, MyTrainingExcListActivity::class.java)
                    i.putExtra("workoutPlanData", Gson().toJson(childItem!!.planDetail))
                    i.putExtra(Constant.extra_day_id, childItem!!.DayId)
                    i.putExtra("day_name", childItem!!.DayName)
                    startActivity(i)
                } else if (childItem.planDetail!!.hasSubPlan) {
                    val i = Intent(this@HistoryActivity, SubPlanActivity::class.java)
                    i.putExtra("workoutPlanData", Gson().toJson(childItem!!.planDetail))
                    i.putExtra(Constant.extra_day_id, childItem!!.DayId)
                    i.putExtra("day_name", childItem!!.DayName)
                    startActivity(i)
                } else if (childItem.planDetail!!.planDays.equals("YES")) {
                    val i = Intent(this@HistoryActivity, DaysPlanDetailActivity::class.java)
                    i.putExtra("workoutPlanData", Gson().toJson(childItem!!.planDetail))
                    i.putExtra(Constant.extra_day_id, childItem!!.DayId)
                    i.putExtra("day_name", childItem!!.DayName)
                    startActivity(i)
                } else {
                    val i = Intent(this@HistoryActivity, ExercisesListActivity::class.java)
                    i.putExtra("workoutPlanData", Gson().toJson(childItem!!.planDetail))
                    i.putExtra(Constant.extra_day_id, childItem!!.DayId)
                    i.putExtra("day_name", childItem!!.DayName)
                    i.putExtra(Constant.IS_PURCHASE,false)
                    startActivity(i)
                }

            }

        })

        val arrHistoryData = dbHelper.getWeekDayOfHistory()
        historyExpandableAdapter!!.addAll(arrHistoryData)
        historyExpandableAdapter!!.expandAllParents()

        compactCalendarSetup()
    }

    private fun compactCalendarSetup() {

        binding!!.compatCalenderView.removeAllEvents()
        binding!!.compatCalenderView.shouldScrollMonth(false)

        binding!!.tvMonthYear.text = dateFormatForMonth.format(Calendar.getInstance().time)

        val arrCompleteExerciseDt: ArrayList<String> = dbHelper.getCompleteExerciseDate()

        for (i in 0 until arrCompleteExerciseDt.size) {
            addEvents(Utils.parseTime(arrCompleteExerciseDt[i],Constant.DATE_TIME_24_FORMAT).time)
        }

        binding!!.compatCalenderView.setCurrentDate(Date())
        binding!!.compatCalenderView.setListener(object : fitnessapp.workout.homeworkout.compactcalender.CompactCalendarView.CompactCalendarViewListener {

            override fun onDayClick(dateClicked: Date?) {

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                binding!!.tvMonthYear.text = dateFormatForMonth.format(firstDayOfNewMonth!!)
            }
        })

    }

    /* Add Events */
    private fun addEvents(timeInMillis: Long) {
        val currentCalender = Calendar.getInstance(Locale.ENGLISH)
        currentCalender.time = Date()

        binding!!.compatCalenderView.addEvent(
            fitnessapp.workout.homeworkout.compactcalender.Event(
                Color.argb(255, 237, 55, 221),
                timeInMillis,
                "Event at " + Date(timeInMillis)
            )
        )

    }

    override fun onResume() {
//        openInternetDialog(this,false)
        super.onResume()
    }


    inner class ClickHandler {

        fun onNextClick()
        {
            binding!!.compatCalenderView.scrollRight()
        }

        fun onPrevClick()
        {
            binding!!.compatCalenderView.scrollLeft()
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

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
