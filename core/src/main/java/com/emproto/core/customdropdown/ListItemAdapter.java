package com.emproto.core.customdropdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emproto.core.R;


import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {
    private final Context context;
    private List<String> values;
    private final com.emproto.core.customdropdown.OnItemClickListener onItemClickListener;

    public ListItemAdapter(Context context, List<String> values, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.values = values;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(values.get(position));
        holder.textView.setOnClickListener(v -> onItemClickListener.onItemClicked(values.get(position), position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<String> getValues() {
        return values;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setValues(List<String> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewValue);
        }
    }
}
