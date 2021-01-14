package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Adapter.ModelListAdapter;
import com.iskool.iskool_t.Fragment.HomeworkViewFragment;
import com.iskool.iskool_t.Fragment.ListFragment;
import com.iskool.iskool_t.Fragment.TopicDialog;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.HomeworkModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.Models.TopicModel;
import com.iskool.iskool_t.R;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeworkActivity extends AppCompatActivity implements TopicDialog.ExampleDialogListener {

    Button btnAddHomework,btnaddTopic,btnaddQuiz,btnaddAssignments,upHW,btnDate;
    LinearLayout llAddHomwrk;
    FrameLayout frameLayout;
    HomeworkModel homeworkModel;
    CourseModel courseModel;
    TopicDialog topicDialog;
    ModelClass topic;
    String dates;
    int stage = 0;
    Calendar calendar1 = Calendar.getInstance();
    DocumentReference hreff;
    ModelListAdapter modelListAdapter,modelListAdapter2,modelListAdapter3;
    RecyclerView rvTOpics,rvQuiz,rvAss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);


        String cpath = getIntent().getStringExtra("cpath");
        btnAddHomework = findViewById(R.id.btnAddHomeWork);
        llAddHomwrk=findViewById(R.id.llAddHomework);
        frameLayout = findViewById(R.id.TopicFragContainer);
        btnaddTopic = findViewById(R.id.btnAddTopic);
        btnaddQuiz = findViewById(R.id.btnAddQuiz);
        btnaddAssignments = findViewById(R.id.btnAddAss);
        rvAss=findViewById(R.id.rvAssignments);
        rvQuiz=findViewById(R.id.rvQuiz);
        upHW=findViewById(R.id.btnupHomework);
        btnDate=findViewById(R.id.btnDate);
        rvTOpics = findViewById(R.id.rvTopics);
        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);
                Calendar calendars = Calendar.getInstance();
                documentSnapshot.getReference().collection("HOMEWORK").document(DateFormat.format("yyyyMMdd", calendars).toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                            homeworkModel = documentSnapshot.toObject(HomeworkModel.class);
                        }
                        else
                        {
                            homeworkModel = new HomeworkModel();
                            Toast.makeText(HomeworkActivity.this, "Nahi h aaj", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        btnAddHomework.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addHomework();
                            }
                        });
                        showList();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeworkActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            }
        });
        upHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(dates==null)&&!((homeworkModel.getQuiz()==null)&&(homeworkModel.getAssignments()==null)&&(homeworkModel.getTopics()==null)))
                {
                    upload();
                }
                else
                {
                    Toast.makeText(HomeworkActivity.this, "Kuchh To dalo", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });





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
                String dateText = DateFormat.format("yyyyMMdd", calendar1).toString();

                btnDate.setText(dateText);
                courseModel.getSelf().getReff().collection("HOMEWORK").document(dateText).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                            homeworkModel = documentSnapshot.toObject(HomeworkModel.class);
                        }
                        else
                        {
                            homeworkModel = new HomeworkModel();
                            Toast.makeText(HomeworkActivity.this, "Nahi h aaj", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       addHomework();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeworkActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                dates=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();



    }


    private void upload() {

        hreff =courseModel.getSelf().getReff().collection("HOMEWORK").document(dates);
        homeworkModel.setSelf(new ModelClass(dates,hreff));
        homeworkModel.setTimestamp(new Timestamp(calendar1.getTime()));
        hreff.set(homeworkModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(HomeworkActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                showList();
            }
        });

    }

    private void showList() {
        llAddHomwrk.setVisibility(View.GONE);
        btnAddHomework.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);

        stage=0;
        if (findViewById(R.id.TopicFragContainer) != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            HomeworkViewFragment listFragment = new HomeworkViewFragment(homeworkModel,courseModel);

            fragmentTransaction.replace(R.id.TopicFragContainer, listFragment, null);
            fragmentTransaction.commit();
        }
    }

    private void addHomework() {
        stage = 1;
        llAddHomwrk.setVisibility(View.VISIBLE);
        btnAddHomework.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        if (homeworkModel.getTopics()==null&&homeworkModel.getAssignments()==null&&homeworkModel.getQuiz()==null)
        {

            Toast.makeText(this, "Sahi Hai", Toast.LENGTH_SHORT).show();
        }
       if (homeworkModel.getTopics()!=null)
       {
           intialise();
       }
       else
       {
           rvTOpics.setAdapter(null);

       }
        if (homeworkModel.getAssignments()!=null)
        {
            intiAss();
        }
        else
        {
            rvAss.setAdapter(null);

        }
        if (homeworkModel.getQuiz()!=null)
        {
            intiQuiz();
        }
        else
        {
            rvQuiz.setAdapter(null);
        }
        if (homeworkModel.getSelf()!=null)
        {
            btnDate.setText(homeworkModel.getSelf().getName());
        }



        btnaddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opoenDialog(1);
            }
        });
       btnaddAssignments.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               opoenDialog(2);
           }
       });
       btnaddQuiz.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               opoenDialog(3);
           }
       });




    }

    private void intialise() {
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter = new ModelListAdapter(homeworkModel.getTopics(),R.layout.subject_chapters_view_rv,null);

        rvTOpics.setAdapter(modelListAdapter);
        rvTOpics.setLayoutManager(manager);
    }

    private void opoenDialog(int i) {
        if (i==1)
        {
            topicDialog = new TopicDialog(courseModel.getSubjects(),i);
        }
        else if (i==2)
        {
            topicDialog = new TopicDialog(courseModel.getAssignments(),i);
        }
        else if (i==3)
        {
            topicDialog = new TopicDialog(courseModel.getQuiz(),i);
        }
        topicDialog.show(getSupportFragmentManager(), "Topic Dialog");
    }


    @Override
    public void applyTexts(ModelClass topicModel,int i) {

        topic = topicModel;
        if (i==1)
        {
            if (homeworkModel.getTopics()==null)
            {
                homeworkModel.setTopics(new ArrayList<ModelClass>());
                intialise();
            }
            homeworkModel.getTopics().add(topicModel);
            modelListAdapter.notifyItemInserted(homeworkModel.getTopics().size()-1);

            Toast.makeText(this, "" + homeworkModel.getTopics(), Toast.LENGTH_SHORT).show();
        }
        else if (i==2)
        {
            if (homeworkModel.getAssignments()==null)
            {
                homeworkModel.setAssignments(new ArrayList<ModelClass>());
                intiAss();
            }
            homeworkModel.getAssignments().add(topicModel);
            modelListAdapter2.notifyItemInserted(homeworkModel.getAssignments().size()-1);

        }
        else if (i==3)
        {
            if (homeworkModel.getQuiz()==null)
            {
                homeworkModel.setQuiz(new ArrayList<ModelClass>());
                intiQuiz();
            }
            homeworkModel.getQuiz().add(topicModel);
            modelListAdapter3.notifyItemInserted(homeworkModel.getQuiz().size()-1);
        }

    }

    private void intiQuiz() {

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter3 = new ModelListAdapter(homeworkModel.getQuiz(),R.layout.subject_chapters_view_rv,null);

        rvQuiz.setAdapter(modelListAdapter3);
        rvQuiz.setLayoutManager(manager);

    }

    private void intiAss() {

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter2 = new ModelListAdapter(homeworkModel.getAssignments(),R.layout.subject_chapters_view_rv,null);

        rvAss.setAdapter(modelListAdapter2);
        rvAss.setLayoutManager(manager);
        rvAss.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        if (stage==0)
        {
            super.onBackPressed();
        }
        else if (stage==1)
        {
            showList();
        }
    }
}
