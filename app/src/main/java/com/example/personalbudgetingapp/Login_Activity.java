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
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private EditText email, pass;
    private Button login_bitton;
    private TextView  dnt_have_accoutn;

    private FirebaseAuth auth;
    private ProgressDialog pd;

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email= findViewById(R.id.l_email_id);
        pass= findViewById(R.id.l_password_id);
        login_bitton= findViewById(R.id.l_login_id);
        dnt_have_accoutn= findViewById(R.id.l_dnt_have_Accout_id);

        auth = FirebaseAuth.getInstance();
        pd= new ProgressDialog(this);

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user!=null)
                {
                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };




    }

    public void login(View view) {

        String email_string = email.getText().toString();
        String password_string = pass.getText().toString();

        if (TextUtils.isEmpty(email_string))
        {
            email.setError("Email");
        }
        if (TextUtils.isEmpty(password_string))
        {
            pass.setError("Password");
        }
        else
        {
            pd.setMessage("Logging.........");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            auth.signInWithEmailAndPassword(email_string, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        pd.dismiss();
                    }else
                    {
                        Toast.makeText(Login_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    }
                }
            });
        }

    }

    public void dnt_have_accout(View view) {
        Intent intent = new Intent(Login_Activity.this, Registration_Activity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        auth.removeAuthStateListener(authStateListener);
    }
}