package fitnessapp.workout.homeworkout.stretching

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityHomeBinding
import fitnessapp.workout.homeworkout.stretching.adapter.HomePlansAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.HomeWeekGoalAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.objects.HistoryDetailsClass
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Debug
import fitnessapp.workout.homeworkout.stretching.utils.ExitStrategy
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import fitnessapp.workout.homeworkout.stretching.viewmodel.AdsIdData
import kotlin.math.roundToInt


class HomeActivity : BaseActivity(), CallbackListener {

    var binding: ActivityHomeBinding? = null
    var homeWeekGoalAdapter: HomeWeekGoalAdapter? = null
    var homePlansAdapter: HomePlansAdapter? = null
    var recentPlan: HomePlanTableClass? = null
    var lastWorkout: HistoryDetailsClass? = null
    var onClickAd = 1
    lateinit var adsIdData: AdsIdData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//        RequestConfiguration.Builder().setTestDeviceIds(listOf("9CC18F110F993FBFD4CDC98B82191F4A"))
        Log.e("TAG", "MainActivity:::::::::onCreate::::Main Activity:::  ")
        initTopBar(binding!!.topbar)
        initDrawerMenu(true)

        init()
        adsIdData = AdsIdData()
        callGetAdsId()

//       subScribeToFirebaseTopic()

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
//        Log.e("TAG", "onCreate:Is Purchase::::::: "+Utils.isPurchased(this) )
//        if (Utils.isPurchased(this)) {
//            binding!!.llAdView.visibility=View.GONE
//            binding!!.llAdViewFacebook.visibility = View.GONE
//        }

    }

    private fun subScribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("home_workout")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("subScribeFirebaseTopic", ": Fail")
                } else {
                    Log.e("subScribeFirebaseTopic", ": Success")
                }
            }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                Log.d( "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = baseContext.getString(R.string.msg_token_fmt, token)
//            Log.d("logToast", token)
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }


    fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }


    fun callGetAdsId() {
        try {
            if (isNetworkConnected()) {
                if (Constant.ENABLE_DISABLE == Constant.ENABLE) {
                    Utils.setPref(this, Constant.AD_TYPE_FB_GOOGLE, Constant.AD_TYPE_FACEBOOK_GOOGLE)
                    Utils.setPref(this, Constant.FB_BANNER, Constant.FB_BANNER_ID)
                    Utils.setPref(this, Constant.FB_INTERSTITIAL, Constant.FB_INTERSTITIAL_ID)
                    Utils.setPref(this, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_ID)
                    Utils.setPref(this, Constant.GOOGLE_INTERSTITIAL, Constant.GOOGLE_INTERSTITIAL_ID)
                    Utils.setPref(this, Constant.STATUS_ENABLE_DISABLE, Constant.ENABLE_DISABLE)
                    setAdAppId(Constant.GOOGLE_ADMOB_APP_ID)
                } else {
                    Utils.setPref(this, Constant.STATUS_ENABLE_DISABLE, Constant.ENABLE_DISABLE)
                }
            } else {
//                openInternetDialog(this, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun setAdAppId(id: String) {
        try {
            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            val myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
            Debug.e("SplashActivity ==>", "Before Api Key::::: $myApiKey")
            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", id)
            val apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
            Debug.e("SplashActivity ==>", "After Api Key:::::: $apiKey")
        } catch (e: PackageManager.NameNotFoundException) {
            Debug.e("SplashActivity ==>", "Failed to load meta-data, NameNotFound: " + e.message)
        } catch (e: NullPointerException) {
            Debug.e("SplashActivity ==>", "Failed to load meta-data, NullPointer: " + e.message)
        }
    }


    private fun init() {
        binding!!.topbar.isMenuShow = true
        binding!!.topbar.isDiscoverShow = true
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()

//        setupWeekTopData()

        homePlansAdapter = HomePlansAdapter(this)
        binding!!.rvPlans.layoutManager = LinearLayoutManager(this)
        binding!!.rvPlans.adapter = homePlansAdapter
        homePlansAdapter!!.setEventListener(object : HomePlansAdapter.EventListener {

            override fun onItemClick(position: Int, view: View) {
                val item = homePlansAdapter!!.getItem(position)
                startNextScreenMove(position)
            }
        })

        homePlansAdapter!!.addAll(dbHelper.getHomePlanList(Constant.PlanTypeWorkoutNew))

        binding!!.viewStatistics.setOnClickListener {
            val intent = Intent(getActivity(), ReportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }


    fun startNextScreenMove(position: Int) {
        val item = homePlansAdapter!!.getItem(position)
        if (item.hasSubPlan) {
            val i = Intent(this@HomeActivity, SubPlanActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(item))
            startActivity(i)
        } else if (item.planDays.equals("YES")) {
            val i = Intent(this@HomeActivity, DaysPlanDetailActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(item))
            startActivity(i)
        } else {
            val i = Intent(this@HomeActivity, ExercisesListActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(item))
            if (item.isPro) {
                i.putExtra(Constant.IS_PURCHASE, true)
            } else {
                i.putExtra(Constant.IS_PURCHASE, false)
            }
            startActivity(i)
        }
    }

    private fun setupWeekTopData() {

        try {
            if (Utils.getPref(this, Constant.PREF_IS_WEEK_GOAL_DAYS_SET, false)) {

                binding!!.llWeekGoal.visibility = View.VISIBLE
                binding!!.llSetGoal.visibility = View.GONE

                val arrCurrentWeek = Utils.getCurrentWeek()
                var completedWeekDay = 0
                for (pos in 0 until arrCurrentWeek.size) {
                    if (dbHelper.isHistoryAvailable(
                            Utils.parseTime(
                                arrCurrentWeek[pos],
                                Constant.DATE_TIME_24_FORMAT,
                                Constant.DATE_FORMAT
                            )
                        )
                    ) {
                        completedWeekDay++
                    }
                }

                val weekDayGoal = Utils.getPref(this, Constant.PREF_WEEK_GOAL_DAYS, 7)

                /* binding!!.tvCompletedGoalCount.text = HtmlCompat.fromHtml(
                "<font color='${ContextCompat.getColor(this, R.color.green_text)}'>$completedWeekDay</font>/$weekDayGoal",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )*/

                binding!!.tvCompletedGoalCount.text = completedWeekDay.toString()
                binding!!.tvTotalGoalCount.text = "/$weekDayGoal"

                homeWeekGoalAdapter = HomeWeekGoalAdapter(this)
                binding!!.rvWeekGoal.adapter = homeWeekGoalAdapter

                homeWeekGoalAdapter!!.setEventListener(object : HomeWeekGoalAdapter.EventListener {
                    override fun onItemClick(position: Int, view: View) {
                        val i = Intent(this@HomeActivity, HistoryActivity::class.java)
                        startActivity(i)
                    }
                })

            } else {
                binding!!.llWeekGoal.visibility = View.GONE
                binding!!.llSetGoal.visibility = View.VISIBLE
            }

            if (dbHelper.getHistoryList().isNullOrEmpty()) {
                binding!!.llRecent.visibility = View.GONE
                binding!!.llReport.visibility = View.GONE
            } else {
                binding!!.llRecent.visibility = View.VISIBLE
                binding!!.llReport.visibility = View.VISIBLE

                binding!!.tvWorkOuts.text = dbHelper.getHistoryTotalWorkout().toString()
                binding!!.tvCalorie.text = dbHelper.getHistoryTotalKCal().toInt().toString()
                binding!!.tvMinutes.text =
                    ((dbHelper.getHistoryTotalMinutes() / 60).toDouble()).roundToInt().toString()

                lastWorkout = dbHelper.getRecentHistory()
                if (lastWorkout != null) {
                    recentPlan = dbHelper.getPlanByPlanId(lastWorkout!!.PlanId.toInt())

                    if (recentPlan!!.planDays == Constant.PlanDaysYes) {
                        binding!!.tvRecentWorkOutName.text = recentPlan!!.planName
                        val item = dbHelper.getDaysPlanData(lastWorkout!!.DayId)
                        recentPlan!!.planMinutes = item!!.Minutes
                        recentPlan!!.planWorkouts = item!!.Workouts
                    } else {
                        binding!!.tvRecentWorkOutName.text = lastWorkout!!.PlanName
                    }

                    binding!!.tvRecentTime.text = Utils.parseTime(
                        lastWorkout!!.DateTime,
                        Constant.DATE_TIME_24_FORMAT,
                        "HH:mm"
                    )
                    binding!!.imgRecentWorkout.setImageResource(
                        Utils.getDrawableId(
                            recentPlan!!.planThumbnail,
                            this
                        )
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
//        openInternetDialog(this,false)
        super.onResume()
        changeSelection(0)
//        setupWeekTopData()
        homePlansAdapter?.notifyDataSetChanged()
        Log.e("TAG", "MainActivity:::::::::onResume:::Main Activity:::::: ")
    }


    inner class TopClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getActivity(), view!!)

            if (value.equals(getString(R.string.menu_reminder))) {
//                val i = Intent(this@HomeActivity, DiscoverActivity::class.java)
//                startActivity(i)
                val intent = Intent(getActivity(), ReminderActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
//                finish()
            }

            if(value.equals(getString(R.string.menu_setting))){
                val intent = Intent(getActivity(), SettingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }

        }
    }

    inner class ClickHandler {

        fun onSetGoalClick() {
            val i = Intent(this@HomeActivity, SetYourWeeklyGoalActivity::class.java)
            startActivityForResult(i, 8019)
        }

        fun onEditWeekGoalClick() {
            val i = Intent(this@HomeActivity, SetYourWeeklyGoalActivity::class.java)
            startActivity(i)
        }

        fun onBackToTopClick() {
            binding!!.nestedScrollView.isSmoothScrollingEnabled = true
            binding!!.nestedScrollView.fullScroll(View.FOCUS_UP)
        }

        fun onMyTrainingClick() {
            val i = Intent(this@HomeActivity, MyTrainingActivity::class.java)
            startActivity(i)
        }

        fun onRecentViewAllClick() {
            val i = Intent(this@HomeActivity, RecentActivity::class.java)
            startActivity(i)
        }

        fun onRecentViewClick() {

            if (recentPlan!!.planType.equals(Constant.PlanTypeMyTraining)) {
                val i = Intent(this@HomeActivity, MyTrainingExcListActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(recentPlan))
                startActivity(i)
            } else if (recentPlan!!.hasSubPlan) {
                val i = Intent(this@HomeActivity, SubPlanActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(recentPlan))
                startActivity(i)
            } else if (recentPlan!!.planDays.equals("YES")) {
                val i = Intent(this@HomeActivity, DaysPlanDetailActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(recentPlan))
                startActivity(i)
            } else {
                val i = Intent(this@HomeActivity, ExercisesListActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(recentPlan))
                i.putExtra(Constant.IS_PURCHASE, false)
                startActivity(i)
            }

            /*val i = Intent(this@HomeActivity, ExercisesListActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(recentPlan))
            i.putExtra(Constant.extra_day_id, lastWorkout!!.DayId)
            i.putExtra("day_name", lastWorkout!!.DayName)
            startActivity(i)*/
        }
    }

    override fun onBackPressed() {
        try {
            if (result != null && result!!.isDrawerOpen) {
                hideMenu(true)
            } else {
                if (ExitStrategy.canExit()) {
                    super.onBackPressed()
                } else {
                    ExitStrategy.startExitDelay(2000)
                    showToast(getString(R.string.exit_msg))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 8019 && resultCode == Activity.RESULT_OK) {
            setupWeekTopData()
        }

    }*/

}
