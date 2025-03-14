package fitnessapp.workout.homeworkout.stretching

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityMetricImperialUnitsBinding
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class MetricImperialUnitsActivity : BaseActivity(), CallbackListener {

    var binding: ActivityMetricImperialUnitsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_metric_imperial_units)

//        AdUtils.loadBannerAd(binding!!.adView,this)
//        AdUtils.loadBannerGoogleAd(this,binding!!.llAdView,Constant.BANNER_TYPE)

        if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
            AdUtils.loadGoogleBannerAd(this, binding!!.llAdView, Constant.BANNER_TYPE)
            binding!!.llAdViewFacebook.visibility=View.GONE
            binding!!.llAdView.visibility=View.VISIBLE
        }
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
        binding!!.topbar.tvTitleText.text = getString(R.string.metric_and_imperial_units)
        binding!!.topbar.toolbarTitle.visibility = View.VISIBLE
        binding!!.topbar.toolbar.setBackgroundResource(R.color.green_seven_fit)
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()

        binding!!.tvHeightUnit.text = Utils.getPref(this, Constant.PREF_HEIGHT_UNIT, Constant.DEF_CM)
        binding!!.tvWeightUnit.text = Utils.getPref(this, Constant.PREF_WEIGHT_UNIT, Constant.DEF_KG)

    }




    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
    }


    inner class ClickHandler {

        fun onWeightUnitClick(){
            showWeightUnitDialog()
        }

        fun onHeightUnitClick(){
            showHeightUnitDialog()
        }
    }

    private fun showWeightUnitDialog() {

        val weightUnits = arrayOf<CharSequence>("Kg", "Lbs")

        var checkedItem = 0
        if ( Utils.getPref(this, Constant.PREF_WEIGHT_UNIT, Constant.DEF_KG).equals(Constant.DEF_LB)) {
            checkedItem = 1
        }

        val builder = AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
        builder.setTitle(R.string.weight_unit)
        builder.setSingleChoiceItems(weightUnits,checkedItem,object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                if (which == 0) {
                    Utils.setPref(this@MetricImperialUnitsActivity, Constant.PREF_WEIGHT_UNIT, Constant.DEF_KG)
                } else {
                    Utils.setPref(
                        this@MetricImperialUnitsActivity,
                        Constant.PREF_WEIGHT_UNIT,
                        Constant.DEF_LB
                    )
                }

                binding!!.tvWeightUnit.text = Utils.getPref(this@MetricImperialUnitsActivity, Constant.PREF_WEIGHT_UNIT, Constant.DEF_KG)
                if (dialog != null) {
                    dialog.dismiss()
                }
            }

        })

        builder.create().show()
    }

    private fun showHeightUnitDialog() {

        val heightUnits = arrayOf<CharSequence>("Cm", "In")

        var checkedItem = 0
        if ( Utils.getPref(this, Constant.PREF_HEIGHT_UNIT, Constant.DEF_CM).equals(Constant.DEF_IN)) {
            checkedItem = 1
        }

        val builder = AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
        builder.setTitle(R.string.height_unit)
        builder.setSingleChoiceItems(heightUnits,checkedItem,object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (which == 0) {
                    Utils.setPref(this@MetricImperialUnitsActivity, Constant.PREF_HEIGHT_UNIT, Constant.DEF_CM)
                } else {
                    Utils.setPref(this@MetricImperialUnitsActivity, Constant.PREF_HEIGHT_UNIT, Constant.DEF_IN)
                }
                binding!!.tvHeightUnit.text = Utils.getPref(this@MetricImperialUnitsActivity, Constant.PREF_HEIGHT_UNIT, Constant.DEF_CM)
                if (dialog != null) {
                    dialog.dismiss()
                }
            }

        })

        builder.create().show()
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
