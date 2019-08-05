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


import java.util.Collections;
import java.util.List;


public class ActiveTaskRecyclerAdapter extends RecyclerView.Adapter<ActiveTaskRecyclerAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private Context context;
    public static List<Tasks> tasksList;
    private RecyclerViewClickListener itemClickListener;
    private MarkItemSwiped itemSwipeListener;

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

//        Tasks task = tasksList.get(position);
//        final long id = task.getId();
//        AppExecutor.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                TasksDB db = TasksDB.getInstance(context);
//                db.taskDAO().updateTaskExpiryTrue(id);
//            }
//        });

        itemSwipeListener.itemSwiped(tasksList.get(position));

        tasksList.remove(position);
        notifyItemRemoved(position);


    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reminderText,durationText,locationText;
        private ImageView deleteTaskImage,editTaskImage,locationTagImage;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reminderText = itemView.findViewById(R.id.reminderCardView);
            durationText = itemView.findViewById(R.id.durationTextCV);
            locationText = itemView.findViewById(R.id.locationTextCV);


            deleteTaskImage = itemView.findViewById(R.id.deleteNotes);
            editTaskImage = itemView.findViewById(R.id.editNote);
            locationTagImage = itemView.findViewById(R.id.locationCVImage);

            deleteTaskImage.setOnClickListener(this);
            editTaskImage.setOnClickListener(this);

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

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
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

    private int getLocationTagImage(int tag){

        int resId;

        switch (tag){
            case -1 :
                resId = R.drawable.ic_nav_other_fill;
                break;
            case 0 :
                resId = R.drawable.ic_nav_home_fill;
                break;
            case 1 :
                resId = R.drawable.ic_nav_work_fill;
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
