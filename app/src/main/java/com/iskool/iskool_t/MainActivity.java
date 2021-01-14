package com.iskool.iskool_t;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.iskool.iskool_t.Activities.CoursesActivity;
import com.iskool.iskool_t.Activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getUid()!=null)
        {
            startActivity(new Intent(MainActivity.this, CoursesActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}
