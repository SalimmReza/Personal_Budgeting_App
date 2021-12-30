package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.personalbudgetingapp.Models.Data;
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
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekSoendingActivity extends AppCompatActivity {

    private TextView totalAmount;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog pd;

    private weekspending_adapter weekspending_adapter;
    private List<Data> my_data_list;


    private FirebaseAuth auth;
    private DatabaseReference express_Ref;
    private String on_line_user_id = "";

    //month work
    private String type ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_soending);

        totalAmount = findViewById(R.id.w_total_budget_Amount_text_view_id);
        progressBar = findViewById(R.id.w_progressbar_id);
        recyclerView = findViewById(R.id.w_recycler_view_id);

        pd= new ProgressDialog(this);


        auth = FirebaseAuth.getInstance();
        on_line_user_id= auth.getCurrentUser().getUid();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        my_data_list= new ArrayList<>();
        weekspending_adapter= new weekspending_adapter(WeekSoendingActivity.this , my_data_list);
        recyclerView.setAdapter(weekspending_adapter);

        if (getIntent().getExtras()!=null)
        {
            type= getIntent().getStringExtra("type");
            if (type.equals("week"))
            {
                read_weeks_spending_items();
            }
            else if (type.equals("month"))
            {
                read_month_spending_items();
            }
        }


        



    }

    private void read_month_spending_items() {

        MutableDateTime epochh = new MutableDateTime();
        epochh.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epochh, now);

        express_Ref= FirebaseDatabase.getInstance().getReference("expenses").child(on_line_user_id);

        Query query = express_Ref.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                my_data_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Data data = dataSnapshot.getValue(Data.class);
                    my_data_list.add(data);
                }

                weekspending_adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int total_amount = 0;
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int p_total = Integer.parseInt(String.valueOf(total));
                    total_amount += p_total;

                    totalAmount.setText("Month spending : $"+total_amount);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void read_weeks_spending_items() {

        MutableDateTime epochh = new MutableDateTime();
        epochh.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epochh, now);

        express_Ref= FirebaseDatabase.getInstance().getReference("expenses").child(on_line_user_id);

        Query query = express_Ref.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            my_data_list.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                Data data = dataSnapshot.getValue(Data.class);
                my_data_list.add(data);
            }

            weekspending_adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

                int total_amount = 0;
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int p_total = Integer.parseInt(String.valueOf(total));
                    total_amount += p_total;

                    totalAmount.setText("Week spending : $"+total_amount);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}