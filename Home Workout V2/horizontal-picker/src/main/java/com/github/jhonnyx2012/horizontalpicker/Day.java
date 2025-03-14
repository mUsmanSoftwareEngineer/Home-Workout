package com.github.jhonnyx2012.horizontalpicker;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Day {
    private Calendar date;
    private boolean selected;
    private String monthPattern = "MMMM yyyy";

    public Day(Calendar date) {
        this.date = date;
    }

    public String getDay() {
        return String.valueOf(date.get(Calendar.DAY_OF_MONTH));
    }

    public String getWeekDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        return sdf.format(date.getTime()).toUpperCase();
    }

    public String getMonth() {
        return getMonth("");
    }

    public String getMonth(String pattern) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (!pattern.isEmpty())
                this.monthPattern = pattern;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(monthPattern, Locale.getDefault());
        return sdf.format(date.getTime());
    }

    public Calendar getDate() {
        return date;
    }

    public boolean isToday() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR);
    }

    public boolean isFuture() {
        return date.after(Calendar.getInstance());
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
