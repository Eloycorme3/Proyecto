package com.example.nimesukiapp.view.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nimesukiapp.R;

public class PickerItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView tvItem;

    public PickerItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvItem = itemView.findViewById(R.id.tv_item);
    }
}
