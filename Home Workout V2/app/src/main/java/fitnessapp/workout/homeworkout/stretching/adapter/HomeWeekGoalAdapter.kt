package fitnessapp.workout.homeworkout.stretching.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fitnessapp.workout.homeworkout.stretching.db.DataHelper
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ItemHomeWeekGoalBinding
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils
import java.util.*
import kotlin.collections.ArrayList


class HomeWeekGoalAdapter(internal var context: Context) :
    RecyclerView.Adapter<HomeWeekGoalAdapter.MyViewHolder>() {

    private val data = Utils.getCurrentWeekByFirstDay(context)
    val dbHelper = DataHelper(context)
    internal var mEventListener: EventListener? = null


    fun getItem(pos: Int): String {
        return data[pos]
    }

    fun addAll(mData: ArrayList<String>) {
        try {
            data.clear()
            data.addAll(mData)

        } catch (e: Exception) {
            Utils.sendExceptionReport(e)
        }
        notifyDataSetChanged()
    }

    fun add(item: String) {

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
        val rowSideMenuBinding = DataBindingUtil.inflate<ItemHomeWeekGoalBinding>(
            inflater,
            R.layout.item_home_week_goal, parent, false
        )
        rowSideMenuBinding.root.layoutParams.width = parent.measuredWidth / 7
        rowSideMenuBinding.root.layoutParams.height = parent.measuredWidth / 7
        return MyViewHolder(rowSideMenuBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        val date = Utils.parseTime(item, Constant.CapDateFormatDisplay)
        val currDate = Utils.parseTime(Date(), Constant.CapDateFormatDisplay)
        holder.rowSideMenuBinding.tvDate.text =
            Utils.parseTime(item, Constant.CapDateFormatDisplay, "d")

        if (date == currDate) {
            holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context,R.color.green_text))
        } else {
            holder.rowSideMenuBinding.tvDate.setTextColor(ContextCompat.getColor(context,R.color.gray_text))
        }

        holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
        when {
            dbHelper.isHistoryAvailable(Utils.parseTime(item, Constant.CapDateFormatDisplay,Constant.DATE_FORMAT)) -> {
                holder.rowSideMenuBinding.imgCompleted.visibility = View.VISIBLE
            }
            else -> {
                holder.rowSideMenuBinding.imgCompleted.visibility = View.GONE
            }
        }

        holder.rowSideMenuBinding.container.setOnClickListener {
            if (mEventListener != null) {
                mEventListener!!.onItemClick(position, holder.rowSideMenuBinding.root)
            }
        }

    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(internal var rowSideMenuBinding: ItemHomeWeekGoalBinding) :
        RecyclerView.ViewHolder(rowSideMenuBinding.root)

    interface EventListener {
        fun onItemClick(position: Int, view: View)
    }

    fun setEventListener(eventListener: EventListener) {
        this.mEventListener = eventListener
    }

}
