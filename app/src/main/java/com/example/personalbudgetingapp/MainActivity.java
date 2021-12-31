package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private CardView budget_card_view , today_expense, week_cardview, month_card_view , analytics_cardview, history_card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budget_card_view= findViewById(R.id.m_budget_card_view_id);
        today_expense= findViewById(R.id.m_today_card_view_id);
        week_cardview= findViewById(R.id.m_week_card_view_id);
        month_card_view= findViewById(R.id.m_month_card_view_id);
        analytics_cardview= findViewById(R.id.m_analytics_card_view_id);
        history_card_view= findViewById(R.id.m_history_card_view_id);

    }


    public void budget_card_view(View view) {
        Intent intent = new Intent(MainActivity.this, Budget_Activity.class);
        startActivity(intent);
    }

    public void today_expense(View view) {
        Intent intent = new Intent(MainActivity.this, Today_activity.class);
        startActivity(intent);
    }

    public void week(View view) {
        Intent intent = new Intent(MainActivity.this, WeekSoendingActivity.class);
        intent.putExtra("type", "week");
        startActivity(intent);

    }

    public void month(View view) {
        Intent intent = new Intent(MainActivity.this, WeekSoendingActivity.class);
        intent.putExtra("type", "month");
        startActivity(intent);
    }

    public void analytics(View view) {
        Toast.makeText(MainActivity.this, "IN NEXT UPDATE", Toast.LENGTH_LONG).show();
    }


    public void history(View view) {
        Intent intent = new Intent(MainActivity.this, HISTORY_ACTIVITY.class);

        startActivity(intent);
    }
}