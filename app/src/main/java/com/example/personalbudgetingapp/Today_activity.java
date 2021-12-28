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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.personalbudgetingapp.Models.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Today_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView totalAmount;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog pd;


    private FirebaseAuth auth;
    private DatabaseReference express_Ref;
    private String on_line_user_id = "";

    private Today_items_adapter today_items_adapter;
    private List<Data> my_data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        toolbar = findViewById(R.id.t_toollbar_id);
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today Spending");*/


        totalAmount = findViewById(R.id.t_total_budget_Amount_text_view_id);
        progressBar = findViewById(R.id.t_progressbar_id);
        recyclerView = findViewById(R.id.t_recycler_view_id);
        fab = findViewById(R.id.t_fab_id);

        pd= new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        on_line_user_id= auth.getCurrentUser().getUid();
        express_Ref= FirebaseDatabase.getInstance().getReference("expenses").child(on_line_user_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        my_data_list= new ArrayList<>();
        today_items_adapter= new Today_items_adapter(Today_activity.this , my_data_list);
        recyclerView.setAdapter(today_items_adapter);

        read_items();


    }

    private void read_items() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(on_line_user_id);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_data_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Data data = dataSnapshot.getValue(Data.class);
                    my_data_list.add(data);

                }

                today_items_adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int total_amount = 0;
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int p_total = Integer.parseInt(String.valueOf(total));
                    total_amount += p_total;

                    totalAmount.setText("Today spending : $"+total_amount);
                            
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void fab(View view) {
        add_item_spent();
    }

    private void add_item_spent() {


            AlertDialog.Builder my_dialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View my_view = inflater.inflate(R.layout.input_layout, null);
            my_dialog.setView(my_view);

            final AlertDialog dialog = my_dialog.create();
            dialog.setCancelable(false);

            final Spinner item_spinner = my_view.findViewById(R.id.items_spinner_id);
            final EditText amount = my_view.findViewById(R.id.item_amount_id);
            final EditText note = my_view.findViewById(R.id.item_notes_id);
            final Button save = my_view.findViewById(R.id.item_save_id);
            final Button cancel = my_view.findViewById(R.id.item_cancel_id);

            note.setVisibility(View.VISIBLE);



            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String expense_amount = amount.getText().toString();
                    String expense_item = item_spinner.getSelectedItem().toString();
                    String expense_note = note.getText().toString();

                    if (expense_item.equals("Select Item"))
                    {
                        Toast.makeText(Today_activity.this, "Select any items", Toast.LENGTH_LONG).show();
                    }
                    if (expense_amount.isEmpty())
                    {
                        Toast.makeText(Today_activity.this, "Amount Can't be Empty", Toast.LENGTH_LONG).show();
                    }
                    if (expense_note.isEmpty())
                    {
                        Toast.makeText(Today_activity.this, "Note Can't be Empty", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        pd.setMessage("Adding Please Wait..");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();

                        String id = express_Ref.push().getKey();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar cal = Calendar.getInstance();
                        String date = dateFormat.format(cal.getTime());

                        MutableDateTime epochh = new MutableDateTime();
                        epochh.setDate(0);
                        DateTime now = new DateTime();
                        Months months = Months.monthsBetween(epochh, now);

                        Data data = new Data(expense_item, date , id, expense_note, Integer.parseInt(expense_amount), months.getMonths());
                        express_Ref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Today_activity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Today_activity.this, task.getException().toString() , Toast.LENGTH_SHORT).show();

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
    }
