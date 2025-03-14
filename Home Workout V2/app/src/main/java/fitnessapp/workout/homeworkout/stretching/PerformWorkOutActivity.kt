package fitnessapp.workout.homeworkout.stretching

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityPerformWorkoutBinding
import fitnessapp.workout.homeworkout.databinding.BottomSheetSoundOptionBinding
import fitnessapp.workout.homeworkout.databinding.DialogQuiteWorkoutBinding
import fitnessapp.workout.homeworkout.stretching.adapter.WorkoutProgressIndicatorAdapter
import fitnessapp.workout.homeworkout.stretching.db.DataHelper
import fitnessapp.workout.homeworkout.stretching.interfaces.AdsCallback
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.DialogDismissListener
import fitnessapp.workout.homeworkout.stretching.objects.HomeExTableClass
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.objects.Music
import fitnessapp.workout.homeworkout.stretching.utils.*
import java.util.*


class PerformWorkOutActivity : BaseActivity(), CallbackListener {

    val TAG = PerformWorkOutActivity::class.java.name + Constant.ARROW
    var binding: ActivityPerformWorkoutBinding? = null
    var workoutProgressIndicatorAdapter: WorkoutProgressIndicatorAdapter? = null
    var workoutPlanData: HomePlanTableClass? = null
    var exercisesList: ArrayList<HomeExTableClass>? = null
    private lateinit var mySoundUtil: MySoundUtil

    var currentPos = 0
    var currentExe: HomeExTableClass? = null
    var totalExTime = 0L

    private var exStartTime: Long = 0
    private var running = false
    private var currentTime: Long = 0
    private var timeCountDown = 0
    var currMusic: Music? = null
    var isPaused = false

    var timer: fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_perform_workout)

//        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
//            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
////            binding!!.llAdViewFacebook.visibility = View.GONE
//            binding!!.llAdView.visibility = View.VISIBLE
//        }

        initIntentParam()
        init()
        initReadyToGo()

    }

    private fun initIntentParam() {
        try {
            if (intent.extras != null) {
                if (intent.extras!!.containsKey("workoutPlanData")) {
                    val str = intent.getStringExtra("workoutPlanData")
                    workoutPlanData = Gson().fromJson(str, object :
                        TypeToken<HomePlanTableClass>() {}.type)!!
                }

                if (intent.extras!!.containsKey("ExcList")) {
                    val str = intent.getStringExtra("ExcList")
                    exercisesList = Gson().fromJson(
                        str,
                        object : TypeToken<ArrayList<HomeExTableClass>>() {}.type
                    )!!
                    binding!!.progressBarTop.max = exercisesList!!.size
                }

                if (intent.extras!!.containsKey("currentPos")) {
                    currentPos = intent.getIntExtra("ExcList", 0)
                    if (exercisesList.isNullOrEmpty().not()) {
                        currentExe = exercisesList!!.get(currentPos)
                    }
                } else {
                    if (exercisesList.isNullOrEmpty().not()) {
                        currentExe = exercisesList!!.get(0)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding!!.handler = ClickHandler()

        mySoundUtil = MySoundUtil(this)

        workoutProgressIndicatorAdapter = WorkoutProgressIndicatorAdapter(this)
        binding!!.rcyWorkoutStatus.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding!!.rcyWorkoutStatus.adapter = workoutProgressIndicatorAdapter
        workoutProgressIndicatorAdapter!!.setTotalExercise(exercisesList!!.size)

        initMusic(true)

        if (Utils.getPref(this, Constant.PREF_IS_SOUND_MUTE, false)) {
//            binding!!.imgSound.setImageResource(R.drawable.ic_mute_sound)
        } else {
//            binding!!.imgSound.setImageResource(R.drawable.ic_sound_round)
        }

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.GooglebeforloadAd(this)
        } else if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK) {
            AdUtils.FacebookbeforeloadFullAd(this)
        }


    }

    private fun initMusic(isPlayMusic: Boolean) {

        if (Utils.getPref(this, Constant.PREF_IS_MUSIC_SELECTED, false)) {
            val str = Utils.getPref(this, Constant.PREF_MUSIC, "")
            if (str.isNullOrEmpty().not()) {
                currMusic = Gson().fromJson<Music>(str, object : TypeToken<Music>() {}.type)
                if (Utils.getPref(this@PerformWorkOutActivity, Constant.PREF_IS_MUSIC_MUTE, false)
                        .not()
                ) {
                    if (isPlayMusic)
                        playMusic()
                }
            }
        }

        setPlayPauseView()
    }


    private fun loadWorkoutImage() {

        binding!!.viewFlipper.removeAllViews()
        val listImg: ArrayList<String>? =
            Utils.ReplaceSpacialCharacters(currentExe!!.exPath!!)
                ?.let { Utils.getAssetItems(this, it) }

        if (listImg != null) {
            Log.d("checkListSize", listImg.size.toString())
        }

        if (listImg != null) {
            for (i in 0 until listImg.size) {
                val imgview = ImageView(this)
                //            Glide.with(mContext).load("//android_asset/burpee/".plus(i.toString()).plus(".png")).into(imgview)
                Glide.with(this).load(listImg.get(i)).into(imgview)
                imgview.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                Log.d("videoURIList", listImg.get(i).toString())



                binding!!.viewFlipper.addView(imgview)
            }
        }

        binding!!.viewFlipper.isAutoStart = true
        binding!!.viewFlipper.flipInterval = resources.getInteger(R.integer.viewfliper_animation)
        binding!!.viewFlipper.startFlipping()
    }

    private fun initReadyToGo() {

        loadWorkoutImage()
        binding!!.llReadyToGo.visibility = View.VISIBLE
        binding!!.readyToGOTxt.visibility = View.VISIBLE
        binding!!.llAfterStartWithTime.visibility = View.GONE
        binding!!.remainningTime.visibility = View.GONE
        binding!!.tvExcName.visibility = View.GONE
        binding!!.llAfterStartWithSteps.visibility = View.GONE
        binding!!.tvExcNameReadyToGo.text = currentExe!!.exName
        binding!!.tvExcDescriptionReadyToGo.text = currentExe!!.exDescription
        binding!!.videoView.setVideoURI(Utils.videoURIList.get(currentPos))
        binding!!.videoView.start()
        binding!!.videoView.setOnPreparedListener {
            it.isLooping = true
        }
//        Log.d("checkCurrentPosition",currentPos.toString())

        countDownReadyToGo()

        val readyToGoText = "Ready to go start with ${currentExe!!.exName}"

        MyApplication.speechText(this, readyToGoText)
    }

    private fun countDownReadyToGo() {

        var timeCountDown = 0

        val readyToGoTime =
            Utils.getPref(this, Constant.PREF_READY_TO_GO_TIME, Constant.DEFAULT_READY_TO_GO_TIME)
        binding!!.progressBarReadyToGo.max = readyToGoTime.toInt() - 1

        timer = object : fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause(
            readyToGoTime * 1000L,
            1000,
            true
        ) {
            override fun onFinish() {
                binding!!.tvCountDownReadyToGO.text = "0"
                binding!!.progressBarReadyToGo.progress = readyToGoTime.toInt()
                exStartTime = System.currentTimeMillis()
                startPerformExercise(false)
            }

            override fun onTick(millisUntilFinished: Long) {
                timeCountDown++
                if (readyToGoTime - timeCountDown >= 0) {
                    binding!!.tvCountDownReadyToGO.text = (readyToGoTime - timeCountDown).toString()
                    binding!!.progressBarReadyToGo.progress = timeCountDown

                    if (timeCountDown == readyToGoTime.toInt() / 2) {

                        val readyToGoText = "Please do that on a mat"

                        MyApplication.speechText(this@PerformWorkOutActivity, readyToGoText)

                    } else if ((readyToGoTime - timeCountDown) < 4) {
                        MyApplication.speechText(
                            this@PerformWorkOutActivity,
                            (readyToGoTime - timeCountDown).toString()
                        )
                    }
                } else {
                    timer!!.onFinish()
                    timer!!.cancel()
                }
            }

        }

        Handler().postDelayed(Runnable {
            timer!!.start()
        }, 1000)

    }

    fun startPerformExercise(isNeedDelay: Boolean) {

        workoutProgressIndicatorAdapter!!.setCompletedExercise(currentPos)
        if (currentPos > 0) {
//            binding!!.imgPrevWorkout.visibility = View.VISIBLE
        } else {
//            binding!!.imgPrevWorkout.visibility = View.GONE
        }



        binding?.videoView?.stopPlayback()
        binding!!.videoView.setVideoURI(Utils.videoURIList.get(currentPos))
        binding!!.videoView.start()
        binding!!.videoView.setOnPreparedListener {
            it.isLooping = true
        }

//        Log.d("checkCureeeee",binding.videoView.)

        if (currentExe!!.exUnit.equals(Constant.workout_type_step)) {

            binding!!.tvExcName.text = currentExe!!.exName
            binding!!.tvExcNameStep.text = currentExe!!.exName
            binding!!.tvTotalStep.text = currentExe!!.exTime!!
            binding!!.llReadyToGo.visibility = View.GONE
            binding!!.readyToGOTxt.visibility = View.GONE
            binding!!.llAfterStartWithTime.visibility = View.GONE
            binding!!.remainningTime.visibility = View.GONE

            binding!!.tvExcName.visibility = View.GONE
            binding!!.llAfterStartWithSteps.visibility = View.VISIBLE

        } else {
            binding!!.tvExcName.text = currentExe!!.exName
            binding!!.llReadyToGo.visibility = View.GONE
            binding!!.llAfterStartWithTime.visibility = View.VISIBLE
            binding!!.remainningTime.visibility = View.VISIBLE
            binding!!.tvExcName.visibility = View.VISIBLE
            binding!!.llAfterStartWithSteps.visibility = View.GONE
            binding!!.readyToGOTxt.visibility = View.GONE
            binding!!.progressBarWorkOut.max = currentExe!!.exTime!!.toInt()
            binding!!.progressBarWorkOut.progress = 0
            binding!!.tvCompletedSec.text = "${currentExe!!.exTime!!}\""
            Utils.secToString(currentExe!!.exTime!!.toInt(), "mm:ss")
            binding!!.remainningTime.text =
                Utils.secToString(currentExe!!.exTime!!.toInt(), "mm:ss")
            binding!!.tvTotalSec.text = "/ ${currentExe!!.exTime!!}\""
        }

//        if (isNeedDelay) {
//            val scaleAnimation: Animation = ScaleAnimation(
//                1.0f, 0.0f, 1.0f, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
//            )
//            scaleAnimation.setDuration(1000)
//            scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationRepeat(animation: Animation?) {
//
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    binding!!.tvAnimation.visibility = View.GONE
//                }
//
//                override fun onAnimationStart(animation: Animation?) {
//
//                }
//
//            })
//            timer = object : fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause(10000, 1000, true) {
//                override fun onFinish() {
//                    timer?.cancel()
//                    timer = null
//                    binding!!.tvAnimation.visibility = View.GONE
//                    if (currentExe!!.exUnit.equals(Constant.workout_type_step)) {
//                        startExerciseWithStep()
//                    } else {
//                        startExerciseWithTime()
//                    }
//                    this@PerformWorkOutActivity.start()
//                }
//
//                override fun onTick(millisUntilFinished: Long) {
//                    if ((millisUntilFinished / 1000) > 0) {
//                        MyApplication.speechText(
//                            this@PerformWorkOutActivity,
//                            (millisUntilFinished / 1000).toString()
//                        )
//                        Debug.e("321 Anim", (millisUntilFinished / 1000).toString())
//                        binding!!.tvAnimation.setText((millisUntilFinished / 1000).toString())
//                        binding!!.tvAnimation.visibility = View.VISIBLE
//                        binding!!.tvAnimation.startAnimation(scaleAnimation)
//                    }
//                }
//
//            }
//            timer!!.start()
//        } else {
//            start()
//            if (currentExe!!.exUnit.equals(Constant.workout_type_step)) {
//                startExerciseWithStep()
//            } else {
//                startExerciseWithTime()
//            }
//        }

        start()
        if (currentExe!!.exUnit.equals(Constant.workout_type_step)) {
            startExerciseWithStep()
        } else {
            startExerciseWithTime()
        }


    }

    private fun start() {
        /*val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 1000)*/
        exStartTime = System.currentTimeMillis()
        running = true
    }

    private fun startExerciseWithStep() {
        if (timer != null) {
            timer!!.cancel()
        }
        mySoundUtil.playSound(0)

    }

    private fun startExerciseWithTime() {
        if (timer != null) {
            timer!!.cancel()
        }
        mySoundUtil.playSound(0)

        try {
            countExercise()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val excTime = currentExe!!.exTime!!.toInt()
        val readyToGoText = "Start $excTime seconds ${currentExe!!.exName}"

        MyApplication.speechText(this, readyToGoText)
    }

    private fun countExercise() {

        var timeCountDown = 0

        val exerciseTime = currentExe!!.exTime!!.toInt()
        val halfTime = exerciseTime / 2

        timer =
            object : fitnessapp.workout.homeworkout.stretching.utils.CountDownTimerWithPause(
                currentExe!!.exTime!!.toInt() * 1000L,
                1000,
                true
            ) {
                override fun onFinish() {
                    binding!!.tvCompletedSec.text = "0"
                    binding!!.remainningTime.text = "00:00"
                    binding!!.progressBarWorkOut.progress = exerciseTime
                    onWorkoutTimeOver()
                }

                override fun onTick(millisUntilFinished: Long) {
                    timeCountDown++
                    if ((exerciseTime - timeCountDown) >= 0) {
                        binding!!.tvCompletedSec.text = (exerciseTime - timeCountDown).toString()
//                        Utils.secToString((exerciseTime - timeCountDown).toInt(), "mm:ss")
                        binding!!.remainningTime.text =
                            Utils.secToString((exerciseTime - timeCountDown).toInt(), "mm:ss")
                        binding!!.progressBarWorkOut.progress = timeCountDown

                        if (timeCountDown == halfTime) {
                            MyApplication.speechText(this@PerformWorkOutActivity, "Half time")
                        } else if ((exerciseTime - timeCountDown) < 4) {
                            MyApplication.speechText(
                                this@PerformWorkOutActivity,
                                (exerciseTime - timeCountDown).toString()
                            )
                        }
                    } else {
                        try {
                            timer!!.onFinish()
                            timer!!.cancel()
                        }catch (exc:Exception){
                            Log.d("ThrowException",exc.message.toString())
                        }
                    }
                }

            }

        Handler().postDelayed(Runnable {
            if (timer != null)
                timer!!.start()
        }, 1000)


    }

    private fun onWorkoutTimeOver() {
        stopTimer()

        DataHelper(this).updateCompleteHomeExByDayExId(currentExe!!.exId!!)
        if (currentPos == exercisesList!!.lastIndex) {
            // Go to Complete Screen
            MyApplication.speechText(this, "Congratulation")
            goToCompleteScreen()
        } else {
            goToRestScreen()
        }
    }

    private fun goToRestScreen() {
        mySoundUtil.playSound(mySoundUtil.SOUND_DING)
        val i = Intent(this, RestActivity::class.java)
        i.putExtra("nextEx", Gson().toJson(exercisesList!!.get(currentPos + 1)))
        i.putExtra("nextPos", currentPos + 2)
        i.putExtra("totalEx", exercisesList!!.size)
        startActivityForResult(i, 7029)
    }

    private fun goToCompleteScreen() {
        AdUtils.showAdsDialog(this)
        Log.e(
            TAG,
            "goToCompleteScreen:::: " + Utils.getPref(
                this,
                Constant.STATUS_ENABLE_DISABLE,
                ""
            ) + "  " +
                    Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "")
        )
        if (Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE &&
            Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE
        ) {

            AdUtils.showInterstitialAdsGoogle(this, object : AdsCallback {
                override fun startNextScreenAfterAd() {
                    startCompleteActivity()
                    AdUtils.dismissDialog()
                }

            })
        } else if (Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE &&
            Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK
        ) {
            AdUtils.showInterstitialAdsFacebook(this, object : AdsCallback {
                override fun startNextScreenAfterAd() {
                    startCompleteActivity()
                    AdUtils.dismissDialog()
                }

            })
        } else {
            AdUtils.dismissDialog()
            startCompleteActivity()
        }


    }

    fun startCompleteActivity() {
        val i = Intent(this@PerformWorkOutActivity, CompletedActivity::class.java)
        i.putExtra("workoutPlanData", Gson().toJson(workoutPlanData))
        i.putExtra("ExcList", Gson().toJson(exercisesList))
        i.putExtra("duration", totalExTime)
        startActivity(i)
        finish()
    }

    override fun onResume() {
//        openInternetDialog(this,false)
        super.onResume()
        initMusic(false)
        resumeTimer()
    }


    inner class ClickHandler {

        fun onWorkOutInfoClick() {
            //pauseTimer()
            val i = Intent(this@PerformWorkOutActivity, ExerciseVideoActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(currentExe))
            i.putExtra("posChange", currentPos)
            Log.d("checkPosToPass", currentPos.toString())
            startActivityForResult(i, 786)
//            startActivity(i)
        }

        fun onVideoClick() {
            val i = Intent(this@PerformWorkOutActivity, ExerciseVideoActivity::class.java)
            i.putExtra("workoutPlanData", Gson().toJson(currentExe))
            startActivity(i)
        }

        fun onSoundClick() {
            pauseTimer()
            showSoundOptionDialogPerForm(
                this@PerformWorkOutActivity,
                object : DialogDismissListener {
                    override fun onDialogDismiss() {
                        resumeTimer()
                    }
                })
        }

        fun onReadyToGoClick() {
            // pauseTimer()
            /*  val i = Intent(this@PerformWorkOutActivity, PauseBeforeStartActivity::class.java)
              i.putExtra("workoutPlanData", Gson().toJson(currentExe))
              i.putExtra("nextPos", currentPos + 1)
              i.putExtra("totalEx", exercisesList!!.size)
              startActivity(i)*/
        }

        fun onMusicClick() {
            if (binding!!.llMusic.visibility == View.VISIBLE) {
                binding!!.llMusic.visibility = View.INVISIBLE
            } else {
                binding!!.llMusic.visibility = View.VISIBLE
            }
        }

        fun onPauseMusicClick() {
            if (currMusic != null)
                playMusic()
            else {
                val i = Intent(this@PerformWorkOutActivity, MusicListActivity::class.java)
                startActivity(i)
            }
        }


        fun onPrevMusicClick() {
            if (!Utils.isPurchased(this@PerformWorkOutActivity)) {
                val i = Intent(this@PerformWorkOutActivity, MusicListActivity::class.java)
                startActivity(i)
            } else {
                MyApplication.prevMusic()
            }
        }

        fun onNextMusicClick() {
            if (!Utils.isPurchased(this@PerformWorkOutActivity)) {
                val i = Intent(this@PerformWorkOutActivity, MusicListActivity::class.java)
                startActivity(i)
            } else {
                MyApplication.nextMusic()
            }
        }

        fun onSelectMusicClick() {
            pauseTimer()
            val i = Intent(this@PerformWorkOutActivity, MusicListActivity::class.java)
            startActivity(i)
        }

        fun onSkipReadyToGoClick() {
            startPerformExercise(false)
        }

        fun onBackClick() {
            pauseTimer()
            showQuitDialog()
        }

        fun onNextExerciseClick() {
            stopTimer()
            onWorkoutTimeOver()
        }

        fun onPrevExerciseClick() {
            stopTimer()
            if (currentPos >= 1) {
                val i = Intent(this@PerformWorkOutActivity, RestActivity::class.java)
                i.putExtra("nextEx", Gson().toJson(exercisesList!!.get(currentPos - 1)))
                i.putExtra("nextPos", currentPos - 1)
                i.putExtra("totalEx", exercisesList!!.size)

                currentPos -= 2

                startActivityForResult(i, 7029)
            } else {
                totalExTime = 0
                currentPos = 0
                exStartTime = System.currentTimeMillis()
                currentTime = 0
                running = false
                initReadyToGo()
            }
        }

    }


    var dialogSoundOptionBindingPerForm: BottomSheetSoundOptionBinding? = null
    lateinit var dialogSoundOptionPerForm: BottomSheetDialog

    fun showSoundOptionDialogPerForm(mContext: Context, listner: DialogDismissListener) {
        val v: View = (mContext as Activity).layoutInflater
            .inflate(R.layout.bottom_sheet_sound_option, null)
        dialogSoundOptionBindingPerForm = DataBindingUtil.bind(v)
        dialogSoundOptionPerForm = BottomSheetDialog(mContext)
        dialogSoundOptionPerForm.setContentView(v)

        dialogSoundOptionBindingPerForm!!.switchMute.isChecked =
            Utils.getPref(this, Constant.PREF_IS_SOUND_MUTE, false)
        dialogSoundOptionBindingPerForm!!.switchCoachTips.isChecked =
            Utils.getPref(this, Constant.PREF_IS_COACH_SOUND_ON, true)
        dialogSoundOptionBindingPerForm!!.switchVoiceGuide.isChecked =
            Utils.getPref(this, Constant.PREF_IS_INSTRUCTION_SOUND_ON, true)

        dialogSoundOptionBindingPerForm!!.switchMute.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
//                binding!!.imgSound.setImageResource(R.drawable.ic_mute_sound)
                dialogSoundOptionBindingPerForm!!.switchCoachTips.isChecked = false
                dialogSoundOptionBindingPerForm!!.switchVoiceGuide.isChecked = false
                Utils.setPref(this, Constant.PREF_IS_SOUND_MUTE, true)
            } else {
//                binding!!.imgSound.setImageResource(R.drawable.ic_sound_round)
                Utils.setPref(this, Constant.PREF_IS_SOUND_MUTE, false)
            }
        }

        dialogSoundOptionBindingPerForm!!.switchCoachTips.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dialogSoundOptionBindingPerForm!!.switchMute.isChecked = false
                Utils.setPref(this, Constant.PREF_IS_COACH_SOUND_ON, true)
            } else {
                Utils.setPref(this, Constant.PREF_IS_COACH_SOUND_ON, false)
            }
        }

        dialogSoundOptionBindingPerForm!!.switchVoiceGuide.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dialogSoundOptionBindingPerForm!!.switchMute.isChecked = false
                Utils.setPref(this, Constant.PREF_IS_INSTRUCTION_SOUND_ON, true)
            } else {
                Utils.setPref(this, Constant.PREF_IS_INSTRUCTION_SOUND_ON, false)
            }
        }

        dialogSoundOptionBindingPerForm!!.llDone.setOnClickListener {
            dialogSoundOptionPerForm.dismiss()
        }

        dialogSoundOptionPerForm.setOnDismissListener {
            listner.onDialogDismiss()
        }

        dialogSoundOptionPerForm.show()
    }

    private fun playMusic() {
        if (currMusic != null) {
            if (MyApplication.musicUtil == null || MyApplication.musicUtil!!.isPlaying.not()) {
                binding!!.imgPlayMusic.visibility = View.GONE
                binding!!.imgPauseMusic.visibility = View.VISIBLE
                binding!!.imgMusic.setImageResource(R.drawable.ic_music)
                MyApplication.playMusic(currMusic!!, this@PerformWorkOutActivity)
                Utils.setPref(this@PerformWorkOutActivity, Constant.PREF_IS_MUSIC_MUTE, false)
            } else {
                binding!!.imgPlayMusic.visibility = View.VISIBLE
                binding!!.imgPauseMusic.visibility = View.GONE
                binding!!.imgMusic.setImageResource(R.drawable.ic_music_off)
                Utils.setPref(this@PerformWorkOutActivity, Constant.PREF_IS_MUSIC_MUTE, true)
                MyApplication.stopMusic()
            }
        }
    }

    fun setPlayPauseView() {
        if (MyApplication.musicUtil == null || MyApplication.musicUtil!!.isPlaying.not()) {
            binding!!.imgPlayMusic.visibility = View.VISIBLE
            binding!!.imgPauseMusic.visibility = View.GONE
            binding!!.imgMusic.setImageResource(R.drawable.ic_music_off)
        } else {
            binding!!.imgPlayMusic.visibility = View.GONE
            binding!!.imgPauseMusic.visibility = View.VISIBLE
            binding!!.imgMusic.setImageResource(R.drawable.ic_music)
        }
    }

    override fun onPause() {
        super.onPause()
        pauseTimer()
    }

    override fun onBackPressed() {
        pauseTimer()
        showQuitDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.stopMusic()
        stopTimer()
    }

    fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
            fitnessapp.workout.homeworkout.stretching.utils.Debug.e(TAG, "OnTimer cancel")
        }
    }

    fun resumeTimer() {

        if (timer != null && timer!!.isPaused) {
            timer!!.resume()
//            binding?.videoView?.start()
            fitnessapp.workout.homeworkout.stretching.utils.Debug.e(TAG, "OnTimer resume")
            Log.d("checkResumeTimer1", currentPos.toString() + " curr")
            if (binding?.videoView?.isPlaying?.not()!!) {
//                showToast("not playing")
                binding!!.videoView.setVideoURI(Utils.videoURIList.get(currentPos))
                binding!!.videoView.start()
                binding!!.videoView.setOnPreparedListener {
                    it.isLooping = true
                }
            }
        }
        if (running.not()) {
//            binding?.videoView?.start()
            running = true
            exStartTime = System.currentTimeMillis()
            Debug.e("resumeTimer exStartTime", exStartTime.toString())
            Log.d("checkResumeTimer2", currentPos.toString() + " curr")
        }

    }

    fun pauseTimer() {
        binding?.videoView?.stopPlayback()
        if (running) {

//            binding?.videoView?.stopPlayback()
            running = false
            currentTime = System.currentTimeMillis() - exStartTime
            totalExTime += currentTime / 1000
            Debug.e("pauseTimer currentTime", currentTime.toString())
            Debug.e("pauseTimer totalExTime", totalExTime.toString())
        }

        if (timer != null && timer!!.isRunning) {
            Log.d("whenToCallThis", "pauseTimer")
            timer!!.pause()
            fitnessapp.workout.homeworkout.stretching.utils.Debug.e(TAG, "OnTimer pause")
        }
    }


    var adClickCount: Int = 1
    lateinit var quiteDialog: Dialog
    fun showQuitDialog() {
        MyApplication.speechText(this, getString(R.string.quite_exercise_msg))
        quiteDialog = Dialog(getActivity())
//        quiteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        quiteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




//        Log.d("checkCurrentPos", progressPercentageExercise.toString())

        var dialogQuiteWorkoutBinding =
            DataBindingUtil.inflate<DialogQuiteWorkoutBinding>(
                layoutInflater,
                R.layout.dialog_quite_workout,
                null,
                false
            )

        quiteDialog.setContentView(dialogQuiteWorkoutBinding.root)

        quiteDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogQuiteWorkoutBinding!!.imgClose.setOnClickListener {
            resumeTimer()
            quiteDialog.dismiss()
        }

        dialogQuiteWorkoutBinding.btnContinue.setOnClickListener {
            resumeTimer()
            quiteDialog.dismiss()
        }

        dialogQuiteWorkoutBinding.btnQuit.setOnClickListener {
            if (Utils.getPref(this, Constant.EXIT_BTN_COUNT, 1) == 2) {
                if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE &&
                    Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE
                ) {
                    AdUtils.showInterstitialAdsGoogle(this, object : AdsCallback {
                        override fun startNextScreenAfterAd() {
                            quiteData(quiteDialog)
                        }

                    })
                } else if (Utils.getPref(
                        this,
                        Constant.AD_TYPE_FB_GOOGLE,
                        ""
                    ) == Constant.AD_FACEBOOK &&
                    Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE
                ) {
                    AdUtils.showInterstitialAdsFacebook(this, object : AdsCallback {
                        override fun startNextScreenAfterAd() {
                            quiteData(quiteDialog)
                        }

                    })
                } else {
                    quiteData(quiteDialog)
                }
                Utils.setPref(this, Constant.EXIT_BTN_COUNT, 1)
            } else {
                if (adClickCount == 1) {
                    Utils.setPref(this, Constant.EXIT_BTN_COUNT, 2)
                }
                quiteData(quiteDialog)
            }
        }

        dialogQuiteWorkoutBinding.tvComeback.setOnClickListener {
            //Utils.setComBackIn30Min(this)
            quiteDialog.dismiss()
            finish()
        }

        quiteDialog.setOnDismissListener {
            resumeTimer()
            quiteDialog.dismiss()
        }

        quiteDialog.show()
    }

    fun quiteData(dialog: Dialog) {
        dialog.dismiss()
        saveData()
    }

    private fun saveData() {

        try {
            val calValue = Constant.SEC_DURATION_CAL * totalExTime


//            Log.d("planData", workoutPlanData?.planName.toString()+" \n"+ exercisesList!![0].planId!!)


//            Log.d("checkPlanProgress",calValue.toString()+" "+exercisesList!![0].planId!!+" "+exercisesList!![0].dayId!!)

            if((exercisesList!![0].planId!!) == "6"){
                val progressPercentageExercise =((currentPos+1) * 100) / 13

                dbHelper.updateProgressPlanDayCompleteByDayId(
                    exercisesList!![0].dayId!!,
                    progressPercentageExercise.toString()
                )
            }


            /*dbHelper.addHistory(
                exercisesList!![0].planId!!,
                dbHelper.getPlanNameByPlanId(exercisesList!![0].planId!!),
                Utils.parseTime(Date().time, Constant.DATE_TIME_24_FORMAT),
                totalExTime.toString(),
                calValue.toString(),
                exercisesList!!.size.toString(),
                Utils.getPref(this, Constant.PREF_LAST_INPUT_WEIGHT, 0f).toString(),
                Utils.getPref(this, Constant.PREF_LAST_INPUT_FOOT, 0).toString(),
                Utils.getPref(this, Constant.PREF_LAST_INPUT_INCH, 0F).toString(),
                "0",
                dbHelper.getPlanDayNameByDayId(exercisesList!![0].dayId!!),
                exercisesList!![0].dayId!!
            )*/

//            LocalDB.setLastUnCompletedExPos(this, arrDayExTableClass[0].planId.toInt(), arrDayExTableClass[0].dayId, viewPagerWorkout.currentItem)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7029 && resultCode == Activity.RESULT_OK) {
//            showToast("methodCalled")
            if (data!!.hasExtra("restTime")) {
                totalExTime += data.getLongExtra("restTime", 0)
                Debug.e("onActivityResult totalExTime", totalExTime.toString())
            }

            currentPos++
            binding!!.progressBarTop.progress = currentPos
            currentExe = exercisesList!!.get(currentPos)
            loadWorkoutImage()
            if (data.hasExtra("isRestSkip")) {
                startPerformExercise(data.getBooleanExtra("isRestSkip", false))
            } else {
                startPerformExercise(false)
            }

        }

        if (requestCode == 786 && resultCode == Activity.RESULT_OK) {
//            showToast("coming from video exercise Activity")
//            Log.d("checkToPos",currentPos.toString())
//            binding!!.videoView.setVideoURI(Utils.videoURIList.get(currentPos))
//            binding!!.videoView.start()
//            binding!!.videoView.setOnPreparedListener {
//                it.isLooping = true
//            }
        }
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }
}
