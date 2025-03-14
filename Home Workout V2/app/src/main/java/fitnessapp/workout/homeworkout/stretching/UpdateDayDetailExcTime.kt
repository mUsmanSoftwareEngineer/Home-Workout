package fitnessapp.workout.homeworkout.stretching

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityUpdateDayDetailExcTimeBinding
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.objects.HomeExTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils

class UpdateDayDetailExcTime : BaseActivity(), CallbackListener {

    var binding: ActivityUpdateDayDetailExcTimeBinding? = null
    var workoutPlanData: HomeExTableClass? = null
    var pos: Int? = null
    var second = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_day_detail_exc_time)

        AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility = View.GONE
            binding!!.llAdView.visibility = View.VISIBLE
        }

        initIntentParam()
        binding!!.handler = ClickHandler()
//        pos?.let { UpdateExcDetailDialog(workoutPlanData, it) }
        fillData()

    }

    private fun initIntentParam() {
        try {

            if (intent.extras != null) {

                if (intent.extras!!.containsKey("posUpdate")) {
                    pos = intent.getIntExtra("posUpdate", -1)
                }

                if (intent.extras!!.containsKey("ExDetailUpdate")) {
                    val str = intent.getStringExtra("ExDetailUpdate")
                    Log.d("checkToUpdate",str.toString())

                    workoutPlanData = Gson().fromJson(
                        str,
                        object : TypeToken<HomeExTableClass>() {}.type
                    )!!
                }

                Log.d(
                    "check001",
                    workoutPlanData?.exName.toString() + "\n" + workoutPlanData?.exDescription + "\n" +
                            workoutPlanData?.exTime + "\n" + workoutPlanData?.exPath
                )

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fillData() {
        if (workoutPlanData != null) {


            binding!!.tvTitle.text = workoutPlanData!!.exName
            binding!!.tvDes.text = workoutPlanData!!.exDescription

            if (workoutPlanData!!.exTime.isNullOrEmpty()) {
//                binding!!.llTime.visibility = View.GONE
            } else {
                second = workoutPlanData!!.exTime!!.toInt()
//                binding!!.llTime.visibility = View.VISIBLE
//                if (workoutPlanData!!.exReplaceTime.isNullOrEmpty()) {
//                    second = workoutPlanData!!.exTime!!.toInt()
//                } else {
//                    second = workoutPlanData!!.exReplaceTime!!.toInt()
//                    binding!!.tvReset.visibility = View.VISIBLE
//                }
//                defaultSecond = workoutPlanData!!.exTime!!.toInt()

                if (workoutPlanData!!.exUnit.equals(Constant.workout_type_step)) {
                    binding!!.tvTime.text = "${second}"
                } else {
                    binding!!.tvTime.text = Utils.secToString(
                        second,
                        Constant.WORKOUT_TIME_FORMAT
                    )
                }
            }

            binding!!.excVideo.setVideoURI(pos?.let { Utils.videoURIList.get(it) })
            binding!!.excVideo.start()
            binding!!.excVideo.setOnPreparedListener {
                it.isLooping=true
            }
        }
    }

//    fun UpdateExcDetailDialog(
//        excList: HomeExTableClass?,
//        position: Int
//    ) {
//        var currPos = position
//
//
//        val item = excList.get(position)
//        binding?.tvTitle?.text ?:   item.exName
//                binding?.tvDes?.text = item.exDescription
//
//
//        if (item.exUnit.equals(Constant.workout_type_step)) {
//            binding?.tvTime?.text ?:  "X ${item.exTime}"
//        } else {
//            binding?.tvTime?.text ?:
//            Utils.secToString(item.exTime!!.toInt(), Constant.WORKOUT_TIME_FORMAT)
//        }
//
//        var second = item.exTime!!.toInt()
//
//        binding?.imgPlus?.setOnClickListener{
//
//
//            showToast("plus click")
//            second++
//            if (item.exUnit.equals(Constant.workout_type_step)) {
//                binding?.tvTime?.text ?:   "X ${second}"
//            } else {
//                binding?.tvTime?.text ?: Utils.secToString(second, Constant.WORKOUT_TIME_FORMAT)
//            }
//        }
//
//        binding?.imgMinus?.setOnClickListener{
//            showToast("plus click")
//            second--
//            if (item.exUnit.equals(Constant.workout_type_step)) {
//                binding?.tvTime?.text ?:   "X ${second}"
//            } else {
//                binding?.tvTime?.text ?:
//                Utils.secToString(second, Constant.WORKOUT_TIME_FORMAT)
//            }
//        }
//
//
//
////            binding.tvSave.setOnClickListener {
////                Log.d("checkSaveDetail",item.dayExId+" "+second.toString()+" ")
////                dbHelper.updateDayExTime(item.dayExId!!.toInt(), second.toString())
////
//////                binding.dismiss()
////            }
//
//        binding?.tvReset?.setOnClickListener {
//            second = dbHelper.getOriginalDetailExTime(item.dayExId!!.toInt())!!.toInt()
//            dbHelper.updateDayExTime(item.dayExId!!.toInt(), second.toString())
//            Log.d("checkTime",second.toString())
//            //                if (item.exUnit.equals(Constant.workout_type_step)) {
//            //                    binding.tvTime.text = "X ${second}"
//            //                } else {
//            //                    binding.tvTime.text =
//            //                        Utils.secToString(second.toInt(), Constant.WORKOUT_TIME_FORMAT)
//            //                }
//
//        }
//
//
//
//
//
//
//
//
////        showExcDetail(currPos)
//
//
//    }

    inner class ClickHandler {

        //        fun onCancelClick() {
//            finish()
//        }

        fun onBackClick() {
            binding?.excVideo?.stopPlayback()
            finish()
        }

        fun onResetClick() {
//            if (workoutPlanData!!.exUnit.equals(Constant.workout_type_step)) {
//                binding!!.tvTime.text = "${defaultSecond}"
//            } else {
//                binding!!.tvTime.text =
//                    Utils.secToString(defaultSecond, Constant.WORKOUT_TIME_FORMAT)
//            }
//            binding!!.tvReset.visibility = View.GONE
//            workoutPlanData!!.exReplaceTime = ""
//            second = defaultSecond
//            isEditTime = false
            binding?.excVideo?.stopPlayback()
            Log.d("checkPlanId", workoutPlanData?.planId.toString())
            if(workoutPlanData?.planId.toString().equals("6")){
                second = dbHelper.getOriginalDetailExTime(workoutPlanData?.dayExId!!.toInt())!!.toInt()
                dbHelper.updateDayExTime(workoutPlanData!!.dayExId!!.toInt(), second.toString())
                val i = Intent()
                setResult(Activity.RESULT_OK, i)
                finish()
            }
            else{
//                second = dbHelper.getOriginalDetailExTime(workoutPlanData?.dayExId!!.toInt())!!.toInt()
                second = dbHelper.getOriginalPlanExTime(workoutPlanData?.dayExId!!.toInt())!!.toInt()
                dbHelper.updatePlanExTime(workoutPlanData?.dayExId!!.toInt(), second.toString())
                Log.d("checkSec",second.toString())
//                dbHelper.updateDayExTime(workoutPlanData!!.dayExId!!.toInt(), second.toString())
                val i = Intent()
                setResult(Activity.RESULT_OK, i)
                finish()
            }

        }

        fun onMinusTimeClick() {
            if (second > 1)
                second--
            binding!!.tvTime.text = Utils.secToString(second, "mm:ss")
            binding!!.tvReset.visibility = View.VISIBLE
        }

        fun onAddTimeClick() {
//            showToast("Add Time Clicked")
            second++
            binding!!.tvTime.text = Utils.secToString(second, "mm:ss")
            binding!!.tvReset.visibility = View.VISIBLE
        }

        //        fun onVideoClick() {
//            val i = Intent(getActivity(), ExerciseVideoActivity::class.java)
//            i.putExtra("workoutPlanData", Gson().toJson(workoutPlanData))
//            startActivity(i)
//        }
//
        fun onAddClick() {

//            val i = Intent()
//            if (isEditTime)
//                workoutPlanData!!.exReplaceTime = second.toString()
//            i.putExtra("workoutPlanData", Gson().toJson(workoutPlanData))
//            i.putExtra("categoryDetail", Gson().toJson(categoryData))
//            if (pos != null && pos!! >= 0) {
//                i.putExtra("pos", pos!!)
//            }
//            setResult(Activity.RESULT_OK, i)
            if(workoutPlanData?.planId.toString().equals("6")){
                binding?.excVideo?.stopPlayback()
                dbHelper.updateDayExTime(workoutPlanData?.dayExId!!.toInt(), second.toString())
                val i = Intent()
                setResult(Activity.RESULT_OK, i)
                finish()
            }
            else{
//                second = dbHelper.getOriginalDetailExTime(workoutPlanData?.dayExId!!.toInt())!!.toInt()
//                second = dbHelper.getOriginalPlanExTime(workoutPlanData?.dayExId!!.toInt())!!.toInt()
                dbHelper.updatePlanExTime(workoutPlanData?.dayExId!!.toInt(), second.toString())
                Log.d("checkSec",second.toString())
//                dbHelper.updateDayExTime(workoutPlanData!!.dayExId!!.toInt(), second.toString())
                val i = Intent()
                setResult(Activity.RESULT_OK, i)
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
}