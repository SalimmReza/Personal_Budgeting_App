package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Choose_analytics_activity extends AppCompatActivity {

    private CardView daily_analytics , monthly_analytics, weekly_analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytics);

        daily_analytics=findViewById(R.id.a_today_card_view_id);
        monthly_analytics=findViewById(R.id.a_month_card_view_id);
        weekly_analytics=findViewById(R.id.a_week_card_view_id);
    }

    public void today_analytics(View view) {
        Intent intent = new Intent(Choose_analytics_activity.this, Daily_analytics_Activity.class);
        startActivity(intent);
    }

    public void week(View view) {
        Intent intent = new Intent(Choose_analytics_activity.this, Wekly_analytics_activity.class);
        startActivity(intent);
    }

    public void month(View view) {
        Intent intent = new Intent(Choose_analytics_activity.this, Monthly_analytics_Activity.class);
        startActivity(intent);
    }
}