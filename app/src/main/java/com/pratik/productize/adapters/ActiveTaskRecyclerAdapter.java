package com.pratik.productize.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.productize.R;
import com.pratik.productize.asynchronous.AppExecutor;
import com.pratik.productize.database.Tasks;

import com.pratik.productize.database.TasksDB;
import com.pratik.productize.ui.RecyclerViewClickListener;
import com.pratik.productize.utils.Converters;


import java.util.Collections;
import java.util.List;


public class ActiveTaskRecyclerAdapter extends RecyclerView.Adapter<ActiveTaskRecyclerAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private Context context;
    public static List<Tasks> tasksList;
    private RecyclerViewClickListener itemClickListener;
    private MarkItemSwiped itemSwipeListener;
    private Converters converter;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(tasksList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(tasksList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);


    }

    @Override
    public void onItemDismiss(int position) {

        itemSwipeListener.itemSwiped(tasksList.get(position));

        tasksList.remove(position);
        notifyItemRemoved(position);

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reminderText,durationText;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = itemView.findViewById(R.id.currentReminderCardView);
            durationText = itemView.findViewById(R.id.currentDurationTextCV);

        }


        @Override
        public void onClick(View view) {
            itemClickListener.recyclerViewClicked(view,getLayoutPosition());
        }


    }

    public ActiveTaskRecyclerAdapter(Context context,RecyclerViewClickListener itemListener,MarkItemSwiped itemSwipeListener){
        this.context = context;
        this.itemClickListener = itemListener;
        this.itemSwipeListener = itemSwipeListener;

        converter = new Converters();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_layout2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(tasksList!=null){
            Tasks currentTask = tasksList.get(position);
            holder.reminderText.setText(currentTask.getTaskText());
            holder.durationText.setText(String.valueOf(converter.timeLongToMin(currentTask.getDuration())));

        }
    }

    public void setTasksList(List<Tasks> tasksList) {
        ActiveTaskRecyclerAdapter.tasksList = tasksList;
        notifyDataSetChanged();
    }

    public Tasks getTaskAtPosition(int position){
        return tasksList.get(position);
    }

    @Override
    public int getItemCount() {

        if(tasksList != null)
            return tasksList.size();
        else
            return 0;

    }

}
