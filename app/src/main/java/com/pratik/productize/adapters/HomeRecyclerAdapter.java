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
import com.pratik.productize.database.Tasks;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    private List<Tasks> tasksList;
    private Context context;
    private RecyclerViewClickListener itemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView reminderText,durationText,locationText,priorityText,idText;
        private ImageView deleteTaskImage,editTaskImage;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);

            reminderText = itemView.findViewById(R.id.reminderCardView);
            durationText = itemView.findViewById(R.id.durationTextCV);
            locationText = itemView.findViewById(R.id.locationTextCV);
            priorityText = itemView.findViewById(R.id.priorityTextCV);
            idText = itemView.findViewById(R.id.IdTextCV);
            deleteTaskImage = itemView.findViewById(R.id.deleteNotes);
            editTaskImage = itemView.findViewById(R.id.editNote);

            editTaskImage.setOnClickListener(this);
            deleteTaskImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickListener.recyclerViewClicked(view,getLayoutPosition());
        }
    }


    public HomeRecyclerAdapter(Context context,RecyclerViewClickListener itemClickListener){
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(tasksList!=null){
            Tasks currentTask = tasksList.get(position);
            holder.reminderText.setText(currentTask.getTaskText());
            holder.durationText.setText(String.valueOf(currentTask.getDuration()));
            holder.priorityText.setText(String.valueOf(currentTask.getPriority()));
            holder.locationText.setText(String.valueOf(currentTask.getTags()));
            holder.idText.setText(String.valueOf(currentTask.getTimeStamp()));
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


}
