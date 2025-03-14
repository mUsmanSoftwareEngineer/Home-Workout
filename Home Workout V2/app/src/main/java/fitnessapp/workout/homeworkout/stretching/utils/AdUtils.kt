package fitnessapp.workout.homeworkout.stretching.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.stretching.interfaces.AdsCallback


internal object AdUtils {


    fun loadGoogleBannerAd(context: Context, llAdview: RelativeLayout, type: String) {
        try {
            Log.e(
                "TAG",
                "loadBannerAdBackend::::::: " + Utils.isPurchased(context) + "  " + Debug.DEBUG_IS_HIDE_AD
            )
            if (Debug.DEBUG_IS_HIDE_AD.not() && Utils.isPurchased(context).not()) {
//                val adViewBottom = AdView(context)
//                if (type.equals(Constant.BANNER_TYPE)) {
//                    adViewBottom.adSize = AdSize.BANNER
//                } else if (type.equals(Constant.REC_BANNER_TYPE)) {
//                    adViewBottom.adSize = AdSize.MEDIUM_RECTANGLE
//                }
//                adViewBottom.adUnitId = Utils.getPref(context, Constant.GOOGLE_BANNER, "").toString()
//                llAdview.addView(adViewBottom)
//                val adRequest = AdRequest.Builder().build()
//                adViewBottom.loadAd(adRequest)
//                adViewBottom.adListener = object : AdListener() {
//                    override fun onAdLoaded() {
//                        super.onAdLoaded()
//                        adViewBottom.visibility = View.VISIBLE
//                        llAdview.visibility = View.VISIBLE
//                    }
//
//                    override fun onAdFailedToLoad(p0: LoadAdError) {
//                        super.onAdFailedToLoad(p0)
//                        llAdview.visibility = View.GONE
//                        Log.e("TAG", "onAdFailedToLoad:Faild :::  " + p0.toString())
//                    }
//
//
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFacebookBannerAd(context: Context, banner_container: LinearLayout) {
//        try {
//            if (Debug.DEBUG_IS_HIDE_AD.not() && Utils.isPurchased(context).not()) {
//
//
//                Log.e("TAG", "loadFbAdFacebook::::::::::: ")
//                var adView: com.facebook.ads.AdView? = null
//                adView =
//                    com.facebook.ads.AdView(
//                        context,
//                        Utils.getPref(context, Constant.FB_BANNER, ""),
//                        com.facebook.ads.AdSize.BANNER_HEIGHT_50
//                    )
//
//
//                banner_container.addView(adView)
//
//                val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
//                    override fun onError(ad: Ad?, adError: com.facebook.ads.AdError) {
//                        // Ad error callback
//                        Log.e(
//                            "TAG",
//                            "onError:Fb:::: ${adError.errorMessage}   ${adError.errorCode}"
//                        )
//                        banner_container.visibility = View.GONE
//                    }
//
//                    override fun onAdLoaded(ad: Ad?) {
//                        // Ad loaded callback
//                        Log.e("TAG", "onAdLoaded:::::: ")
//                        banner_container.visibility = View.VISIBLE
//                    }
//
//                    override fun onAdClicked(ad: Ad?) {
//                        // Ad clicked callback
//                    }
//
//                    override fun onLoggingImpression(ad: Ad?) {
//                        // Ad impression logged callback
//                    }
//
//
//                }
//
//                adView!!.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    fun loadFacebookMediumRectangleAd(context: Context, banner_container: LinearLayout) {
//        try {
//            if (Debug.DEBUG_IS_HIDE_AD.not() && Utils.isPurchased(context).not()) {
//                Log.e("TAG", "loadFbAdFacebook::::::::::: ")
//                var adView: com.facebook.ads.AdView? = null
//
//                adView =
//                    com.facebook.ads.AdView(
//                        context,
//                        Utils.getPref(context, Constant.FB_BANNER_RECTANGLE_AD, ""),
//                        com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250
//                    )
//
//
//                banner_container.addView(adView)
//
//                val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
//                    override fun onError(ad: Ad?, adError: AdError) {
//                        // Ad error callback
//                        Log.e("TAG", "onError:Fb:::: $adError")
//                        banner_container.visibility = View.GONE
//                    }
//
//                    override fun onAdLoaded(ad: Ad?) {
//                        // Ad loaded callback
//                        Log.e("TAG", "onAdLoaded:::::: ")
//                        banner_container.visibility = View.VISIBLE
//                    }
//
//                    override fun onAdClicked(ad: Ad?) {
//                        // Ad clicked callback
//                    }
//
//                    override fun onLoggingImpression(ad: Ad?) {
//                        // Ad impression logged callback
//                    }
//
//
//                }
//
//                adView!!.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    internal var adsDialog: Dialog? = null
    fun showAdsDialog(context: Context) {
        try {
            adsDialog = Dialog(context)
            adsDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            adsDialog!!.setContentView(R.layout.dialog_full_screen_ad)
            adsDialog!!.setCancelable(false)
            adsDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissDialog() {
        try {
            if (adsDialog != null && adsDialog!!.isShowing) {
                adsDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private fun getAdRequest(): AdRequest {
//        return AdRequest.Builder().build()
//    }


    /*Google Full Ad*/
    /*var mInterstitialAdGoogle: InterstitialAd? = null
    private lateinit var adsCallback: AdsCallback
    fun GooglebeforloadAd(context: Context) {
        try {
            mInterstitialAdGoogle = InterstitialAd(context)
            mInterstitialAdGoogle!!.adUnitId =
                Utils.getPref(context, Constant.GOOGLE_INTERSTITIAL, "")
            mInterstitialAdGoogle!!.loadAd(getAdRequest())
            mInterstitialAdGoogle!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.e("TAG", "onAdLoaded:::::Before:: ")

                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Code to be executed when an ad request fails.
                    Log.e("TAG", "onAdFailedToLoad:::::Before::: $adError")
                }

                override fun onAdOpened() {
                    Log.e("TAG", "onAdOpened:::::Before::: ")
                    // Code to be executed when the ad is displayed.
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    adsCallback.startNextScreenAfterAd()
                    Log.e("TAG", "onAdClosed:::::Before::::   ")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun showInterstitialAdsGoogle(context: Context, adsCallback: AdsCallback) {

        try {
            if (Debug.DEBUG_IS_HIDE_AD.not() && Utils.isPurchased(context).not()) {
                this.adsCallback = adsCallback
                if (mInterstitialAdGoogle!!.isLoaded) {
                    mInterstitialAdGoogle!!.show()
                    mInterstitialAdGoogle = null
                } else {
                    adsCallback.startNextScreenAfterAd()
                    Log.e("TAG", "The interstitial wasn't loaded yet.")
                }
            } else {
                adsCallback.startNextScreenAfterAd()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/


    //    var mInterstitialAd: InterstitialAd? = null
    fun GooglebeforloadAd(context: Context) {

//        try {
//            InterstitialAd.load(
//                context, Utils.getPref(context, Constant.GOOGLE_INTERSTITIAL, "")!!, getAdRequest(),
//                object : InterstitialAdLoadCallback() {
//                    override fun onAdFailedToLoad(adError: LoadAdError) {
//                        Log.e("TAG ERRRR:::: ", adError.message)
//                        mInterstitialAd = null
//                    }
//
//                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                        Log.e("TAG", "Ad was loaded.")
//                        mInterstitialAd = interstitialAd
//                    }
//                }
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }
    fun showInterstitialAdsGoogle(context: Context,adsCallback: AdsCallback) {

//        try {
//            if (mInterstitialAd != null) {
//                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//                    override fun onAdDismissedFullScreenContent() {
//                        Log.d("TAG", "Ad was dismissed.")
//                        // Don't forget to set the ad reference to null so you
//                        // don't show the ad a second time.
//                        mInterstitialAd = null
//                        adsCallback.startNextScreenAfterAd()
//
//                    }
//
////                    override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError?) {
////                        Log.d("TAG", "Ad failed to show.")
////                        // Don't forget to set the ad reference to null so you
////                        // don't show the ad a second time.
////                        mInterstitialAd = null
////                        adsCallback.startNextScreenAfterAd()
////                    }
//
//                }
//                mInterstitialAd?.show(context as Activity)
//            } else {
//                Log.e("TAG", "showInterstitialAdsGoogle:::::NOT LOADED:::::  " )
//                adsCallback.startNextScreenAfterAd()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }



    /*Facebook Full Ad*/
//    var interstitialAdFb: com.facebook.ads.InterstitialAd? = null
    private var adsCallbackFb: AdsCallback? = null
    fun FacebookbeforeloadFullAd(context: Context) {
//        interstitialAdFb = com.facebook.ads.InterstitialAd(
//            context,
//            Utils.getPref(context, Constant.FB_INTERSTITIAL, "")
//        )
//        try {
//            val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
//                override fun onInterstitialDisplayed(ad: Ad?) {
//                    Log.e("TAG", "Interstitial ad displayed.")
//
//                }
//
//                override fun onInterstitialDismissed(ad: Ad?) {
//                    Log.e("TAG", "Interstitial ad dismissed.")
//                    adsCallbackFb!!.startNextScreenAfterAd()
//                }
//
//                override fun onError(ad: Ad?, adError: com.facebook.ads.AdError) {
//                    //                adsCallbackFb!!.adLoadingFailed()
//                    Log.e("TAG", "onError::::Full Ad Facebook "+adError.errorCode+"  "+adError.errorMessage )
//                }
//
//                override fun onAdLoaded(ad: Ad?) {
//                    Log.e("TAG", "Interstitial ad is loaded and ready to be displayed!")
//                    // Show the ad
//                }
//
//                override fun onAdClicked(ad: Ad?) {
//                    Log.e("TAG", "Interstitial ad clicked!")
//                }
//
//                override fun onLoggingImpression(ad: Ad?) {
//                    Log.e("TAG", "Interstitial ad impression logged!")
//                }
//            }
//
//            interstitialAdFb!!.loadAd(
//                interstitialAdFb!!.buildLoadAdConfig()
//                    .withAdListener(interstitialAdListener)
//                    .build()
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


    }
    fun showInterstitialAdsFacebook(context: Context, adsCallbackFb: AdsCallback?) {

        try {
            if (Debug.DEBUG_IS_HIDE_AD.not() && Utils.isPurchased(context).not()) {
                this.adsCallbackFb = adsCallbackFb
//                if (interstitialAdFb!!.isAdLoaded) {
//                    interstitialAdFb!!.show()
//                    interstitialAdFb = null
//                } else {
//                    adsCallbackFb!!.startNextScreenAfterAd()
//                }
            } else {
                Log.e("TAG", "showInterstitialAdsFacebook:::::ELSE:::  " )
                adsCallbackFb!!.startNextScreenAfterAd()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}