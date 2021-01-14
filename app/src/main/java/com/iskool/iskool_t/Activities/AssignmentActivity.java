package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.iskool.iskool_t.Fragment.ListFragment;
import com.iskool.iskool_t.Fragment.PdfViewFragment;
import com.iskool.iskool_t.Fragment.TopicDialog;
import com.iskool.iskool_t.Models.AssignmentModel;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.R;

import java.util.ArrayList;

public class AssignmentActivity extends AppCompatActivity implements  TopicDialog.ExampleDialogListener {
    String cpath;
    CourseModel courseModel;
    TopicDialog topicDialog;
    Uri uri;
    LinearLayout llAddass;
    FrameLayout frameLayout;
    String aname;
    Button btnUp;
    ModelClass topic;
    int stage = 0;
    ProgressDialog progressDoalog;
    void  startProgress(Context context)
    {
        progressDoalog = new ProgressDialog(context);
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        llAddass= findViewById(R.id.llAddAss);
        cpath=getIntent().getStringExtra("cpath");
        frameLayout= findViewById(R.id.TopicFragContainer);
        btnUp = findViewById(R.id.btnAddAss);
        final EditText etAssName = findViewById(R.id.etAssName);
        Button upPdf, upAss;
        final Button btnAddTopic= findViewById(R.id.btnAddTopic);
        upAss=findViewById(R.id.btnAssDone);
        upAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etAssName.getText().toString();
                if (!(name.equalsIgnoreCase(""))&&uri!=null)
                {
                    aname=name;
                    uploadAss(savedInstanceState);
                }
                else
                {
                    if ((name.equalsIgnoreCase("")))
                    {
                        Toast.makeText(AssignmentActivity.this, "name Gadbad", Toast.LENGTH_SHORT).show();

                    }
                    else if (uri==null)
                    {
                        Toast.makeText(AssignmentActivity.this, "uri Gadbad", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        upPdf=findViewById(R.id.btnAddPdf);
        upPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPdf();

            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stage = 1;
                btnUp.setVisibility(View.GONE);
                llAddass.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                btnAddTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        opoenDialog();
                    }
                });


            }
        });
        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                showlist(savedInstanceState);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AssignmentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showlist(Bundle savedInstanceState) {
        stage =0;
        frameLayout.setVisibility(View.VISIBLE);
        llAddass.setVisibility(View.GONE);


        if (findViewById(R.id.TopicFragContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListFragment listFragment = new ListFragment(courseModel.getAssignments());

            fragmentTransaction.replace(R.id.TopicFragContainer,listFragment,null);
            fragmentTransaction.commit();
        }
    }

    private void uploadAss(final Bundle savedInstanceState) {

        startProgress(this);

        final DocumentReference aReff =FirebaseFirestore.getInstance().collection("ASSIGNMENTS").document();
        FirebaseStorage.getInstance().getReference(aReff.getPath()).child("file").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                final AssignmentModel assignmentModel = new AssignmentModel();
                assignmentModel.setSelf(new ModelClass(aname,aReff));
                assignmentModel.setLink(task.getResult().getStorage().toString());
                assignmentModel.setTopicReq(topic);
                aReff.set(assignmentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (courseModel.getAssignments()==null)
                        {
                            courseModel.setAssignments(new ArrayList<ModelClass>());
                        }
                        courseModel.getAssignments().add(assignmentModel.getSelf());
                        courseModel.getSelf().getReff().set(courseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AssignmentActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                stopProgress();
                                showlist(savedInstanceState);
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }


    private void uploadPdf() {


        if (uri==null)
        {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"SELECT PDF"),1);

        }
        else
        {
            openPdf(uri);
        }




    }

    private void openPdf(Uri uuri) {
        stage=2;
        llAddass.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        if (findViewById(R.id.TopicFragContainer) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PdfViewFragment listFragment = new PdfViewFragment(uuri);

            fragmentTransaction.replace(R.id.TopicFragContainer,listFragment,null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode== Activity.RESULT_OK&&data!=null&&data.getData()!=null)
        {

            uri = data.getData();
            openPdf(data.getData());


        }
    }

    private void opoenDialog() {
        topicDialog = new TopicDialog(courseModel.getSubjects(),1);
        topicDialog.show(getSupportFragmentManager(),"Topic Dialog");
    }


    @Override
    public void applyTexts(ModelClass topicModel,int i) {

        topic = topicModel;


    }
    @Override
    public void onBackPressed() {
        if (stage==2)
        {
            frameLayout.setVisibility(View.GONE);
            llAddass.setVisibility(View.VISIBLE);
        }
        else
        {
            super.onBackPressed();
        }


    }
}
