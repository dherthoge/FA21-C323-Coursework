package com.example.project3dylanherthoge;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ItemManager itemManager;
    private Context context;
    private String[] titles, dates, priorities;

    public MyAdapter(Context context, String[] titles, String[] dates, String[] priorities, ItemManager itemManager) {
        this.context = context;
        this.titles = titles;
        this.dates = dates;
        this.priorities = priorities;
        this.itemManager = itemManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, itemManager);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(titles[position]);
        holder.date.setText(dates[position]);
        holder.priority.setImageResource(priorities[position] == "HIGH" ? R.drawable.high_priority : R.drawable.low_priority);
        holder.setIndex(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemManager itemManager;
        private TextView title, date;
        private ImageView priority;
        private int index;
        private View itemView;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public MyViewHolder(@NonNull View itemView, ItemManager itemManager) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleTxtView);
            this.date = itemView.findViewById(R.id.dateTxtView);
            this.priority = itemView.findViewById(R.id.priorityImgView);
            this.itemView = itemView;
            this.itemManager = itemManager;
            title.setOnClickListener(this);
            date.setOnClickListener(this);
            priority.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemManager.removeItem(getIndex());
            RecyclerView recyclerView = findV
//            itemView.setVisibility(View.GONE);
        }
    }
}
