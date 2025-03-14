package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivitySubPlanBinding
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class SubPlanActivity : BaseActivity(), CallbackListener {

    var binding: ActivitySubPlanBinding? = null
    var workoutPlanData: HomePlanTableClass? = null
    var subPlans:ArrayList<HomePlanTableClass>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_plan)

        initIntentParam()
        init()

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility=View.GONE
            binding!!.llAdView.visibility=View.VISIBLE
        }else if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK) {
            AdUtils.loadFacebookBannerAd(this,binding!!.llAdViewFacebook)
            binding!!.llAdViewFacebook.visibility=View.VISIBLE
            binding!!.llAdView.visibility=View.GONE
        }else{
            binding!!.llAdView.visibility=View.GONE
            binding!!.llAdViewFacebook.visibility=View.GONE
        }


        if (Utils.isPurchased(this)) {
            binding!!.llAdView.visibility=View.GONE
            binding!!.llAdViewFacebook.visibility = View.GONE
        }

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

        fillData()

    }


    private fun fillData() {
        if (workoutPlanData != null) {
            binding!!.tvTitle.text = workoutPlanData!!.planName
            binding!!.tvIntroductionDes.text = workoutPlanData!!.introduction
            binding!!.imgCover.setImageResource(
                Utils.getDrawableId(
                    workoutPlanData!!.planImage,
                    this
                )
            )

            subPlans = dbHelper.getHomeSubPlanList(workoutPlanData!!.planId!!)

            for (item in subPlans!!)
            {
                if (item.planLvl!!.equals(Constant.PlanLvlBeginner)) {
                    binding!!.tvBeginnerTime.text = item.planMinutes + " mins"
                } else if (item.planLvl!!.equals(Constant.PlanLvlIntermediate)) {
                    binding!!.tvInterMediateTime.text = item.planMinutes + " mins"
                } else if (item.planLvl!!.equals(Constant.PlanLvlAdvanced)) {
                    binding!!.tvAdvancedTime.text = item.planMinutes + " mins"
                }

            }
        }
    }

    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
    }


    inner class ClickHandler {

        fun onBackClick() {
            finish()
        }

        fun onBeginnerClick() {
            val i = Intent(this@SubPlanActivity,ExercisesListActivity::class.java)

            for (item in subPlans!!)
            {
                if (item.planLvl!!.equals(Constant.PlanLvlBeginner)) {
                    i.putExtra("workoutPlanData", Gson().toJson(item))
                    i.putExtra(Constant.IS_PURCHASE,true)
                }
            }

            startActivity(i)
        }

        fun onIntermediateClick() {
            val i = Intent(this@SubPlanActivity,ExercisesListActivity::class.java)

            for (item in subPlans!!)
            {
                if (item.planLvl!!.equals(Constant.PlanLvlIntermediate)) {
                    i.putExtra("workoutPlanData", Gson().toJson(item))
                    i.putExtra(Constant.IS_PURCHASE,true)
                }
            }

            startActivity(i)
        }

        fun onAdvanceClick() {
            val i = Intent(this@SubPlanActivity,ExercisesListActivity::class.java)
            for (item in subPlans!!)
            {
                if (item.planLvl!!.equals(Constant.PlanLvlAdvanced)) {
                    i.putExtra("workoutPlanData", Gson().toJson(item))
                    i.putExtra(Constant.IS_PURCHASE,true)
                }
            }
            startActivity(i)
        }
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }


}
