package com.example.project32.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project32.AddNewTask;
import com.example.project32.ListingActivity;
import com.example.project32.Model.TodoModel;
import com.example.project32.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<TodoModel> todoList;
    private ListingActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(ListingActivity listingActivity, List<TodoModel>todoList){
        this.todoList = todoList;
        activity = listingActivity;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task ,parent ,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        TodoModel todoModel = todoList.get(position);
        firestore.collection("tasks").document(todoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return activity;
    }

//    public void editTask(int position){
//        TodoModel todoModel = todoList.get(position);
//
//        Bundle bundle = new Bundle();
//        bundle.putString("tasks", todoModel.getTask());
//        bundle.putString("due", todoModel.getDue());
//        bundle.putString("id", todoModel.TaskId);
//
//        AddNewTask addNewTask = new AddNewTask();
//        addNewTask.setArguments(bundle);
//        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());
//
//        notifyItemChanged(position);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        TodoModel todoModel = todoList.get(position);
//        holder.checkBox.setText(todoModel.getTask());
//        holder.dueDateTv.setText(todoModel.getDue());
//        holder.checkBox.setChecked(toBoolean(todoModel.getStatus()));
//
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    firestore.collection("tasks").document(todoModel.TaskId).update("status" ,1);
//                } else {
//                    firestore.collection("tasks").document(todoModel.TaskId).update("status" ,0);
//                }
//            }
//        });
//    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoModel todoModel = todoList.get(position);
        holder.checkBox.setText(todoModel.getTask());
        holder.dueDateTv.setText(todoModel.getDue());
        holder.checkBox.setChecked(toBoolean(todoModel.getStatus()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (todoModel.TaskId != null) {
                firestore.collection("tasks").document(todoModel.TaskId)
                        .update("status", isChecked ? 1 : 0)
                        .addOnSuccessListener(aVoid -> {

                        })
                        .addOnFailureListener(e -> {

                        });
            }
        });
    }

    private Boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dueDateTv;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            dueDateTv = itemView.findViewById(R.id.set_due_date);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
