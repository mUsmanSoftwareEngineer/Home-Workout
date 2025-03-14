package fitnessapp.workout.homeworkout.stretching.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ItemWeekDayBinding
import fitnessapp.workout.homeworkout.stretching.RestDayActivity
import fitnessapp.workout.homeworkout.stretching.db.DataHelper
import fitnessapp.workout.homeworkout.stretching.objects.PWeekDayData
import fitnessapp.workout.homeworkout.stretching.objects.PWeeklyDayData
import fitnessapp.workout.homeworkout.stretching.utils.Utils



//class WeekDaysAdapter(internal var context: Context,
//                      val arrWeeklyDayStatus: ArrayList<PWeeklyDayData>,
//                      val pos1: Int,
//                      val workoutPlanData: HomePlanTableClass) :
//    RecyclerView.Adapter<WeekDaysAdapter.MyViewHolder>() {
//
//    private val data = mutableListOf<PWeekDayData>()
//    internal var mEventListener: EventListener? = null
//    var continueDataPos:Int? = null
//
//
//
//
//    fun getItem(pos: Int): PWeekDayData {
//        return data[pos]
//    }
//
//    fun addAll(mData: ArrayList<PWeekDayData>) {
//        try {
//            data.clear()
//            Log.d("populateDataList",mData.size.toString())
//            data.addAll(mData)
//
//        } catch (e: Exception) {
//            Utils.sendExceptionReport(e)
//        }
//        notifyDataSetChanged()
//    }
//
//    fun add(item: PWeekDayData) {
//
//        try {
//            this.data.add(item)
//
//        } catch (e: Exception) {
//            Utils.sendExceptionReport(e)
//        }
//
//        notifyDataSetChanged()
//    }
//
//
//    fun clear() {
//        data.clear()
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val rowSideMenuBinding = DataBindingUtil.inflate<ItemWeekDayBinding>(
//            inflater,
//            R.layout.item_week_day, parent, false
//        )
//        rowSideMenuBinding.root.layoutParams.width = parent.measuredWidth / 4
//
//        return MyViewHolder(rowSideMenuBinding)
//    }
//
//    private var flagPrevDay = true
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        val item = getItem(position)
//
//
//        Log.d("checkDaysName",data.get(position).Day_name.toString())
//        holder.rowSideMenuBinding.tvDate.text = item.Day_name.replace("0", "")
//
//        if(item.Day_name == "Cup")
//        {
//            holder.rowSideMenuBinding.tvDate.tag = "n"
//
//            holder.rowSideMenuBinding.imgCup.visibility = View.VISIBLE
//            holder.rowSideMenuBinding.llDay.visibility = View.GONE
//
//            if (item.Is_completed == "1") {
//                holder.rowSideMenuBinding.imgCup.setImageResource(R.drawable.ic_week_winner_cup)
//            } else {
//                holder.rowSideMenuBinding.imgCup.setImageResource(R.drawable.ic_week_winner_cup_gray)
//            }
//        }else{
//            holder.rowSideMenuBinding.tvDate.tag = "y"
//            if (flagPrevDay && pos1 != 0) {
//                flagPrevDay = arrWeeklyDayStatus[pos1 - 1].Is_completed == "1"
//            }
//
//            if (arrWeeklyDayStatus[pos1].Is_completed == "1") {
//                holder.rowSideMenuBinding.imgCompleted.visibility = View.VISIBLE
//            holder.rowSideMenuBinding.llDay.visibility = View.GONE
//
//            } else {
//                when {
//                    item.Is_completed == "1" -> {
//                        holder.rowSideMenuBinding.imgCompleted.visibility = View.VISIBLE
//                        holder.rowSideMenuBinding.llDay.visibility = View.GONE
//                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
//
//                        flagPrevDay = true
//                    }
//                    flagPrevDay -> {
////                            holder.imgIndicator.setImageResource(R.drawable.ic_week_arrow_next_gray)
//                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
//                        holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE
//                        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
//
//                        holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.green_text))
//                        holder.rowSideMenuBinding.llDay.background = ContextCompat.getDrawable(context, R.drawable.bg_circle_dotted_border_green)
//                        if (mEventListener != null) {
//                            mEventListener!!.onSetContinueItemPos(position)
//                        }
//                        flagPrevDay = false
//                    }
//                    else -> {
////                            holder.imgIndicator.setImageResource(R.drawable.ic_week_arrow_next_gray)
//                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
//                        holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE
//                        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
//
//                        holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.col_999))
//                        holder.rowSideMenuBinding.llDay.background =
//                            ContextCompat.getDrawable(context, R.drawable.bg_circle_border)
//                        holder.rowSideMenuBinding.tvDate.tag = "n"
//
//                        flagPrevDay = false
//                    }
//                }
//            }
//
//        }
//
//        holder.rowSideMenuBinding.container.setOnClickListener {
//            if (mEventListener != null) {
//                try {
//                    if (item.Day_name != "Cup") {
//                        if (holder.rowSideMenuBinding.tvDate.tag == "y") {
//                            mEventListener!!.onItemClick(position, holder.rowSideMenuBinding.root)
//                        } else {
//                            Toast.makeText(context, "Please finish the previous challenge date first.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }
//
//
//
//        if (position == 3 || position == 7) {
//            holder.rowSideMenuBinding.imgArrow.visibility = View.GONE
//        } else {
//            holder.rowSideMenuBinding.imgArrow.visibility = View.VISIBLE
//        }
//
//    }
//
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    inner class MyViewHolder(internal var rowSideMenuBinding: ItemWeekDayBinding) :
//        RecyclerView.ViewHolder(rowSideMenuBinding.root)
//
//    interface EventListener {
//        fun onItemClick(position: Int, view: View)
//        fun onSetContinueItemPos(position: Int)
//    }
//
//    fun setEventListener(eventListener: EventListener) {
//        this.mEventListener = eventListener
//    }
//
//}


class WeekDaysAdapter(
    internal var context: Context,
    val arrWeeklyDayStatus: ArrayList<PWeeklyDayData>,
    val pos1: Int,
) :
    RecyclerView.Adapter<WeekDaysAdapter.MyViewHolder>() {

    private val data = mutableListOf<PWeekDayData>()
    internal var mEventListener: EventListener? = null
    var continueDataPos: Int? = null


    fun getItem(pos: Int): PWeekDayData {
        return data[pos]
    }

    fun addAll(mData: ArrayList<PWeekDayData>) {
        try {
            data.clear()
//            Log.d("populateDataList",mData.size.toString())
            data.addAll(mData)

        } catch (e: Exception) {
            Log.d("exceptionCheck",e.toString())
            Utils.sendExceptionReport(e)
        }
        notifyDataSetChanged()

    }

    fun add(item: PWeekDayData) {

        try {
            this.data.add(item)

        } catch (e: Exception) {
            Utils.sendExceptionReport(e)
        }

        notifyDataSetChanged()
    }


    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rowSideMenuBinding = DataBindingUtil.inflate<ItemWeekDayBinding>(
            inflater,
            R.layout.item_week_day, parent, false
        )
//        rowSideMenuBinding.root.layoutParams.width = parent.measuredWidth / 4

        return MyViewHolder(rowSideMenuBinding)
    }

    private var flagPrevDay = true
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = getItem(position)

//        Log.d("checkProgress",holder.itemView.progressBar.progress.toString())
//        Log.d("checkDayAdapter",item.)

//        holder.rowSideMenuBinding.tvDate.text = item.Day_name.replace("0", ""+" Days")

//        holder.rowSideMenuBinding.progressBar.



        if (item.Day_name == "Cup") {
//            holder.itemView.rel1.visibility = View.GONE
            holder.rowSideMenuBinding.tvDate.tag = "n"



            holder.rowSideMenuBinding.tvDate.text = "Rest Day"

            holder.rowSideMenuBinding.imgCup.visibility = View.VISIBLE
            holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE

//            if (item.Is_completed == "1") {
//                holder.rowSideMenuBinding.imgCup.setImageResource(R.drawable.ic_week_winner_cup)
//            } else {
//                holder.rowSideMenuBinding.imgCup.setImageResource(R.drawable.ic_week_winner_cup_gray)
//            }
        } else {


            Log.d("checkDayName",item.Day_name)
            holder.rowSideMenuBinding.tvDate.text = "Day " + item.Day_name
//            holder.itemView.rel1.visibility = View.VISIBLE

            val progress = DataHelper(context).getProgressByPlanIdDayID(item.Day_id)
//            Log.d(
//                "ProgressInPercentage1",
//                (((((progress).toFloat()) * 100)).toDouble()).toString() + " %"
//            )



            if (progress != "0" && !progress.equals(" ")) {
                Log.d("check1122", "we are in")
//                val progressPercenage = (((((progress).toFloat()) ))).toString() + " %"
//                val progressBarVal = ((((progress).toFloat()))).toInt()
                val progressPercenage = "$progress %"
                val progressBarVal = progress.toInt()
//                holder.itemView.progressBar.progress = progressBarVal
//                holder.itemView.percentText.text = progressPercenage
            }
            else{
                Log.d("check133", "we are out")
//               holder.itemView.progressBar.setProgress(10)
            }


            holder.rowSideMenuBinding.tvDate.tag = "y"
            if (flagPrevDay && pos1 != 0) {
                flagPrevDay = arrWeeklyDayStatus[pos1 - 1].Is_completed == "1"
            }

            if (arrWeeklyDayStatus[pos1].Is_completed == "1") {
//                holder.rowSideMenuBinding.imgCompleted.visibility = View.VISIBLE
                holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE

            } else {
                when {
                    item.Is_completed == "1" -> {
                        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
                        holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE
                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
                        holder.rowSideMenuBinding.insImg.visibility=View.VISIBLE
                        holder.rowSideMenuBinding.progressBar.visibility=View.GONE
                        holder.rowSideMenuBinding.percentText.visibility=View.GONE

                        flagPrevDay = true
                    }
                    flagPrevDay -> {
//                            holder.imgIndicator.setImageResource(R.drawable.ic_week_arrow_next_gray)
                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
                        holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE
                        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
                        holder.rowSideMenuBinding.insImg.visibility=View.GONE

                        holder.rowSideMenuBinding.progressBar.visibility=View.GONE
                        holder.rowSideMenuBinding.percentText.visibility=View.VISIBLE

//                        holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.green_text))
//                        holder.rowSideMenuBinding.llDay.background = ContextCompat.getDrawable(context, R.drawable.bg_circle_dotted_border_green)
                        if (mEventListener != null) {
                            mEventListener!!.onSetContinueItemPos(position)
                        }
                        flagPrevDay = false
                    }
                    else -> {
//                            holder.imgIndicator.setImageResource(R.drawable.ic_week_arrow_next_gray)
                        holder.rowSideMenuBinding.imgCup.visibility = View.GONE
                        holder.rowSideMenuBinding.llDay.visibility = View.VISIBLE
                        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE

                        holder.rowSideMenuBinding.insImg.visibility=View.GONE

                        holder.rowSideMenuBinding.progressBar.visibility=View.GONE
                        holder.rowSideMenuBinding.percentText.visibility=View.VISIBLE

//                        holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.col_999))
//                        holder.rowSideMenuBinding.llDay.background =
//                            ContextCompat.getDrawable(context, R.drawable.bg_circle_border)
                        holder.rowSideMenuBinding.tvDate.tag = "n"

                        flagPrevDay = false
                    }
                }
            }

        }

        holder.rowSideMenuBinding.container.setOnClickListener {
            if (mEventListener != null) {
                try {
                    if (item.Day_name != "Cup") {
                        if (holder.rowSideMenuBinding.tvDate.tag == "y") {
                            mEventListener!!.onItemClick(position, holder.rowSideMenuBinding.root)
                        } else {
                            Toast.makeText(
                                context,
                                "Please finish the previous challenge date first.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else{
                        context.startActivity(Intent(context, RestDayActivity::class.java))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


//        if (position == 3 || position == 7) {
//            holder.rowSideMenuBinding.imgArrow.visibility = View.GONE
//        } else {
//            holder.rowSideMenuBinding.imgArrow.visibility = View.VISIBLE
//        }

    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(internal var rowSideMenuBinding: ItemWeekDayBinding) :
        RecyclerView.ViewHolder(rowSideMenuBinding.root)

    interface EventListener {
        fun onItemClick(position: Int, view: View)
        fun onSetContinueItemPos(position: Int)
    }

    fun setEventListener(eventListener: EventListener) {
        this.mEventListener = eventListener
    }

}