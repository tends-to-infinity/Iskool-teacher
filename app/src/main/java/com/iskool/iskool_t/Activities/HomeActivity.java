package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.R;

public class HomeActivity extends AppCompatActivity  {

    LinearLayout llHomework,llAssignments,llClassroom,llQuiz,llExams;

    CourseModel courseModel;
    String cpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cpath = getIntent().getStringExtra("cpath");
        llAssignments=findViewById(R.id.llAssignments);
        llClassroom=findViewById(R.id.llClassroom);
        llExams=findViewById(R.id.llExams);
        llHomework=findViewById(R.id.llHomework);
        llQuiz=findViewById(R.id.llQuiz);

        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 courseModel = documentSnapshot.toObject(CourseModel.class);

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                started();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void started() {
        llClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ClassroomActivity.class).putExtra("cpath",cpath));
            }
        });
        llAssignments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AssignmentActivity.class).putExtra("cpath",cpath));

            }
        });
        llQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,QuizActivity.class).putExtra("cpath",cpath));
            }
        });
        llExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ExamActivity.class).putExtra("cpath",cpath));

            }
        });
        llHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,HomeworkActivity.class).putExtra("cpath",cpath));

            }
        });
    }

}
