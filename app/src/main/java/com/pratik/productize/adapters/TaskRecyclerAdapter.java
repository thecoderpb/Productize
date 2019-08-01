package com.pratik.productize.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.productize.R;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.RecyclerViewClickListener;

import java.util.List;


public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.MyViewHolder> {

    private List<Tasks> tasksList;
    private Context context;
    private RecyclerViewClickListener itemClickListener;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView reminderText,durationText,locationText;
        private ImageView deleteTaskImage,editTaskImage,locationTagImage;
        private View view;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = itemView.findViewById(R.id.reminderCardView);
            durationText = itemView.findViewById(R.id.durationTextCV);
            locationText = itemView.findViewById(R.id.locationTextCV);
            deleteTaskImage = itemView.findViewById(R.id.deleteNotes);
            editTaskImage = itemView.findViewById(R.id.editNote);
            locationTagImage = itemView.findViewById(R.id.locationCVImage);
            view = itemView.findViewById(R.id.card_border);

            editTaskImage.setOnClickListener(this);
            deleteTaskImage.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            itemClickListener.recyclerViewClicked(view,getLayoutPosition());
        }
    }


    public TaskRecyclerAdapter(Context context,RecyclerViewClickListener itemListener){

        this.context = context;
        this.itemClickListener = itemListener;
    }


    @NonNull
    @Override
    public TaskRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        if(tasksList!=null){
            Tasks currentTask = tasksList.get(position);
            holder.reminderText.setText(currentTask.getTaskText());
            holder.durationText.setText(String.valueOf(currentTask.getDuration()));
            holder.locationText.setText(convertTagToText(currentTask.getTags()));
            holder.locationTagImage.setImageResource(getLocationTagImage(currentTask.getTags()));
            holder.view.setBackgroundResource(getColorResource(currentTask.getPriority()));

        }
    }

    private int getColorResource(int priority) {

        switch (priority){
            case 6 : return R.drawable.card_border_orange;

            case 7 : return R.drawable.card_border_red;

            default: return R.drawable.card_border;
        }

    }

    @Override
    public int getItemCount() {

        if(tasksList != null)
            return tasksList.size();
        else
            return 0;

    }

    public void setTasksList(List<Tasks> tasksList) {
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    public Tasks getTaskAtPosition(int position){
        return tasksList.get(position);
    }

    private int getLocationTagImage(int tag){

        int resId;

        switch (tag){
            case -1 :
                resId = R.drawable.ic_other_fill_small;
                break;
            case 0 :
                resId = R.drawable.ic_home_fill_small;
                break;
            case 1 :
                resId = R.drawable.ic_work_fill_small;
                break;
            default: resId = R.drawable.ic_nav_other_hollow;
        }

        return resId;
    }

    private String convertTagToText(int tag){

        if( tag == 0){
            return "Home";
        }else if( tag == 1){
            return " Work";
        }

        return "Others";

    }

}
