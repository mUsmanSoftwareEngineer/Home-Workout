package fitnessapp.workout.homeworkout.stretching

import android.content.*
import android.net.ConnectivityManager
import android.os.*
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.android.billingclient.api.*
import fitnessapp.workout.homeworkout.stretching.db.DataHelper
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import fitnessapp.workout.homeworkout.stretching.viewmodel.AdsIdData
import fitnessapp.workout.homeworkout.stretching.interfaces.AdsCallback
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Debug
import java.util.*


class SplashScreenActivity : BaseActivity(), CallbackListener, AdsCallback {

    internal var handler = Handler()
    internal var bindService: Boolean = false
    lateinit var adsIdData: AdsIdData
    private var billingClient: BillingClient? = null
    val SKU_MONTHLY_SUB = "99monthlysubscription"
    val SKU_YEAR_SUB = "499yearlysubscription"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
//        adsIdData = AdsIdData()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }




        DataHelper(this).checkDBExist()
//        initInAppPurchase()


//        networkCheck()
        Handler().postDelayed({
            val i = Intent(getActivity(), HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }, 2000)

    }


    private fun initInAppPurchase() {
        try {
            billingClient = BillingClient.newBuilder(this).setListener(purchaseUpdateListener).enablePendingPurchases().build()
            billingClient!!.startConnection(object : BillingClientStateListener {


                override fun onBillingServiceDisconnected() {
                }

                override fun onBillingSetupFinished(p0: BillingResult) {
//                    checkSubscriptionList()
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private val purchaseUpdateListener: PurchasesUpdatedListener = PurchasesUpdatedListener { result, _ ->
        try {
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                if (result.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Utils.setPref(this, Constant.PREF_KEY_PURCHASE_STATUS, true)
                }
            } else {
                Utils.setPref(this, Constant.PREF_KEY_PURCHASE_STATUS, true)
            }
//            checkSubscriptionList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private suspend fun checkSubscriptionList() {
//        if (billingClient != null) {
//            var isPurchasedSku = false
//            try {
//                val purchasesResult = billingClient!!.queryPurchasesAsync(BillingClient.SkuType.SUBS)
//                if (purchasesResult.purchasesList.size == 0) {
//
//
//                    val purchaseDataList = purchasesResult.purchasesList
//                    Log.e("", "purchaseDataList::$purchaseDataList")
//                    if (purchaseDataList != null) {
//                        for (i in 0 until purchaseDataList.size) {
//                            val purchaseData = purchaseDataList[i]
//                            if ((purchaseData.skus == SKU_MONTHLY_SUB) || (purchaseData.skus == SKU_YEAR_SUB)) {
//                                isPurchasedSku = true
//                            }
//
//                            if (purchaseData.purchaseState == Purchase.PurchaseState.PURCHASED) {
//                                if (!purchaseData.isAcknowledged) {
//                                    val acknowledgePurchaseParams =
//                                        AcknowledgePurchaseParams.newBuilder()
//                                            .setPurchaseToken(purchaseData.purchaseToken)
//                                    billingClient!!.acknowledgePurchase(acknowledgePurchaseParams.build()){
//                                            p0 -> Debug.e("BillingResult ======>", p0.debugMessage)
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    Utils.setPref(this, Constant.PREF_KEY_PURCHASE_STATUS, isPurchasedSku)
//
//                }
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//            dismissDialog()
//
//        }
//    }




    fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun networkCheck() {
        if (isNetworkConnected()) {
            successCall()
        } else {
            openInternetDialog(this, true)
        }
    }

    fun successCall() {

        if (!Utils.getPref(this, Constant.IS_FIRST_TIME, false)) {
            Utils.setPref(this, Constant.IS_FIRST_TIME, true)
            checkPermissions(this)
            Utils.setPref(this, Constant.SPLASH_SCREEN_COUNT, 2)
        }else{
            if (Utils.getPref(this, Constant.SPLASH_SCREEN_COUNT, 1) == 1) {
                Utils.setPref(this, Constant.SPLASH_SCREEN_COUNT, 2)
               checkPermissions(this)
            } else {
                checkAd()
            }
        }

    }

    private fun checkAd() {
        if (Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE) {
            Log.e("TAG", "checkAd:L::::IFF:: "+Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, ""))
            if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_GOOGLE) {
                AdUtils.GooglebeforloadAd(this)
            } else if (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "") == Constant.AD_FACEBOOK) {
                AdUtils.FacebookbeforeloadFullAd(this)
            }

            if (Utils.getPref(this, Constant.STATUS_ENABLE_DISABLE, "") == Constant.ENABLE) {
                Handler().postDelayed({
                    when (Utils.getPref(this, Constant.AD_TYPE_FB_GOOGLE, "")) {
                        Constant.AD_GOOGLE -> {
                            AdUtils.showInterstitialAdsGoogle(this, this)
                        }
                        Constant.AD_FACEBOOK -> {
                            AdUtils.showInterstitialAdsFacebook(this, this)
                        }
                        else -> {
                            startNextActivity()
                        }
                    }
                    Utils.setPref(this, Constant.SPLASH_SCREEN_COUNT, 1)
                }, 2000)
            }else{
                checkPermissions(this)
            }


        } else {
            checkPermissions(this)
        }

    }


    fun startapp(sleepTime: Long) {
        handler.postDelayed(startApp, sleepTime)
    }

    var startApp: Runnable = object : Runnable {
        override fun run() {
            handler.removeCallbacks(this)
            startNextActivity()

        }
    }

    fun startNextActivity(){
        if (Utils.getPref(this@SplashScreenActivity, Constant.PREF_IS_FIRST_TIME, true) &&
            Utils.getPref(this@SplashScreenActivity, Constant.PREF_WHATS_YOUR_GOAL, "").isNullOrEmpty()) {


            val i = Intent(getActivity(), WhatsYourGoalActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        } else {
            val i = Intent(getActivity(), HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }
    }


    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {
//        networkCheck()
    }

    override fun startNextScreenAfterAd() {
        startNextActivity()
    }

}
