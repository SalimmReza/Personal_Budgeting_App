package com.example.personalbudgetingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalbudgetingapp.Models.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Today_items_adapter extends RecyclerView.Adapter<Today_items_adapter.ViewHolder>{

    private Context context;
    private RecyclerView recyclerView;
    private List<Data> my_data_list;


    private String post_key="";
    private String item = "";
    private int amount =0;
    private String note="";


    public Today_items_adapter(Context context, List<Data> my_data_list) {
        this.context = context;
        this.my_data_list = my_data_list;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(context).inflate(R.layout.retrive_layout, parent , false);
        return new Today_items_adapter.ViewHolder(view);
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
//to update the items
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_key=data.getId();
                item=data.getItem();
                amount = data.getAmount();
                note= data.getNotes();
                update_data();
            }
        });



    }

    private void  update_data()
    {
        AlertDialog.Builder my_dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View m_view= inflater.inflate(R.layout.update_layout, null);

        my_dialog.setView(m_view);
        final AlertDialog dialog = my_dialog.create();


        final TextView m_item = m_view.findViewById(R.id.u_item_name_id);
        final EditText m_amount = m_view.findViewById(R.id.u_amount_id);
        final EditText m_notes = m_view.findViewById(R.id.u_notes_id);


        m_item.setText(item);
        m_amount.setText(String.valueOf(amount));
        m_amount.setSelection(String.valueOf(amount).length());


        m_notes.setText(note);
        m_notes.setSelection(note.length());



        Button delete_btn = m_view.findViewById(R.id.u_delete_id);
        Button update_btn = m_view.findViewById(R.id.u_update_id);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount= Integer.parseInt(m_amount.getText().toString());
                note= m_notes.getText().toString();


                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epochh = new MutableDateTime();
                epochh.setDate(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epochh, now);
                Months months = Months.monthsBetween(epochh, now);

                Data data = new Data(item, date , post_key, note , amount, months.getMonths(), weeks.getWeeks());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, task.getException().toString() , Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                dialog.dismiss();
            }
        });


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                reference.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, task.getException().toString() , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();

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
