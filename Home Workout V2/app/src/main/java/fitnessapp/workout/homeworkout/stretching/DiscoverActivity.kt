package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityDiscoverBinding
import fitnessapp.workout.homeworkout.stretching.adapter.DurationAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.PainReliefPagerAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.PostureCorrectionAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.TrainingGoalAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils

import java.util.*
import kotlin.collections.ArrayList


class DiscoverActivity : BaseActivity(), CallbackListener {

    var binding: ActivityDiscoverBinding? = null
    var painReliefPagerAdapter: PainReliefPagerAdapter? = null
    var flexibilityViewPagerAdapter: PainReliefPagerAdapter? = null
    var beginnerViewPagerAdapter: PainReliefPagerAdapter? = null
    var fatBurningViewPagerAdapter: PainReliefPagerAdapter? = null
    var trainingGoalAdapter: TrainingGoalAdapter? = null
    var bodyFocusAdapter: TrainingGoalAdapter? = null
    var durationAdapter: DurationAdapter? = null
    var postureCorrectionAdapter: PostureCorrectionAdapter? = null
    var onClickAd = 1

    var randomPlan: HomePlanTableClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_discover
        )

        initIntentParam()
        initDrawerMenu(true)
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


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding!!.topbar.isMenuShow = true
        binding!!.topbar.tvTitleText.text = getString(R.string.menu_discover)
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()

        painReliefPagerAdapter = PainReliefPagerAdapter(this, 2)
        binding!!.painReliefViewPager!!.offscreenPageLimit = painReliefPagerAdapter!!.count
        binding!!.painReliefViewPager!!.adapter = painReliefPagerAdapter
        binding!!.painReliefViewPager.setClipToPadding(false)
        binding!!.painReliefViewPager.measure(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        binding!!.painReliefViewPager.setPadding(0, 0, 110, 0)

        painReliefPagerAdapter!!.setEventListener(object : PainReliefPagerAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = painReliefPagerAdapter!!.getItem(position)

                    startNextScreenpainRelief(position)

            }

        })

        flexibilityViewPagerAdapter = PainReliefPagerAdapter(this, 2)
        binding!!.flexibilityViewPager.offscreenPageLimit = flexibilityViewPagerAdapter!!.count
        binding!!.flexibilityViewPager.adapter = flexibilityViewPagerAdapter
        binding!!.flexibilityViewPager.setClipToPadding(false)
        binding!!.flexibilityViewPager.measure(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        binding!!.flexibilityViewPager.setPadding(0, 0, 110, 0)

        flexibilityViewPagerAdapter!!.setEventListener(object :
            PainReliefPagerAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = flexibilityViewPagerAdapter!!.getItem(position)

                    startNextScreenflexibility(position)

            }

        })

        beginnerViewPagerAdapter = PainReliefPagerAdapter(this, 2)
        binding!!.forBeginnerViewPager.offscreenPageLimit = beginnerViewPagerAdapter!!.count
        binding!!.forBeginnerViewPager.adapter = beginnerViewPagerAdapter
        binding!!.forBeginnerViewPager.setClipToPadding(false)
        binding!!.forBeginnerViewPager.measure(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        binding!!.forBeginnerViewPager.setPadding(0, 0, 110, 0)

        beginnerViewPagerAdapter!!.setEventListener(object : PainReliefPagerAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {

                val item = beginnerViewPagerAdapter!!.getItem(position)


                    startNextScreembeginner(position)

            }

        })

        fatBurningViewPagerAdapter = PainReliefPagerAdapter(this, 2)
        binding!!.fatBurningViewPager!!.offscreenPageLimit = fatBurningViewPagerAdapter!!.count
        binding!!.fatBurningViewPager!!.adapter = fatBurningViewPagerAdapter
        binding!!.fatBurningViewPager.setClipToPadding(false)
        binding!!.fatBurningViewPager.measure(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        binding!!.fatBurningViewPager.setPadding(0, 0, 110, 0)

        fatBurningViewPagerAdapter!!.setEventListener(object :
            PainReliefPagerAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {

                val item = fatBurningViewPagerAdapter!!.getItem(position)

                    startNextScreenfatBurning(position)

            }

        })

        trainingGoalAdapter = TrainingGoalAdapter(this)
        binding!!.rvTrainingGoal.adapter = trainingGoalAdapter
        trainingGoalAdapter!!.setEventListener(object : TrainingGoalAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = trainingGoalAdapter!!.getItem(position)
                val i = Intent(this@DiscoverActivity, DiscoverDetailActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                if (item.isPro) {
                    i.putExtra(Constant.IS_PURCHASE, true)
                } else {
                    i.putExtra(Constant.IS_PURCHASE, false)
                }
                startActivity(i)
            }

        })

        postureCorrectionAdapter = PostureCorrectionAdapter(this)
        binding!!.rvPostureCorrection.adapter = postureCorrectionAdapter
        postureCorrectionAdapter!!.setEventListener(object :
            PostureCorrectionAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = postureCorrectionAdapter!!.getItem(position)
                val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                if (item.isPro) {
                    i.putExtra(Constant.IS_PURCHASE, true)
                } else {
                    i.putExtra(Constant.IS_PURCHASE, false)
                }
                startActivity(i)
            }
        })

        bodyFocusAdapter = TrainingGoalAdapter(this)
        binding!!.rvBodyFocus.adapter = bodyFocusAdapter
        bodyFocusAdapter!!.setEventListener(object : TrainingGoalAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = bodyFocusAdapter!!.getItem(position)
                val i = Intent(this@DiscoverActivity, DiscoverDetailActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                if (item.isPro) {
                    i.putExtra(Constant.IS_PURCHASE, true)
                } else {
                    i.putExtra(Constant.IS_PURCHASE, false)
                }
                startActivity(i)
            }
        })

        durationAdapter = DurationAdapter(this)
        binding!!.rvDuration.adapter = durationAdapter
        durationAdapter!!.setEventListener(object : DurationAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = durationAdapter!!.getItem(position)
                val i = Intent(this@DiscoverActivity, DiscoverDetailActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                if (item.isPro) {
                    i.putExtra(Constant.IS_PURCHASE, true)
                } else {
                    i.putExtra(Constant.IS_PURCHASE, false)
                }
                startActivity(i)
            }
        })

        fillData()

    }

    private fun startNextScreenfatBurning(position: Int) {
        val item = fatBurningViewPagerAdapter!!.getItem(position)
        val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
        i.putExtra("workoutPlanData", Gson().toJson(item))
        if (item.isPro) {
            i.putExtra(Constant.IS_PURCHASE, true)
        } else {
            i.putExtra(Constant.IS_PURCHASE, false)
        }
        startActivity(i)
    }

    private fun startNextScreembeginner(position: Int) {
        val item = beginnerViewPagerAdapter!!.getItem(position)
        val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
        i.putExtra("workoutPlanData", Gson().toJson(item))
        if (item.isPro) {
            i.putExtra(Constant.IS_PURCHASE, true)
        } else {
            i.putExtra(Constant.IS_PURCHASE, false)
        }
        startActivity(i)
    }

    private fun startNextScreenflexibility(position: Int) {
        val item = flexibilityViewPagerAdapter!!.getItem(position)
        val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
        i.putExtra("workoutPlanData", Gson().toJson(item))
        if (item.isPro) {
            i.putExtra(Constant.IS_PURCHASE, true)
        } else {
            i.putExtra(Constant.IS_PURCHASE, false)
        }
        startActivity(i)
    }

    private fun startNextScreenpainRelief(position: Int) {
        val item = painReliefPagerAdapter!!.getItem(position)
        val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
        i.putExtra("workoutPlanData", Gson().toJson(item))
        if (item.isPro) {
            i.putExtra(Constant.IS_PURCHASE, true)
        } else {
            i.putExtra(Constant.IS_PURCHASE, false)
        }
        startActivity(i)
    }

    private fun fillData() {
        try {
            getPainReliefData()
            getTrainingData()
            getFlexibilityData()
            getForBeginnerData()
            getPostureCurractionData()
            getFatBurningData()
            getBodyFocusData()
            getDurationData()

            val lastDate = Utils.getPref(this, Constant.PREF_RANDOM_DISCOVER_PLAN_DATE, "")
            val currDate = Utils.parseTime(Date(), "dd-MM-yyyy")
            val currDateStr = Utils.parseTime(currDate.time, "dd-MM-yyyy")
            val str = Utils.getPref(this, Constant.PREF_RANDOM_DISCOVER_PLAN, "")
            if (lastDate.isNullOrEmpty()
                    .not() && currDateStr.equals(lastDate) && str.isNullOrEmpty().not()
            ) {
                randomPlan =
                    Gson().fromJson(str, object : TypeToken<HomePlanTableClass>() {}.type)!!

            } else {
                randomPlan = dbHelper.getRandomDiscoverPlan()
                Utils.setPref(this, Constant.PREF_RANDOM_DISCOVER_PLAN_DATE, currDateStr)
                Utils.setPref(this, Constant.PREF_RANDOM_DISCOVER_PLAN, Gson().toJson(randomPlan))
            }
            binding!!.imgCover.setImageResource(Utils.getDrawableId(randomPlan!!.planImage, this))
            binding!!.tvTitle.text = randomPlan!!.planName
            if (randomPlan!!.shortDes.isNullOrEmpty().not())
                binding!!.tvDesc.text = randomPlan!!.shortDes
            else {
                binding!!.tvDesc.text = randomPlan!!.introduction
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBodyFocusData() {
        bodyFocusAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_BodyFocus) as ArrayList<HomePlanTableClass>
        )
    }

    private fun getDurationData() {
        durationAdapter!!.addAll(dbHelper.getDiscoverPlanList(Constant.Discover_Duration) as ArrayList<HomePlanTableClass>)
    }

    private fun getPainReliefData() {
        painReliefPagerAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_Pain_Relief)
                .reversed() as ArrayList<HomePlanTableClass>
        )
    }

    fun getTrainingData() {
        trainingGoalAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_Training) as ArrayList<HomePlanTableClass>
        )
    }

    fun getFlexibilityData() {
        flexibilityViewPagerAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_Flexibility) as ArrayList<HomePlanTableClass>
        )
    }

    fun getForBeginnerData() {
        beginnerViewPagerAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_ForBeginner) as ArrayList<HomePlanTableClass>
        )
    }

    fun getPostureCurractionData() {
        postureCorrectionAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_PostureCorrection) as ArrayList<HomePlanTableClass>
        )
    }

    fun getFatBurningData() {
        fatBurningViewPagerAdapter!!.addAll(
            dbHelper.getDiscoverPlanList(Constant.Discover_FatBurning) as ArrayList<HomePlanTableClass>
        )
    }

    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
        changeSelection(1)
    }


    inner class ClickHandler {

        fun onMyTrainingClick() {
            val i = Intent(this@DiscoverActivity, MyTrainingActivity::class.java)
            startActivity(i)
            finish()
        }

        fun onTopPlanClick() {

            val i = Intent(this@DiscoverActivity, ExercisesListActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(randomPlan))
            startActivity(i)
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
