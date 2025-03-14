package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityDaysPlanDetailBinding
import fitnessapp.workout.homeworkout.stretching.adapter.MonthlyDaysAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.WeekPlanAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.objects.PWeekDayData
import fitnessapp.workout.homeworkout.stretching.objects.PWeeklyDayData
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class DaysPlanDetailActivity : BaseActivity(), CallbackListener {

    var binding: ActivityDaysPlanDetailBinding? = null
    var weekPlanAdapter: WeekPlanAdapter? = null
    var monthPlanAdapter: MonthlyDaysAdapter? = null
    var workoutPlanData: HomePlanTableClass? = null
    lateinit var weeklyDayStatusList: ArrayList<PWeeklyDayData>
    var arrWeekDayData = ArrayList<PWeekDayData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_days_plan_detail)

//        AdUtils.loadBannerAd(binding!!.adView,this)
//        AdUtils.loadBannerGoogleAd(this,binding!!.llAdView,Constant.BANNER_TYPE)
        AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility = View.GONE
            binding!!.llAdView.visibility = View.VISIBLE
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
//        if (Utils.isPurchased(this)) {
//            binding!!.llAdView.visibility=View.GONE
//            binding!!.llAdViewFacebook.visibility = View.GONE
//        }

        initIntentParam()
        init()
    }

    private fun initIntentParam() {
        try {
            if (intent.extras != null) {
                if (intent.extras!!.containsKey("workoutPlanData")) {
                    val str = intent.getStringExtra("workoutPlanData")
                    workoutPlanData = Gson().fromJson(str, object :
                        TypeToken<HomePlanTableClass>() {}.type)!!
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun init() {
        binding!!.handler = ClickHandler()

        weekPlanAdapter = WeekPlanAdapter(this, workoutPlanData!!)

        binding!!.rvWeeks.layoutManager = LinearLayoutManager(this)
        binding!!.rvWeeks.adapter = weekPlanAdapter

        weekPlanAdapter!!.setEventListener(object : WeekPlanAdapter.EventListener {
            override fun onItemClick(
                position: Int,
                child: Int,
                view: View
            ) {
                startWorkOut(position, child)
            }

        })

        fillData()
    }


    private fun startWorkOut(position: Int, child: Int) {
        val item = weekPlanAdapter!!.getItem(position).arrWeekDayData.get(child)

        workoutPlanData!!.planMinutes = item.Minutes
        workoutPlanData!!.planWorkouts = item.Workouts
        val intent = Intent(this@DaysPlanDetailActivity, ExercisesListActivity::class.java)
        intent.putExtra("workoutPlanData", Gson().toJson(workoutPlanData))
        intent.putExtra(Constant.extra_day_id, item.Day_id)
        intent.putExtra("day_name", item.Day_id)
        intent.putExtra("Week_name", weekPlanAdapter!!.getItem(position).Week_name)
        intent.putExtra(Constant.IS_PURCHASE, true)
        startActivity(intent)
    }


    private fun fillData() {
        if (workoutPlanData != null) {
            binding!!.tvTitleText.text = workoutPlanData!!.planName
            binding!!.tvIntroductionDes.text = workoutPlanData!!.introduction





            weeklyDayStatusList = dbHelper.getWorkoutWeeklyData(workoutPlanData!!.planName!!)

            weekPlanAdapter!!.addAll(weeklyDayStatusList)


//            for (item in weeklyDayStatusList){
//                Log.d("checkWeekDaysData",item.Day_name)
//            }

            Utils.setDayProgressData(
                this,
                workoutPlanData!!.planId!!,
                binding!!.tvDayLeft,
                binding!!.tvPer,
                binding!!.pbDay
            )

        }
    }

    override fun onResume() {
//        openInternetDialog(this,false)
        super.onResume()
        fillData()
    }


    inner class ClickHandler {

        fun onStartClick() {
            try {
                if (weekPlanAdapter!!.continueDataChildPos != null && weekPlanAdapter!!.continueDataParentPos != null) {
                    startWorkOut(
                        weekPlanAdapter!!.continueDataParentPos!!,
                        weekPlanAdapter!!.continueDataChildPos!!
                    )
                } else {
                    startWorkOut(0, 0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun onIntroductionClick() {
            if (binding!!.llIntroductionDes.visibility == View.VISIBLE) {
                binding!!.llIntroductionDes.visibility = View.GONE
            } else {
                binding!!.llIntroductionDes.visibility = View.VISIBLE
            }
        }

        fun onBackClick() {
            finish()
        }
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }


}
