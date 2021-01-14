package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Fragment.TopicListFrag;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.R;

public class ClassroomActivity extends AppCompatActivity {

    String cpath;
    CourseModel courseModel;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        cpath = getIntent().getStringExtra("cpath");
        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (findViewById(R.id.TopicFragContainer) != null) {
                    if (savedInstanceState != null) {
                        return;
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TopicListFrag listFragment = new TopicListFrag(courseModel);

                    fragmentTransaction.replace(R.id.TopicFragContainer,listFragment,null);
                    fragmentTransaction.commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ClassroomActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
