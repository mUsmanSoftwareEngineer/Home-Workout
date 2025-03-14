package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityDiscoverDetailBinding
import fitnessapp.workout.homeworkout.stretching.adapter.DiscoverSubPlanAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class DiscoverDetailActivity : BaseActivity(), CallbackListener {

    var binding: ActivityDiscoverDetailBinding? = null
    var discoverSubPlanAdapter: DiscoverSubPlanAdapter? = null
    var workoutPlanData: HomePlanTableClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_discover_detail)

//        AdUtils.loadBannerAd(binding!!.adView,this)

//        AdUtils.loadBannerGoogleAd(this,binding!!.llAdView, Constant.BANNER_TYPE)

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

        showUnlockTrainingDialog(this)
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

        discoverSubPlanAdapter = DiscoverSubPlanAdapter(this)
        binding!!.rvWorkOuts.layoutManager = LinearLayoutManager(this)
        binding!!.rvWorkOuts.setAdapter(discoverSubPlanAdapter)

        discoverSubPlanAdapter!!.setEventListener(object : DiscoverSubPlanAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = discoverSubPlanAdapter!!.getItem(position)
                val i = Intent(this@DiscoverDetailActivity, ExercisesListActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                if (item.isPro){
                    i.putExtra(Constant.IS_PURCHASE, true)
                }else {
                    i.putExtra(Constant.IS_PURCHASE, false)
                }
                startActivity(i)
            }

        })

        var isShow = true
        var scrollRange = -1
        binding!!.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding!!.llTopTitle.visibility = View.VISIBLE
                isShow = true
            } else if (isShow) {
                binding!!.llTopTitle.visibility =
                    View.GONE //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })

        fillData()

    }


    private fun fillData() {
        if (workoutPlanData != null) {

            binding!!.tvTitle.text = workoutPlanData!!.planName
            binding!!.tvDes.text = workoutPlanData!!.shortDes
            binding!!.tvTitleText.text = workoutPlanData!!.planName

            binding!!.titleImage.setImageResource(
                Utils.getDrawableId(
                    workoutPlanData!!.planImage,
                    this
                )
            )

            discoverSubPlanAdapter!!.addAll(dbHelper.getHomeSubPlanList(workoutPlanData!!.planId!!))
        }
    }

    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
    }


    inner class ClickHandler {

        fun onStartClick() {
//            val i = Intent(this@DiscoverDetailActivity,PerformWorkOutActivity::class.java)
//            startActivity(i)
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
