package fitnessapp.workout.homeworkout.stretching

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fitnessapp.workout.homeworkout.swipedragrecyclerview.OnDragListener
import fitnessapp.workout.homeworkout.swipedragrecyclerview.OnSwipeListener
import fitnessapp.workout.homeworkout.swipedragrecyclerview.RecyclerHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ActivityMyTrainingExcListBinding
import fitnessapp.workout.homeworkout.stretching.adapter.MyTrainingExcAdapter
import fitnessapp.workout.homeworkout.stretching.adapter.NewTrainingAdapter
import fitnessapp.workout.homeworkout.stretching.interfaces.AdsCallback
import fitnessapp.workout.homeworkout.stretching.interfaces.CallbackListener
import fitnessapp.workout.homeworkout.stretching.interfaces.TopBarClickListener
import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.objects.MyTrainingCatExTableClass
import fitnessapp.workout.homeworkout.stretching.utils.AdUtils
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import java.util.ArrayList


class MyTrainingExcListActivity : BaseActivity(), CallbackListener, AdsCallback {

    var binding: ActivityMyTrainingExcListBinding? = null
    var myTrainingExcAdapter: MyTrainingExcAdapter? = null
    var editExcAdapter: NewTrainingAdapter? = null
    var workoutPlanData: HomePlanTableClass? = null
    lateinit var touchHelper: RecyclerHelper<*>
    val removedExList = arrayListOf<MyTrainingCatExTableClass>()
    var isNeedToSave = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_training_exc_list)

        initIntentParam()
        initDrawerMenu(true)
        init()
    }

    private fun initIntentParam() {
        try {
            if (intent.extras != null) {
                if (intent.extras!!.containsKey("workoutPlanData")) {
                    val str = intent.getStringExtra("workoutPlanData")
                    workoutPlanData = Gson().fromJson(
                        str,
                        object : TypeToken<HomePlanTableClass>() {}.type
                    )!!
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        binding!!.topbar.isBackShow = true
        binding!!.topbar.tvTitleText.text = workoutPlanData!!.planName
        binding!!.topbar.topBarClickListener = TopClickListener()
        binding!!.handler = ClickHandler()

        myTrainingExcAdapter = MyTrainingExcAdapter(this)
        binding!!.rvExercise.layoutManager = LinearLayoutManager(this)
        binding!!.rvExercise.setAdapter(myTrainingExcAdapter)

        myTrainingExcAdapter!!.setEventListener(object : MyTrainingExcAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {

            }

            override fun onCloseClick(position: Int, view: View) {

            }
        })

        editExcAdapter = NewTrainingAdapter(this)
        binding!!.rvExerciseEdit.layoutManager = LinearLayoutManager(this)
        binding!!.rvExerciseEdit.setAdapter(editExcAdapter)

        editExcAdapter!!.setEventListener(object : NewTrainingAdapter.EventListener {
            override fun onItemClick(position: Int, view: View) {
                val item = editExcAdapter!!.getItem(position)
                val i =
                    Intent(this@MyTrainingExcListActivity, AddExerciseDetailActivity::class.java)
                i.putExtra("ExDetail", Gson().toJson(item))
                i.putExtra("pos", position)
                startActivityForResult(i, 7466)

            }

            override fun onCloseClick(position: Int, view: View) {
                val item = editExcAdapter!!.getItem(position)
                if (item.isNew.not()) {
                    removedExList.add(item)
                }
                editExcAdapter!!.removeAt(position)
            }
        })


        getExerciseData()
        initTouchHelper()

    }

    var arryList = arrayListOf<MyTrainingCatExTableClass>()
    private fun getExerciseData() {
        arryList = dbHelper.getMyTrainingExListForEdit(workoutPlanData!!.planId!!)
        myTrainingExcAdapter!!.addAll(dbHelper.getMyTrainingExList(workoutPlanData!!.planId!!))
        editExcAdapter!!.addAll(arryList)
    }


    private fun initTouchHelper() {

        touchHelper = RecyclerHelper<MyTrainingCatExTableClass>(
            editExcAdapter!!.data as ArrayList<MyTrainingCatExTableClass>,
            editExcAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        )
        touchHelper.setRecyclerItemDragEnabled(true)
            .setOnDragItemListener(object : OnDragListener {
                override fun onDragItemListener(fromPosition: Int, toPosition: Int) {
                    editExcAdapter!!.onChangePosition(fromPosition, toPosition)
                    isNeedToSave = true
                }
            })

        touchHelper.setRecyclerItemSwipeEnabled(true)
            .setOnSwipeItemListener(object : OnSwipeListener {
                override fun onSwipeItemListener() {

                }
            })
        val itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper.attachToRecyclerView(binding!!.rvExerciseEdit)

    }

    override fun onResume() {
        openInternetDialog(this,false)
        super.onResume()
    }

    var adClickCount: Int = 1
    inner class ClickHandler {

        fun onAddNewClick() {
            val i = Intent(this@MyTrainingExcListActivity, AddExerciseActivity::class.java)
            i.putExtra("from_new_training", true)
            startActivityForResult(i, 7489)
        }

        fun onStartClick() {



            if (Utils.getPref(this@MyTrainingExcListActivity, Constant.START_BTN_COUNT, 1) == 1) {
                if (Utils.getPref(
                        this@MyTrainingExcListActivity,
                        Constant.STATUS_ENABLE_DISABLE,
                        ""
                    ) == Constant.ENABLE
                ) {
                    when (Utils.getPref(
                        this@MyTrainingExcListActivity,
                        Constant.AD_TYPE_FB_GOOGLE,
                        ""
                    )) {
                        Constant.AD_GOOGLE -> {
                            AdUtils.showInterstitialAdsGoogle(
                                this@MyTrainingExcListActivity,
                                this@MyTrainingExcListActivity
                            )
                        }
                        Constant.AD_FACEBOOK -> {
                            AdUtils.showInterstitialAdsFacebook(
                                this@MyTrainingExcListActivity,
                                this@MyTrainingExcListActivity
                            )
                        }
                        else -> {
                            startExerciseActivity()
                        }
                    }
                    Utils.setPref(this@MyTrainingExcListActivity, Constant.START_BTN_COUNT, 0)
                } else {
                    startExerciseActivity()
                }
            } else {
                if (adClickCount == 1) {
                    Utils.setPref(this@MyTrainingExcListActivity, Constant.START_BTN_COUNT, 1)
                }
                startExerciseActivity()
            }


        }

        fun onEditClick() {
            binding!!.tvEdit.visibility = View.GONE
            binding!!.tvSave.visibility = View.VISIBLE
            binding!!.btnStart.visibility = View.GONE
            binding!!.imgAddNew.visibility = View.VISIBLE
            binding!!.rvExerciseEdit.visibility = View.VISIBLE
            binding!!.rvExercise.visibility = View.GONE
        }

        fun onSaveClick() {
            saveExcList()
            binding!!.tvEdit.visibility = View.VISIBLE
            binding!!.tvSave.visibility = View.GONE
            binding!!.btnStart.visibility = View.VISIBLE
            binding!!.imgAddNew.visibility = View.GONE
            binding!!.rvExerciseEdit.visibility = View.GONE
            binding!!.rvExercise.visibility = View.VISIBLE
            getExerciseData()
        }
    }

    fun startExerciseActivity(){
        val intent = Intent(this@MyTrainingExcListActivity, PerformWorkOutActivity::class.java)
        intent.putExtra("workoutPlanData", Gson().toJson(workoutPlanData))
        intent.putExtra("ExcList", Gson().toJson(myTrainingExcAdapter!!.data))
        startActivity(intent)
    }

    inner class TopClickListener : TopBarClickListener {
        override fun onTopBarClickListener(view: View?, value: String?) {
            Utils.hideKeyBoard(getActivity(), view!!)

            if (value.equals(getString(R.string.back))) {
                if (isNeedToSave)
                    showSaveConfirmationDialog()
                else
                    finish()
            }

        }
    }


    override fun onBackPressed() {
        if (isNeedToSave)
            showSaveConfirmationDialog()
        else
            super.onBackPressed()
    }

    private fun showSaveConfirmationDialog() {
        val builder = AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle)
        builder.setCancelable(false)
        builder.setMessage(getString(R.string.save_changes_que))
        builder.setPositiveButton(R.string.save) { dialog, which ->
            saveExcList()
            finish()
        }
        builder.setNegativeButton(R.string.btn_cancel, { dialog, which ->
            dialog.dismiss()
            finish()
        })
        builder.create().show()
    }

    fun saveExcList() {

        if (removedExList.isNullOrEmpty().not()) {
            for (item in removedExList) {
                dbHelper.deleteMyTrainingEx(item.catExId!!)
            }
        }

        for (i in editExcAdapter!!.data.indices) {
            val item = editExcAdapter!!.data[i] as MyTrainingCatExTableClass
            if (item.catExId.isNullOrEmpty().not()) {
                dbHelper.updateMyTrainingEx(item.catExId!!.toInt(), item, i + 1)
            }

            if (item.isNew) {
                dbHelper.addMyTrainingExc(item, workoutPlanData!!.planId!!.toInt(), i)
            }
        }

        dbHelper.updateMyTrainingPlanExcCount(
            editExcAdapter!!.data.size,
            workoutPlanData!!.planId!!.toInt()
        )
        isNeedToSave = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 7466 || requestCode == 7489) && resultCode == Activity.RESULT_OK) {
            try {
                var exData: MyTrainingCatExTableClass
                if (data != null) {
                    if (data.extras!!.containsKey("workoutPlanData")) {
                        val str = data.getStringExtra("workoutPlanData")
                        exData = Gson().fromJson(
                            str,
                            object : TypeToken<MyTrainingCatExTableClass>() {}.type
                        )!!
                        if (data.extras!!.containsKey("pos")) {
                            val pos = data.getIntExtra("pos", -1)
                            updateItem(pos, exData)

                        } else {
                            addItem(exData)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun updateItem(pos: Int, exData: MyTrainingCatExTableClass) {
        exData.isUpdated = true
        editExcAdapter!!.update(pos, exData)
        isNeedToSave = true
        //editExcAdapter!!.addAll(touchHelper.list as ArrayList<MyTrainingCatExTableClass>)
    }

    private fun addItem(exData: MyTrainingCatExTableClass) {
        exData.isNew = true
        editExcAdapter!!.add(exData!!)
        isNeedToSave = true
        // editExcAdapter!!.addAll(touchHelper.list as ArrayList<MyTrainingCatExTableClass>)
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

    override fun startNextScreenAfterAd() {
        startExerciseActivity()
    }
}
