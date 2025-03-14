package fitnessapp.workout.homeworkout.stretching.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fitnessapp.workout.homeworkout.R
import fitnessapp.workout.homeworkout.databinding.ItemWorkoutListBinding
import fitnessapp.workout.homeworkout.stretching.objects.HomeExTableClass
import fitnessapp.workout.homeworkout.stretching.utils.Constant
import fitnessapp.workout.homeworkout.stretching.utils.Utils


class WorkoutListAdapter(internal var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = mutableListOf<HomeExTableClass>()
    internal var mEventListener: EventListener? = null

    fun getItem(pos: Int): HomeExTableClass {
        return data[pos]
    }

    fun addAll(mData: ArrayList<HomeExTableClass>) {
        try {
            data.clear()
            data.addAll(mData)

        } catch (e: Exception) {
            Utils.sendExceptionReport(e)
        }

        notifyDataSetChanged()
    }

    fun add(item: HomeExTableClass) {

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
        val rowSideMenuBinding = DataBindingUtil.inflate<ItemWorkoutListBinding>(
            inflater,
            R.layout.item_workout_list, parent, false
        )

        return MyViewHolder(rowSideMenuBinding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val holder = viewHolder as MyViewHolder
        val item = getItem(position)


//        Log.d("value002",item.exVideoPath.toString())
        holder.rowSideMenuBinding.tvName.text = item.exName

        if (item.exUnit.equals(Constant.workout_type_step)) {
            holder.rowSideMenuBinding.tvTime.text = "X ${item.exTime}"
        } else {
//            holder.rowSideMenuBinding.tvTime.text = Utils.secToString(item.exTime!!.toInt(),Constant.WORKOUT_TIME_FORMAT)
            holder.rowSideMenuBinding.tvTime.text = item.exTime
        }

//        holder.rowSideMenuBinding.container.setOnClickListener {
//            if (mEventListener != null) {
//                mEventListener!!.onItemClick(position, holder.rowSideMenuBinding.root)
//            }
//        }


        holder.rowSideMenuBinding.viewMore.setOnClickListener {
            if (mEventListener != null) {
                mEventListener!!.onItemClick(position, holder.rowSideMenuBinding.root)
            }
        }

        holder.rowSideMenuBinding.viewFlipper.removeAllViews()
//        Log.d("checkVideoPath",item.exPath.toString())
        val listImg: ArrayList<String>? =
            Utils.ReplaceSpacialCharacters(item.exPath!!)?.let {

                Utils.getAssetItems(context, it) }




        Glide.with(context).load(
            Utils.videoURIList.get(position)
            ).into(holder.rowSideMenuBinding.videoPreviews)

        if(item.exVideoPath!=null){

//            Log.d("checkVideoPath",item.exVideoPath.toString())
            val listVideo: ArrayList<String>? =
                Utils.ReplaceSpacialCharacters(item.exVideoPath!!)?.let { Utils.getAssetVideoItems(context, it) }

            if (listVideo != null) {
                Log.d("chekValue006",listVideo.size.toString())
            }

            if (listVideo != null) {
                for (i in 0 until listVideo.size) {
                    val imgview = ImageView(context)
                    //            Glide.with(mContext).load("//android_asset/burpee/".plus(i.toString()).plus(".png")).into(imgview)
//                    listVideo.get(i).toUri()
                    Log.d("checkURi", Uri.parse(listVideo.get(i)).toString())
                    Log.d("checkURi",Uri.parse(
                        "android.resource://" + context.packageName + "/"
                                + R.raw.jumpingjack
                    ).toString())
//                    Glide.with(context).load(
//                        Uri.parse(
//                            "android.resource://" + context.packageName + "/"
//                                    + R.raw.jumpingjack
//                        )).into(holder.rowSideMenuBinding.videoPreviews)
                    holder.rowSideMenuBinding.videoView.setVideoURI(Uri.parse(
                            "android.resource://" + context.packageName + "/"
                                    + R.raw.jumpingjack
                        ))
                    holder.rowSideMenuBinding.videoView.start()
//                    Glide.with(context).load(Uri.parse(listVideo.get(i))).into(holder.rowSideMenuBinding.videoPreviews)
//                    imgview.layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT
//                    )
//                    holder.rowSideMenuBinding.viewFlipper.addView(imgview)
                }
            }
        }


        if (listImg != null) {
            for (i in 0 until listImg.size) {
                val imgview = ImageView(context)
                //            Glide.with(mContext).load("//android_asset/burpee/".plus(i.toString()).plus(".png")).into(imgview)
                Glide.with(context).load(listImg.get(i)).into(imgview)
                imgview.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                holder.rowSideMenuBinding.viewFlipper.addView(imgview)
            }
        }

        holder.rowSideMenuBinding.viewFlipper.isAutoStart = true
        holder.rowSideMenuBinding.viewFlipper.setFlipInterval(context.resources.getInteger(R.integer.viewfliper_animation))
        holder.rowSideMenuBinding.viewFlipper.startFlipping()

    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(internal var rowSideMenuBinding: ItemWorkoutListBinding) :
        RecyclerView.ViewHolder(rowSideMenuBinding.root)

    interface EventListener {
        fun onItemClick(position: Int, view: View)
    }

    fun setEventListener(eventListener: EventListener) {
        this.mEventListener = eventListener
    }


}
