package fitnessapp.workout.homeworkout.stretching

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityMyTrainningBinding
import fitnessapp.workout.homeworkout.stretching.adapter.MyTrainingAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import fitnessapp.workout.homeworkout.view.CEditTextView


class MyTrainingActivity : BaseActivity(), CallbackListener {

    var binding: ActivityMyTrainningBinding? = null
    var myTrainingAdapter: MyTrainingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_trainning)

        initTopBar(binding!!.topbar)
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

        Log.e("TAG", "onCreate:Is Purchase::::::: "+Utils.isPurchased(this) )
        if (Utils.isPurchased(this)) {
            binding!!.llAdView.visibility=View.GONE
            binding!!.llAdViewFacebook.visibility = View.GONE
        }
    }

    private fun init() {
        binding!!.topbar.isMenuShow = true
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.topbar.tvTitleText.text = getString(R.string.menu_my_training)
        binding!!.handler = ClickHandler()

        myTrainingAdapter = MyTrainingAdapter(this)
        binding!!.rvMyTraining.layoutManager = LinearLayoutManager(this)
        binding!!.rvMyTraining.setAdapter(myTrainingAdapter)

        myTrainingAdapter!!.setEventListener(object : MyTrainingAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = myTrainingAdapter!!.getItem(position)
                val i = Intent(this@MyTrainingActivity, MyTrainingExcListActivity::class.java)
                i.putExtra("workoutPlanData", Gson().toJson(item))
                startActivity(i)

            }

            override fun onMoreClick(position: Int, view: View) {
                val item = myTrainingAdapter!!.getItem(position)
                showPopupMenu(view, item)
            }

        })

        fillData()
    }

    fun fillData() {
        myTrainingAdapter!!.addAll(dbHelper.getHomePlanList(Constant.PlanTypeMyTraining))

        if (myTrainingAdapter!!.itemCount > 0) {
            binding!!.llPlaceHolder.visibility = View.GONE
            binding!!.imgAddNew.visibility = View.VISIBLE
        } else {
            binding!!.llPlaceHolder.visibility = View.VISIBLE
            binding!!.imgAddNew.visibility = View.GONE
        }
    }

    private fun showPopupMenu(
        view: View,
        planDetail: HomePlanTableClass
    ) {
        val menu = PopupMenu(this@MyTrainingActivity, view)

        menu.getMenu().add(getString(R.string.rename))
        val s = SpannableString(getString(R.string.delete))
        s.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.md_red_500)),
            0,
            s.length,
            0
        )
        menu.getMenu().add(s)

        menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.title is SpannableString) {
                    showDeleteConfirmationDialog(planDetail)
                } else {
                    showTrainingPlanNameDialog(planDetail)
                }
                return true
            }

        })

        menu.show()
    }

    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
        fillData()
        changeSelection(2)
    }

    private fun showDeleteConfirmationDialog(planDetail: HomePlanTableClass) {
        val builder = AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
        builder.setCancelable(false)
        builder.setMessage(getString(R.string.delete_confirmation_msg))
        builder.setPositiveButton(R.string.delete) { dialog, which ->
            dbHelper.deleteMyTrainingPlan(planDetail.planId!!)
            fillData()
        }
        builder.setNegativeButton(R.string.btn_cancel, { dialog, which -> dialog.dismiss() })
        builder.create().show()
    }

    private fun showTrainingPlanNameDialog(item: HomePlanTableClass) {
        val builder = AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
        builder.setCancelable(false)
        builder.setTitle(R.string.please_name_your_plan)

        val dialogLayout = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        val editText = dialogLayout.findViewById<CEditTextView>(R.id.editText)

        editText.setText(item!!.planName)

        builder.setView(dialogLayout)

        builder.setPositiveButton(R.string.btn_ok) { dialog, which ->
            dbHelper.updateMyTrainingPlanName(item.planId!!, editText.text.toString())
            fillData()
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.btn_cancel, { dialog, which -> dialog.dismiss() })
        builder.create().show()
    }


    inner class TopClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getActivity(), view!!)

            /*if (value.equals(getString(R.string.menu))) {

            }*/

        }
    }

    inner class ClickHandler {

        fun onAddNewClick() {
            val i = Intent(this@MyTrainingActivity, AddExerciseActivity::class.java)
            i.putExtra("from_new_training", false)
            startActivity(i)
        }


    }

    override fun onBackPressed() {
        try {
            if (result != null && result!!.isDrawerOpen) {
                hideMenu(true)
            } else {
                super.onBackPressed()
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

}
