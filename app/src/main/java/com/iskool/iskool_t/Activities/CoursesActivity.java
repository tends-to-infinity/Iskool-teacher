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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Fragment.ListFragment;
import com.iskool.iskool_t.Models.Teachers;
import com.iskool.iskool_t.R;

public class CoursesActivity extends AppCompatActivity {

    Teachers teachers;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid!=null)
        {
            FirebaseFirestore.getInstance().collection("TEACHERS").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    teachers = documentSnapshot.toObject(Teachers.class);

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
                        ListFragment listFragment = new ListFragment(teachers.getCourses());

                        fragmentTransaction.replace(R.id.TopicFragContainer,listFragment,null);
                        fragmentTransaction.commit();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CoursesActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        recreate();
    }
}
