package fitnessapp.workout.homeworkout.stretching.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fitnessapp.workout.homeworkout.stretching.db.DataHelper
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ItemHomeChildBinding
import fitnessapp.workout.homeworkout.databinding.ItemHomeParentBinding


import fitnessapp.workout.homeworkout.stretching.objects.HomePlanTableClass
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class HomePlansAdapter(internal var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TITLE = 1
    private val VIEW_DESC = 2

    private val data = mutableListOf<HomePlanTableClass>()
    internal var mEventListener: EventListener? = null

    fun getItem(pos: Int): HomePlanTableClass {
        return data[pos]
    }

    fun addAll(mData: ArrayList<HomePlanTableClass>) {
        try {
            data.clear()
            data.addAll(mData)

        } catch (e: Exception) {
            Utils.sendExceptionReport(e)
        }
        notifyDataSetChanged()
    }

    fun add(item: HomePlanTableClass) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        var viewHolder: RecyclerView.ViewHolder? = null

//        when (viewType) {
//            VIEW_TITLE -> {
//                val itemHomeParentBinding: ItemHomeParentBinding =
//                    DataBindingUtil.inflate<ItemHomeParentBinding>(
//                        inflater,
//                        R.layout.item_home_parent, parent, false
//                    )
//                viewHolder = TitleHolder(itemHomeParentBinding)
//            }
//            VIEW_DESC -> {
//                val itemHomeChildBinding: ItemHomeChildBinding =
//                    DataBindingUtil.inflate<ItemHomeChildBinding>(
//                        inflater,
//                        R.layout.item_home_child, parent, false
//                    )
//                viewHolder = ChildHolder(itemHomeChildBinding)
//            }
//            else -> {
//            }
//        }

        val itemHomeChildBinding: ItemHomeChildBinding =
            DataBindingUtil.inflate<ItemHomeChildBinding>(
                inflater,
                R.layout.item_home_child, parent, false
            )
        viewHolder = ChildHolder(itemHomeChildBinding)

        return viewHolder
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//              //Original Method
//        val inflater = LayoutInflater.from(parent.context)
//        var viewHolder: RecyclerView.ViewHolder? = null
//
//        when (viewType) {
//            VIEW_TITLE -> {
//                val itemHomeParentBinding: ItemHomeParentBinding =
//                    DataBindingUtil.inflate<ItemHomeParentBinding>(
//                        inflater,
//                        R.layout.item_home_parent, parent, false
//                    )
//                viewHolder = TitleHolder(itemHomeParentBinding)
//            }
//            VIEW_DESC -> {
//                val itemHomeChildBinding: ItemHomeChildBinding =
//                    DataBindingUtil.inflate<ItemHomeChildBinding>(
//                        inflater,
//                        R.layout.item_home_child, parent, false
//                    )
//                viewHolder = ChildHolder(itemHomeChildBinding)
//            }
//            else -> {
//            }
//        }
//
//
//        return viewHolder!!
//    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        try {
            val item = getItem(position)
            val holder = viewHolder as ChildHolder

            holder.itemHomeChildBinding.tvTitle.text = item.planName


            holder.itemHomeChildBinding.imgCover.setImageResource(
                Utils.getDrawableId(item.planImage,context)
            )

            if ((item.planMinutes!!.toInt() > 0 || item.planWorkouts!!.toInt() > 0) && item.planText.isNullOrEmpty() && item.planTestDes.isNullOrEmpty()) {
                holder.itemHomeChildBinding.llTimeWorkOut.visibility = View.VISIBLE
                holder.itemHomeChildBinding.tvWorkoutTime.text =
                    "${item.planMinutes} " + context!!.getString(R.string.minutes)
                holder.itemHomeChildBinding.tvTotalWorkout.text =
                    "${item.planWorkouts} " + context!!.getString(R.string.workouts)
                holder.itemHomeChildBinding.tvGo.visibility = View.GONE
            } else {
                holder.itemHomeChildBinding.llTimeWorkOut.visibility = View.GONE
                holder.itemHomeChildBinding.tvGo.visibility = View.VISIBLE
            }

            if (item.planText.isNullOrEmpty().not()) {
                holder.itemHomeChildBinding.tvDes.visibility = View.VISIBLE
                holder.itemHomeChildBinding.tvDes.text = item.planText
            } else {
                holder.itemHomeChildBinding.tvDes.visibility = View.GONE
            }

//            if (item.isPro) {
//                holder.itemHomeChildBinding.imgPro.visibility = View.Visible
//            } else {
//                holder.itemHomeChildBinding.imgPro.visibility = View.GONE
//            }

            if (item.planDays.equals("YES")){
                val days = DataHelper(context).getCompleteDayCountByPlanId(item.planId!!)
                if(days > 0) {
//                    holder.itemHomeChildBinding.tvGo.visibility = View.GONE //old one
                    holder.itemHomeChildBinding.tvGo.visibility = View.VISIBLE //new one
                    holder.itemHomeChildBinding.llDayProgress.visibility = View.VISIBLE
                    Utils.setDayProgressData(context, item.planId!!, holder.itemHomeChildBinding.tvDayLeft, holder.itemHomeChildBinding.tvPer, holder.itemHomeChildBinding.pbDay)
                }else{
                    holder.itemHomeChildBinding.llDayProgress.visibility = View.GONE
                }
            }else {
                holder.itemHomeChildBinding.llDayProgress.visibility = View.GONE
            }

//            holder.itemHomeChildBinding.container.setOnClickListener {
//                if (mEventListener != null) {
//                    mEventListener!!.onItemClick(position, it)
//                }
//            }

            holder.itemHomeChildBinding.tvGo.setOnClickListener {
                if (mEventListener != null) {
                    mEventListener!!.onItemClick(position, it)
                }
            }

//            when (getItemViewType(position)) {
////                VIEW_TITLE -> {
////                    val holder = viewHolder as TitleHolder
////
////                    holder.itemHomeParentBinding.tvName.text = item.planName
////                }
////                VIEW_DESC -> {
////
////
////                }
////                else -> {
////                }
//
//            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

//    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
//          original Fun
//        try {
//            val item = getItem(position)
//
//            when (getItemViewType(position)) {
//                VIEW_TITLE -> {
//                    val holder = viewHolder as TitleHolder
//
//                    holder.itemHomeParentBinding.tvName.text = item.planName
//                }
//                VIEW_DESC -> {
//                    val holder = viewHolder as ChildHolder
//
//                    holder.itemHomeChildBinding.tvTitle.text = item.planName
//                    holder.itemHomeChildBinding.imgCover.setImageResource(
//                        Utils.getDrawableId(item.planImage,context)
//                    )
//
//                    if ((item.planMinutes!!.toInt() > 0 || item.planWorkouts!!.toInt() > 0) && item.planText.isNullOrEmpty() && item.planTestDes.isNullOrEmpty()) {
//                        holder.itemHomeChildBinding.llTimeWorkOut.visibility = View.VISIBLE
//                        holder.itemHomeChildBinding.tvWorkoutTime.text =
//                            "${item.planMinutes} " + context!!.getString(R.string.minutes)
//                        holder.itemHomeChildBinding.tvTotalWorkout.text =
//                            "${item.planWorkouts} " + context!!.getString(R.string.workouts)
//                        holder.itemHomeChildBinding.tvGo.visibility = View.GONE
//                    } else {
//                        holder.itemHomeChildBinding.llTimeWorkOut.visibility = View.GONE
//                        holder.itemHomeChildBinding.tvGo.visibility = View.VISIBLE
//                    }
//
//                    if (item.planText.isNullOrEmpty().not()) {
//                        holder.itemHomeChildBinding.tvDes.visibility = View.VISIBLE
//                        holder.itemHomeChildBinding.tvDes.text = item.planText
//                    } else {
//                        holder.itemHomeChildBinding.tvDes.visibility = View.GONE
//                    }
//
//                    if (item.isPro) {
//                        holder.itemHomeChildBinding.imgPro.visibility = View.VISIBLE
//                    } else {
//                        holder.itemHomeChildBinding.imgPro.visibility = View.GONE
//                    }
//
//                    if (item.planDays.equals("YES")){
//                        val days = DataHelper(context).getCompleteDayCountByPlanId(item.planId!!)
//                        if(days > 0) {
//                            holder.itemHomeChildBinding.tvGo.visibility = View.GONE
//                            holder.itemHomeChildBinding.llDayProgress.visibility = View.VISIBLE
//                            Utils.setDayProgressData(context, item.planId!!, holder.itemHomeChildBinding.tvDayLeft, holder.itemHomeChildBinding.tvPer, holder.itemHomeChildBinding.pbDay)
//                        }else{
//                            holder.itemHomeChildBinding.llDayProgress.visibility = View.GONE
//                        }
//                    }else {
//                        holder.itemHomeChildBinding.llDayProgress.visibility = View.GONE
//                    }
//
//                    holder.itemHomeChildBinding.container.setOnClickListener {
//                        if (mEventListener != null) {
//                            mEventListener!!.onItemClick(position, it)
//                        }
//                    }
//
//                }
//                else -> {
//                }
//            }
//
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//    }



    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {

        try {
            val item = getItem(position)
            return if (item.planLvl.equals(Constant.PlanLvlTitle)) {
                VIEW_TITLE
            } else {
                VIEW_DESC
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    inner class TitleHolder(internal var itemHomeParentBinding: ItemHomeParentBinding) :
        RecyclerView.ViewHolder(itemHomeParentBinding.root)

    inner class ChildHolder(internal var itemHomeChildBinding: ItemHomeChildBinding) :
        RecyclerView.ViewHolder(itemHomeChildBinding.root)

    interface EventListener {
        fun onItemClick(position: Int, view: View)
    }

    fun setEventListener(eventListener: EventListener) {
        this.mEventListener = eventListener
    }


}
