package com.github.jhonnyx2012.horizontalpicker;


import android.app.AlarmManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jhonn on 22/02/2017.
 */

public class HorizontalPickerAdapter extends RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder> {

    private static final long DAY_MILLIS = AlarmManager.INTERVAL_DAY;
    private final int mBackgroundColor;
    private final int mDateSelectedTextColor;
    private final int mDateSelectedColor;
    private final int mTodayDateTextColor;
    private final int mTodayDateBackgroundColor;
    private final int mDayOfWeekTextColor;
    private final int mUnselectedDayTextColor;
    private int itemWidth;
    private final OnItemClickedListener listener;
    private ArrayList<Day> items;
    private Context context;

    public HorizontalPickerAdapter(int itemWidth, OnItemClickedListener listener, Context context, int daysToCreate, int offset, int mBackgroundColor, int mDateSelectedColor, int mDateSelectedTextColor, int mTodayDateTextColor, int mTodayDateBackgroundColor, int mDayOfWeekTextColor, int mUnselectedDayTextColor) {
        items = new ArrayList<>();
        this.itemWidth = itemWidth;
        this.listener = listener;
        this.context = context;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -offset);
        generateDays(daysToCreate, calendar.getTimeInMillis(), false);
        this.mBackgroundColor = mBackgroundColor;
        this.mDateSelectedTextColor = mDateSelectedTextColor;
        this.mDateSelectedColor = mDateSelectedColor;
        this.mTodayDateTextColor = mTodayDateTextColor;
        this.mTodayDateBackgroundColor = mTodayDateBackgroundColor;
        this.mDayOfWeekTextColor = mDayOfWeekTextColor;
        this.mUnselectedDayTextColor = mUnselectedDayTextColor;
    }

    public void generateDays(int n, long initialDate, boolean cleanArray) {
        if (cleanArray)
            items.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initialDate);

        for (int i = 0; i < n; i++) {
            items.add(new Day((Calendar) calendar.clone())); // Clone to avoid modifying previous entries
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_day, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day item = getItem(position);
        holder.tvDay.setText(item.getDay());
        holder.tvWeekDay.setText(item.getWeekDay());
        holder.tvWeekDay.setTextColor(mDayOfWeekTextColor);
        if (item.isSelected()) {
            //holder.tvDay.setBackground(getDaySelectedBackground(holder.itemView));
            holder.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            holder.tvWeekDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor));
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor));
        } /*else if (item.isToday()) {
            //holder.tvDay.setBackground(getDayTodayBackground(holder.itemView));
            //holder.tvDay.setTextColor(mTodayDateTextColor);
            holder.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            holder.tvWeekDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor));
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor));
        } */ else if (item.isFuture()) {
            //holder.tvDay.setBackgroundColor(mBackgroundColor);
            holder.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.tvWeekDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.futureColor));
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(context, R.color.futureColor));
        } else {
           // holder.tvDay.setBackgroundColor(mBackgroundColor);
           // holder.tvDay.setTextColor(mUnselectedDayTextColor);
            holder.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.tvWeekDay.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
            holder.tvWeekDay.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor));
        }
    }

    private Drawable getDaySelectedBackground(View view) {
        Drawable drawable = view.getResources().getDrawable(R.drawable.background_day_selected);
        DrawableCompat.setTint(drawable, mDateSelectedColor);
        return drawable;
    }

    private Drawable getDayTodayBackground(View view) {
        Drawable drawable = view.getResources().getDrawable(R.drawable.background_day_today);
        if (mTodayDateBackgroundColor != -1)
            DrawableCompat.setTint(drawable, mTodayDateBackgroundColor);
        return drawable;
    }

    public Day getItem(int position) {
        return items.get(position);
    }

    public Day getSelectedItem() {
        for (Day item:items) {
            if(item.isSelected())
            {
                return item;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay, tvWeekDay;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvDay.setWidth(itemWidth);
            tvWeekDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Day item = getItem(getAdapterPosition());
            if (!item.isFuture())
                listener.onClickView(v, getAdapterPosition());
        }
    }
}