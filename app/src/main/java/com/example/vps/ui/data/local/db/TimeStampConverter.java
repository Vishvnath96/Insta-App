package com.example.vps.ui.data.local.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class TimeStampConverter {

    @TypeConverter
    public static Date fromTimestamp(Long time){
        if(time == null){
            return null;
        }
        else return new Date(time);
    }

    @TypeConverter
    public static long toTimeStamp(Date date){
        return date.getTime();
    }
}
