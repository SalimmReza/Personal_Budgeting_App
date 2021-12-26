package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalbudgetingapp.Models.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Budget_Activity extends AppCompatActivity {
    private FloatingActionButton fab;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private DatabaseReference budget_ref;

    private TextView total_budget_textview;
    private RecyclerView recyclerView;


    private String post_key="";
    private String item = "";
    private int amount =0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        auth = FirebaseAuth.getInstance();
        budget_ref= FirebaseDatabase.getInstance().getReference().child("budget").child(
                auth.getCurrentUser().getUid());
        pd = new ProgressDialog(this);

        fab= findViewById(R.id.b_fab_id);

        total_budget_textview=findViewById(R.id.b_total_budget_Amount_text_view_id);
        recyclerView=findViewById(R.id.b_recycler_view_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        budget_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int total_amount=0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                   Data data = dataSnapshot.getValue(Data.class);
                   total_amount+= data.getAmount();
                   String s_total = String.valueOf("Total budget: $" + total_amount);
                    total_budget_textview.setText(s_total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void fab(View view) {
        add_items();
    }

    private void add_items() {
        AlertDialog.Builder my_dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View my_view = inflater.inflate(R.layout.input_layout, null);
        my_dialog.setView(my_view);

        final AlertDialog dialog = my_dialog.create();
        dialog.setCancelable(false);

        final Spinner item_spinner = my_view.findViewById(R.id.items_spinner_id);
        final EditText amount = my_view.findViewById(R.id.item_amount_id);
        final Button save = my_view.findViewById(R.id.item_save_id);
        final Button cancel = my_view.findViewById(R.id.item_cancel_id);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budget_amount = amount.getText().toString();
                String budget_item = item_spinner.getSelectedItem().toString();

                if (budget_item.equals("Select Item"))
                {
                    Toast.makeText(Budget_Activity.this, "Select any items", Toast.LENGTH_LONG).show();
                }
                if (budget_amount.isEmpty())
                {
                    Toast.makeText(Budget_Activity.this, "Amount Can't be Empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    pd.setMessage("Adding Please Wait..");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                    String id = budget_ref.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epochh = new MutableDateTime();
                    epochh.setDate(0);
                    DateTime now = new DateTime();
                    Months months = Months.monthsBetween(epochh, now);

                    Data data = new Data(budget_item, date , id, null , Integer.parseInt(budget_amount), months.getMonths());
                    budget_ref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Budget_Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Budget_Activity.this, task.getException().toString() , Toast.LENGTH_SHORT).show();

                            }
                            pd.dismiss();
                        }
                    });

                }
                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(budget_ref, Data.class).build();

        FirebaseRecyclerAdapter<Data, view_holder> adapter= new FirebaseRecyclerAdapter<Data, view_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull view_holder holder, int position, @NonNull Data model) {

                holder.set_items_amount("Amount: $"+ model.getAmount());
                holder.set_items_date("On: "+ model.getDate());
                holder.set_items_name("Item: "+ model.getItem());

                holder.notes.setVisibility(View.GONE);

                switch(model.getItem())
                {
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.transport);
                        break;

                    case "Food":
                        holder.imageView.setImageResource(R.drawable.food);
                        break;

                    case "House":
                        holder.imageView.setImageResource(R.drawable.home);
                        break;

                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.entertainment);
                        break;

                    case "Education":
                        holder.imageView.setImageResource(R.drawable.education);
                        break;

                    case "Charity":
                        holder.imageView.setImageResource(R.drawable.charity);
                        break;

                    case "Health":
                        holder.imageView.setImageResource(R.drawable.health);
                        break;

                    case "Personal":
                        holder.imageView.setImageResource(R.drawable.personal);
                        break;

                    case "Others":
                        holder.imageView.setImageResource(R.drawable.other);
                        break;
                }

                            holder.m_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    post_key=getRef(position).getKey();
                                    item=model.getItem();
                                    amount = model.getAmount();
                                    update_data();
                                }
                            });

            }

            @NonNull
            @Override
            public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrive_layout, parent, false);

                return new view_holder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    public class view_holder extends RecyclerView.ViewHolder{

        View m_view;
        public ImageView imageView;
        public TextView notes , date, amount, item;

        public view_holder(@NonNull View itemView) {
            super(itemView);

            m_view = itemView;
            imageView= itemView.findViewById(R.id.rl_image_view_id);
            notes= itemView.findViewById(R.id.rl_note_id);
            /*date= itemView.findViewById(R.id.rl_date_id);
            amount= itemView.findViewById(R.id.rl_amount_id);
            item= itemView.findViewById(R.id.rl_item_id);*/

        }

        public void set_items_name(String item_name)
        {
            TextView item = m_view.findViewById(R.id.rl_item_id);
            item.setText(item_name);
        }

        public void set_items_amount(String item_amount)
        {
            TextView amount = m_view.findViewById(R.id.rl_amount_id);
            amount.setText(item_amount);
        }

        public void set_items_date(String item_date)
        {
            TextView date = m_view.findViewById(R.id.rl_date_id);
            date.setText(item_date);
        }


    }

    private void  update_data()
    {
        AlertDialog.Builder my_dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View m_view= inflater.inflate(R.layout.update_layout, null);

        my_dialog.setView(m_view);
        final AlertDialog dialog = my_dialog.create();


        final TextView m_item = m_view.findViewById(R.id.u_item_name_id);
        final EditText m_amount = m_view.findViewById(R.id.u_amount_id);
        final EditText m_notes = m_view.findViewById(R.id.u_notes_id);

        m_notes.setVisibility(View.GONE);
        m_item.setText(item);

        m_amount.setText(String.valueOf("$" +amount));
        m_amount.setSelection(String.valueOf(amount).length());

        Button delete_btn = m_view.findViewById(R.id.u_delete_id);
        Button update_btn = m_view.findViewById(R.id.u_update_id);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount= Integer.parseInt(m_amount.getText().toString());


                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epochh = new MutableDateTime();
                epochh.setDate(0);
                DateTime now = new DateTime();
                Months months = Months.monthsBetween(epochh, now);

                Data data = new Data(item, date , post_key, null , amount, months.getMonths());
                budget_ref.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Budget_Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Budget_Activity.this, task.getException().toString() , Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                dialog.dismiss();
            }
        });


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                budget_ref.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Budget_Activity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Budget_Activity.this, task.getException().toString() , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();

    }
}