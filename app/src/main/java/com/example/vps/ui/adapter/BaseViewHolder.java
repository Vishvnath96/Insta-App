package com.example.vps.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public void onBind(int position){
        mCurrentPosition = position;
    }

    public int getCurrentPosition(){
        return mCurrentPosition;
    }

}
