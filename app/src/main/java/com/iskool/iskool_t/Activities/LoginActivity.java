package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iskool.iskool_t.R;

public class LoginActivity extends AppCompatActivity {


    EditText etUserName ,etPassword;
    Button btnLogin;
    String email,pass;
    TextView ltos;
    LinearLayout  llLogin;
    ProgressDialog progressDoalog;
    void  startProgress()
    {
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.show();

        progressDoalog.setContentView(R.layout.progress);
        progressDoalog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDoalog.setCanceledOnTouchOutside(false);

    }
    void  stopProgress()
    {
        progressDoalog.dismiss();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intitailise();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgress();
                login();
            }
        });

    }

    private void login() {

        email = etUserName.getText().toString();
        pass = etPassword.getText().toString();
        if (!(email.equals("")) && !(pass.equals(""))) {
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                stopProgress();
                                Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(LoginActivity.this, CoursesActivity.class));

                            } else {
                                stopProgress();
                                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", e.getMessage());
                            Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void intitailise() {
        etUserName =findViewById(R.id.etLoginEmail);
        etPassword=findViewById(R.id.etLoginPassword);
        btnLogin=findViewById(R.id.btnLoginLogin);

        ltos=findViewById(R.id.tvLoginSignup);
        llLogin=findViewById(R.id.llLoginLogin);
    }
}
