package com.example.nimesukiapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nimesukiapp.R;

import java.util.ArrayList;
public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.PickerViewHolder> {

    private ArrayList<String> data = new ArrayList<>();
    private int selectedItem = -1;
    private Context context;

    public PickerAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<String> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void setSelectedItem(int position) {
        int oldPosition = selectedItem;
        selectedItem = position;
        if (oldPosition != -1) notifyItemChanged(oldPosition);
        notifyItemChanged(selectedItem);
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    @NonNull
    @Override
    public PickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_view_slider_item, parent, false);
        return new PickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickerViewHolder holder, int position) {
        holder.tvItem.setText(data.get(position));

        if (position == selectedItem) {
            holder.tvItem.setTextColor(ContextCompat.getColor(context, R.color.pastelAccent));
        } else {
            holder.tvItem.setTextColor(ContextCompat.getColor(context, R.color.pastelGray));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class PickerViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public PickerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item);
        }
    }
}
