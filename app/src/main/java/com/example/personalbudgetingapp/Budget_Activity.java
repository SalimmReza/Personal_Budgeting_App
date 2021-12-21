package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.personalbudgetingapp.Models.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        auth = FirebaseAuth.getInstance();
        budget_ref= FirebaseDatabase.getInstance().getReference().child("budget").child(
                auth.getCurrentUser().getUid());
        pd = new ProgressDialog(this);

        fab= findViewById(R.id.b_fab_id);

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

                if (budget_item.equals("Select Items"))
                {
                    Toast.makeText(Budget_Activity.this, "Select any items", Toast.LENGTH_LONG).show();
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

}