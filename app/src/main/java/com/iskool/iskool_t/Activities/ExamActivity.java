package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.iskool.iskool_t.Fragment.ListFragment;
import com.iskool.iskool_t.Fragment.PdfViewFragment;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.ExamModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ExamActivity extends AppCompatActivity {
    
    Button btnAddExam,btnAddpdf,btnSetTiem,btnSetDate,btnDone;
    EditText etNAme,etduration;
    FrameLayout frameLayout;
    LinearLayout llAddexam;
    Uri uri;
    int stage=0;
    Calendar calendar1 = Calendar.getInstance();
    String dates,time;
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
    CourseModel courseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        llAddexam=findViewById(R.id.llAddExam);

        btnAddpdf=findViewById(R.id.btnAddQuestionPaper);
        btnAddExam=findViewById(R.id.btnAddExam);
        btnSetTiem=findViewById(R.id.btnSetTime);
        btnSetDate=findViewById(R.id.btnAddDate);
        btnDone=findViewById(R.id.btnDone);
        etduration=findViewById(R.id.etDuration);
        etNAme=findViewById(R.id.etExamName);
        frameLayout=findViewById(R.id.TopicFragContainer);

        String cpath = getIntent().getStringExtra("cpath");
        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                btnAddExam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addExam();
                    }
                });

                showList();

            }
        });
        
        



    }

    private void showList() {
        stage = 0;
        llAddexam.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        btnAddExam.setVisibility(View.VISIBLE);

        if (findViewById(R.id.TopicFragContainer) != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListFragment listFragment = new ListFragment(courseModel.getExams());

            fragmentTransaction.replace(R.id.TopicFragContainer, listFragment, null);
            fragmentTransaction.commit();
        }

    }

    private void addExam() {
        llAddexam.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        btnAddExam.setVisibility(View.GONE);
        
        btnAddpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri==null)
                {
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"SELECT PDF"),1);

                }
                else 
                {
                    showpdf(uri);
                }
            }
        });

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        btnSetTiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etNAme.getText().toString();
                if (!(name.equals("")||etduration.getText().toString().equals("")||dates.equals("")||time.equals("")||uri.equals(null)))
                {
                    int dur = Integer.parseInt(etduration.getText().toString());

                    startProgress(ExamActivity.this);
                    Timestamp timestamp = new Timestamp(calendar1.getTime());
                    final ExamModel examModel = new ExamModel();
                    examModel.setDuration(dur);
                    examModel.setStartTime(timestamp);
                    final DocumentReference ereff= FirebaseFirestore.getInstance().collection("EXAMS").document();
                    examModel.setSelf(new ModelClass(name,ereff));
                    FirebaseStorage.getInstance().getReference(ereff.getPath()).child("QPAPER").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                            examModel.setLink(task.getResult().getStorage().toString());
                            ereff.set(examModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (courseModel.getExams()==null)
                                    {
                                        courseModel.setExams(new ArrayList<ModelClass>());
                                    }
                                    courseModel.getExams().add(examModel.getSelf());
                                    courseModel.getSelf().getReff().set(courseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ExamActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                            stopProgress();
                                            showList();

                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(ExamActivity.this, "Something is missing", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        
    }

    private void uplaod() {




    }

    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i("Demo", "onTimeSet: " + hour + minute);
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                btnSetTiem.setText(dateText);
                time=dateText;
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }


    private void setDate() {

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                btnSetDate.setText(dateText);
                dates=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode== Activity.RESULT_OK&&data!=null&&data.getData()!=null)
        {

            uri = data.getData();
            btnAddpdf.setText(uri.getLastPathSegment());
            showpdf(data.getData());
        }
    }

    private void showpdf(Uri uri) {
        stage=2;
        llAddexam.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        if (findViewById(R.id.TopicFragContainer) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PdfViewFragment listFragment = new PdfViewFragment(uri);

            fragmentTransaction.replace(R.id.TopicFragContainer,listFragment,null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (stage==2)
        {
            frameLayout.setVisibility(View.GONE);
            llAddexam.setVisibility(View.VISIBLE);
        }
        else
        {
            super.onBackPressed();
        }

    }
}
