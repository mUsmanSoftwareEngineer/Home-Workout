package fitnessapp.workout.homeworkout.stretching

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityRestBinding
import fitnessapp.workout.homeworkout.stretching.adapter.WorkoutProgressIndicatorAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.objects.HomeExTableClass
import fitnessapp.workout.homeworkout.stretching.utils.*
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Debug
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import java.util.*


class RestActivity : BaseActivity(), CallbackListener {

    var binding: ActivityRestBinding? = null
    var timer: fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause? = null
    var nextExercise: HomeExTableClass? = null
    var nextPos = 0
    var totalEx = 0

    var timeCountDown = 0L
    var restTime = 10L // in second
    var workoutProgressIndicatorAdapter: WorkoutProgressIndicatorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rest)

//        AdUtils.loadBannerAd(binding!!.adView,this)
//        AdUtils.loadBannerGoogleAd(this,binding!!.llAdView,Constant.BANNER_TYPE)
       /* if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.REC_BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility= View.GONE
        }else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            AdUtils.loadFacebookMediumRectangleAd(this,binding!!.llAdViewFacebook)
        }else{
            binding!!.llAdViewFacebook.visibility= View.GONE
        }*/


//        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
//            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.REC_BANNER_TYPE)
//            binding!!.llAdViewFacebook.visibility=View.GONE
//            binding!!.llAdView.visibility=View.VISIBLE
//        }else if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK) {
//            AdUtils.loadFacebookMediumRectangleAd(this,binding!!.llAdViewFacebook)
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
                if (intent.extras!!.containsKey("nextEx")) {
                    val str = intent.getStringExtra("nextEx")
                    nextExercise = Gson().fromJson(str, object :
                        TypeToken<HomeExTableClass>() {}.type)!!
                }
                if (intent.extras!!.containsKey("nextPos")) {
                    nextPos = intent.getIntExtra("nextPos", 2)
                }
                if (intent.extras!!.containsKey("totalEx")) {
                    totalEx = intent.getIntExtra("totalEx", 0)
                }
//
//                Log.d("chekNextPos",nextPos.toString())
//                Log.d("checkUri",Utils.videoURIList.get(12).toString()+"postUri")

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding!!.handler = ClickHandler()

        restTime = Utils.getPref(this,Constant.PREF_REST_TIME,Constant.DEFAULT_REST_TIME)

        workoutProgressIndicatorAdapter = WorkoutProgressIndicatorAdapter(this)
        binding!!.rcyWorkoutStatus.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding!!.rcyWorkoutStatus.setAdapter(workoutProgressIndicatorAdapter)
        workoutProgressIndicatorAdapter!!.setTotalExercise(totalEx)

        binding!!.tvName.text = nextExercise!!.exName
        binding!!.tvTotalEx.text = "$nextPos / $totalEx"
        if (nextExercise!!.exUnit.equals(Constant.workout_type_step)) {
            binding!!.tvTime.text = "X ${nextExercise!!.exTime}"
        } else {
            binding!!.tvTime.text = "${nextExercise!!.exTime} s"
//                Utils.secToString(nextExercise!!.exTime!!.toInt(), Constant.WORKOUT_TIME_FORMAT)
        }


        binding!!.restVideoview.setVideoURI(Utils.videoURIList.get(nextPos-1))
        binding!!.restVideoview.start()
        binding!!.restVideoview.setOnPreparedListener {
            it.isLooping=true
        }

        binding!!.progressBarWorkOutProcess.max = totalEx
        binding!!.progressBarWorkOutProcess.progress = nextPos-1

        binding!!.viewFlipper.removeAllViews()
        val listImg: ArrayList<String>? =
            Utils.ReplaceSpacialCharacters(nextExercise!!.exPath!!)
                ?.let { Utils.getAssetItems(this, it) }

        if (listImg != null) {
            for (i in 0 until listImg.size) {
                val imgview = ImageView(this)
                //            Glide.with(mContext).load("//android_asset/burpee/".plus(i.toString()).plus(".png")).into(imgview)
                Glide.with(this).load(listImg.get(i)).into(imgview)
                imgview.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                binding!!.viewFlipper.addView(imgview)
            }
        }

        binding!!.viewFlipper.isAutoStart = true
        binding!!.viewFlipper.setFlipInterval(resources.getInteger(R.integer.viewfliper_animation))
        binding!!.viewFlipper.startFlipping()

        if (!nextExercise!!.exUnit.equals(Constant.workout_type_step)) {
            MyApplication.speechText(
                this,
                "Take a rest"
            )
            Handler().postDelayed(Runnable {
                MyApplication.speechText(
                    this,
                    "Next ${nextExercise!!.exTime} seconds ${nextExercise!!.exName}"
                )
            }, 1000)

        } else {
            MyApplication.speechText(
                this,
                "Take a rest"
            )
            Handler().postDelayed(Runnable {
                MyApplication.speechText(
                    this,
                    "Next ${nextExercise!!.exTime} times ${nextExercise!!.exName}"
                )
            }, 1000)

        }
        countDownRest()

    }

    private fun countDownRest() {
        binding!!.tvSecond.text = Utils.secToString(restTime.toInt(), "mm:ss")
        binding!!.progressBarReadyToGo.max = restTime.toInt()-1


        timer = object : fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause(restTime * 1000L, 1000, true) {
            override fun onFinish() {
                val i = Intent()
                i.putExtra("restTime",timeCountDown)
                setResult(Activity.RESULT_OK,i)
                binding!!.progressBarReadyToGo.progress = restTime.toInt()
                timer!!.cancel()
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeCountDown++
                restTime = (millisUntilFinished/1000)
                binding!!.tvSecond.text = Utils.secToString(((millisUntilFinished / 1000)).toInt(), "mm:ss")
                binding!!.progressBarReadyToGo.progress = restTime.toInt()

                Log.e("TAG", "onTick:::::time "+Utils.secToString(((millisUntilFinished / 1000)).toInt(), "mm:ss")+"    "+
                        millisUntilFinished+"    "+millisUntilFinished/1000)
                if (((millisUntilFinished / 1000)) < 4) {
                    //Debug.e("Rest time = " ,""+((millisUntilFinished/1000) - timeCountDown))
                    MyApplication.speechText(
                        this@RestActivity,
                        ((millisUntilFinished / 1000)).toString()
                    )
                }
            }

        }

        Handler().postDelayed(Runnable {
            timer!!.start()
        }, 500)

    }


    inner class ClickHandler {

        fun onSkipClick() {
            val i = Intent()
            i.putExtra("restTime",timeCountDown)
            i.putExtra("isRestSkip",true)
            setResult(Activity.RESULT_OK,i)
            finish()
        }

        fun onPlusTimeClick() {
            Debug.e("Timer:::::old seconds = ", restTime.toString()+"  "+(restTime - timeCountDown) +"  "+timeCountDown.toString())
//            restTime = (restTime - timeCountDown) + 20
            restTime += 20
            Debug.e("Timer:::::New seconds = ", restTime.toString() +"  "+(restTime - timeCountDown)+"  "+timeCountDown.toString())
            timer?.cancel()
            countDownRest()
        }

        fun onExerciseInfoClick() {
            pauseTimer()
            val i = Intent(this@RestActivity, ExerciseVideoActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(nextExercise))
            startActivity(i)
        }
    }

    override fun onResume() {
//        openInternetDialog(this,false)
        super.onResume()
        resumeTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

    fun resumeTimer() {
        if (timer != null && timer!!.isPaused) {
            timer!!.resume()
        }
    }

    fun pauseTimer() {
        if (timer != null && timer!!.isRunning) {
            timer!!.pause()
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        i.putExtra("restTime",timeCountDown)
        i.putExtra("isRestSkip",true)
        setResult(Activity.RESULT_OK,i)
        super.onBackPressed()

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }
}
