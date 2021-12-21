package com.example.personalbudgetingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private CardView budgetcard_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetcard_view= findViewById(R.id.m_budget_card_view_id);
    }


    public void budget_card_view(View view) {
        Intent intent = new Intent(MainActivity.this, Budget_Activity.class);
        startActivity(intent);
    }
}