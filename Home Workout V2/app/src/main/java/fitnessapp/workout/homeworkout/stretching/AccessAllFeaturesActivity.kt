package fitnessapp.workout.homeworkout.stretching

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityAccessAllFeatureBinding
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Debug
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import kotlinx.coroutines.launch
import java.util.*


class AccessAllFeaturesActivity : BaseActivity(), CallbackListener {

    private var billingClient: BillingClient? = null
    var binding: ActivityAccessAllFeatureBinding? = null
    internal val SKU_MONTHLY_SUB = "99monthlysubscription"
    internal val SKU_YEAR_SUB = "499yearlysubscription"
    internal var mSelectedSubscriptionPeriod = SKU_YEAR_SUB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_access_all_feature)

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
        binding!!.handler = ClickHandler()
        initInAppPurchase()
        binding!!.handler?.onFreeTrialClick()

    }



    private fun initInAppPurchase() {
        try {
            billingClient = BillingClient.newBuilder(this).setListener(purchaseUpdateListener).enablePendingPurchases().build()
            billingClient!!.startConnection(object : BillingClientStateListener {


                override fun onBillingServiceDisconnected() {
                    Debug.e("TAG", "onBillingServiceDisconnected::::: " )
                }

                override fun onBillingSetupFinished(p0: BillingResult) {
                    Debug.e("TAG", "onBillingSetupFinished:::: "+p0.debugMessage )
                    lifecycleScope.launch {
                        checkSubscriptionList()
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getSKUDetails() {

        val params = SkuDetailsParams.newBuilder()

        val productIds = arrayListOf<String>()
        productIds.add(SKU_MONTHLY_SUB)
        productIds.add(SKU_YEAR_SUB)

        params.setSkusList(productIds).setType(BillingClient.SkuType.SUBS)
        billingClient!!.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == 0 && skuDetailsList != null) {
                for (thisResponse in skuDetailsList) {
                    try {
                        if (thisResponse.sku == SKU_MONTHLY_SUB) {
                            binding!!.tvMonthly.text = "Then ${thisResponse.price} / Month"
                        } else if (thisResponse.sku == SKU_YEAR_SUB) {
                            binding!!.tvYearly.text = "${thisResponse.originalPrice} / Yearly"
                        }

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private val purchaseUpdateListener: PurchasesUpdatedListener = PurchasesUpdatedListener { result, _ ->
        try {
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                if (result.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                    Utils.setPref(this@AccessAllFeaturesActivity, Constant.PREF_KEY_PURCHASE_STATUS, true)
                    startActivity(Intent(this@AccessAllFeaturesActivity, HomeActivity::class.java))
                    finish()
                }
            } else {
                Utils.setPref(this@AccessAllFeaturesActivity, Constant.PREF_KEY_PURCHASE_STATUS, true)
                startActivity(Intent(this@AccessAllFeaturesActivity, HomeActivity::class.java))
                finish()
            }
            lifecycleScope.launch {
                checkSubscriptionList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun checkSubscriptionList() {
        if (billingClient != null) {
            var isPurchasedSku = false
            try {
                val purchasesResult = billingClient!!.queryPurchasesAsync(BillingClient.SkuType.SUBS)
                if (purchasesResult.billingResult.responseCode == 0) {


                    val purchaseDataList = purchasesResult.purchasesList
                    Log.e("", "purchaseDataList::$purchaseDataList")
                    if (purchaseDataList != null) {
                        for (i in 0 until purchaseDataList.size) {
                            val purchaseData = purchaseDataList[i]
                            if ((purchaseData.skus.equals(SKU_MONTHLY_SUB)) || (purchaseData.skus.equals(SKU_YEAR_SUB))) {
                                isPurchasedSku = true
                            }

                            if (purchaseData.purchaseState == com.android.billingclient.api.Purchase.PurchaseState.PURCHASED) {
                                if (!purchaseData.isAcknowledged) {
                                    val acknowledgePurchaseParams =
                                        AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchaseData.purchaseToken)
                                    billingClient!!.acknowledgePurchase(acknowledgePurchaseParams.build()){
                                            p0 -> Debug.e("BillingResult ======>", p0.debugMessage)
                                    }
                                }
                            }
                        }
                    }
                    Utils.setPref(this@AccessAllFeaturesActivity, Constant.PREF_KEY_PURCHASE_STATUS, isPurchasedSku)
                    getSKUDetails()
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            dismissDialog()

        }
    }


    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
    }


    inner class ClickHandler {

        fun onBackClick(){
            finish()
        }

        fun onFreeTrialClick(){
            binding!!.borderFreeTrial.visibility = View.VISIBLE
            binding!!.borderPerMonth.visibility = View.INVISIBLE
            binding!!.llFreeTrial.setBackgroundResource(R.drawable.bg_dark_green_radius_8)
            binding!!.llPerMonth.setBackgroundResource(R.drawable.bg_dark_blue_radius_8)
            binding!!.imgCheckFreeTrial.setImageResource(R.drawable.ic_goal_complete)
            binding!!.imgCheckPerMonth.setImageResource(R.drawable.bg_circle_border)
            mSelectedSubscriptionPeriod = SKU_YEAR_SUB
        }

        fun onPerMonthClick(){
            binding!!.borderFreeTrial.visibility = View.INVISIBLE
            binding!!.borderPerMonth.visibility = View.VISIBLE
            binding!!.llFreeTrial.setBackgroundResource(R.drawable.bg_dark_blue_radius_8)
            binding!!.llPerMonth.setBackgroundResource(R.drawable.bg_dark_green_radius_8)
            binding!!.imgCheckFreeTrial.setImageResource(R.drawable.bg_circle_border)
            binding!!.imgCheckPerMonth.setImageResource(R.drawable.ic_goal_complete)
            mSelectedSubscriptionPeriod = SKU_MONTHLY_SUB


        }

        fun onContinueClick(){
            onPurchaseClick(mSelectedSubscriptionPeriod)
        }

    }

    private fun onPurchaseClick(SKU: String) {
        val skuList = arrayListOf<String>()
        skuList.add(SKU)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient!!.querySkuDetailsAsync(params.build()) { _, list ->
            runOnUiThread {
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(list!![0])
                    .build()
                val responseCode = billingClient!!.launchBillingFlow(this, billingFlowParams).responseCode
                Debug.e("BillingFlow responce", responseCode.toString() + "")
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
