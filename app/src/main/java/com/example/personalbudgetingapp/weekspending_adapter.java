package com.example.personalbudgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalbudgetingapp.Models.Data;

import java.util.List;

public class weekspending_adapter extends RecyclerView.Adapter<weekspending_adapter.ViewHolder>{
    private Context context;
    private List<Data> my_data_list;

    public weekspending_adapter(Context context, List<Data> my_data_list) {
        this.context = context;
        this.my_data_list = my_data_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrive_layout, parent , false);
        return new weekspending_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = my_data_list.get(position);

        holder.item.setText("Item: "+data.getItem());
        holder.amount.setText("Amount: $"+data.getAmount());
        holder.date.setText("On: "+data.getDate());
        holder.notes.setText("Note: "+data.getNotes());

        switch(data.getItem())
        {
            case "Transport":
                holder.image_view.setImageResource(R.drawable.transport);
                break;

            case "Food":
                holder.image_view.setImageResource(R.drawable.food);
                break;

            case "House":
                holder.image_view.setImageResource(R.drawable.home);
                break;

            case "Entertainment":
                holder.image_view.setImageResource(R.drawable.entertainment);
                break;

            case "Education":
                holder.image_view.setImageResource(R.drawable.education);
                break;

            case "Charity":
                holder.image_view.setImageResource(R.drawable.charity);
                break;

            case "Health":
                holder.image_view.setImageResource(R.drawable.health);
                break;

            case "Personal":
                holder.image_view.setImageResource(R.drawable.personal);
                break;

            case "Others":
                holder.image_view.setImageResource(R.drawable.other);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return my_data_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item, amount, date, notes;
        public ImageView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item= itemView.findViewById(R.id.rl_item_id);
            amount= itemView.findViewById(R.id.rl_amount_id);
            date= itemView.findViewById(R.id.rl_date_id);
            notes= itemView.findViewById(R.id.rl_note_id);
            image_view= itemView.findViewById(R.id.rl_image_view_id);

        }
    }

}



