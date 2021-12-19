package com.example.personalbudgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration_Activity extends AppCompatActivity {
    private EditText email, pass;
    private Button register_button;
    private TextView have_account;

    private FirebaseAuth auth;
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        email= findViewById(R.id.r_email_id);
        pass= findViewById(R.id.r_password_id);
        register_button= findViewById(R.id.r_register_id);
        have_account= findViewById(R.id.r_have_Accout_id);

        auth = FirebaseAuth.getInstance();
        pd= new ProgressDialog(this);
    }

    public void register(View view) {

        String email_string = email.getText().toString();
        String password_string = pass.getText().toString();

        if (TextUtils.isEmpty(email_string))
        {
            email.setError("Email");
        }
        if (TextUtils.isEmpty(password_string))
        {
            pass.setError("Password");
        }else
        {
            pd.setMessage("Registering........");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            auth.createUserWithEmailAndPassword(email_string, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(Registration_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        pd.dismiss();
                    }else
                    {
                        Toast.makeText(Registration_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });
        }

    }

    public void have_account(View view) {

        Intent intent = new Intent(Registration_Activity.this, Login_Activity.class);
        startActivity(intent);
    }


}