package com.example.finalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    // Информация в адаптере
    private ArrayList<Post> dataSet;

    // Конструкторы для адаптера
    public PostAdapter(ArrayList<Post> dataSet) {
        this.dataSet = dataSet;
    }

    public PostAdapter() {}

    // Метод для обновления списка новостей
    public void setDataSet(ArrayList<Post> newDataSet) {
        this.dataSet = newDataSet;
        notifyDataSetChanged();
    }

    // Класс ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // View-элементы
        private final TextView day_profit, day_expen,date_post;

        public ViewHolder(View view) {
            super(view);

            // Инициализация View-элементов
            day_profit = view.findViewById(R.id.day_profit);
            day_expen = view.findViewById(R.id.day_expen);
            date_post = view.findViewById(R.id.date_post);
        }

        //геттеры
        public TextView getDay_profit() {
            return day_profit;
        }

        public TextView getDay_expen() {
            return day_expen;
        }

        public TextView getDate_post() {
            return date_post;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newpost, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post currentPost = dataSet.get(position);
        //заполняем поля в постах
        holder.getDay_profit().setText(currentPost.profit);
        holder.getDay_expen().setText(currentPost.expen);
        holder.getDate_post().setText(currentPost.date);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
