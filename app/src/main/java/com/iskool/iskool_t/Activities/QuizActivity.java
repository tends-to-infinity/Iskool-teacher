package com.iskool.iskool_t.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Fragment.ListFragment;
import com.iskool.iskool_t.Fragment.TopicDialog;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.Models.QuizModel;
import com.iskool.iskool_t.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity implements TopicDialog.ExampleDialogListener {

    private CourseModel courseModel;
    TopicDialog topicDialog;
    ModelClass topic;
    int stage = 0;
    private QuizModel quizListModel = new QuizModel();
    private ArrayList<String> ques = new ArrayList<>();
    private ArrayList<Integer> anss = new ArrayList<>();
    private EditText etques, etop1, etop2, etop3, etop4, etQuizName;
    private CheckBox c1, c2, c3, c4;
    int ans = 0;
    int tans = 0;
    private Button btnAddmore, btnPrev, btnAdd, btn, next, btnAddQuiz, btnAddTopic;
    private String tQues;
    private ArrayList<String> tOpt = new ArrayList<>();
    private DocumentReference areff;
    private HashMap<String, ArrayList<String>> opt = new HashMap<>();
    private int pos = 0;
    FrameLayout frameLayout;
    LinearLayout lladdQuiz;
    ProgressDialog progressDoalog;

    void startProgress(Context context) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.show();

        progressDoalog.setContentView(R.layout.progress);
        progressDoalog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDoalog.setCanceledOnTouchOutside(false);

    }

    void stopProgress() {
        progressDoalog.dismiss();

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        String cpath = getIntent().getStringExtra("cpath");
        btnAddQuiz = findViewById(R.id.btnAddQuiz);
        frameLayout = findViewById(R.id.TopicFragContainer);
        lladdQuiz = findViewById(R.id.llAddQUiz);
        etques = findViewById(R.id.etQues);
        etop1 = findViewById(R.id.etOp1);
        etop2 = findViewById(R.id.etOp2);
        etop3 = findViewById(R.id.etOp3);
        etop4 = findViewById(R.id.etOp4);
        btnAdd = findViewById(R.id.btnAdd);
        btnAddmore = findViewById(R.id.btnAddmore);
        btnPrev = findViewById(R.id.btnPrev);
        next = findViewById(R.id.btnNext);
        c1 = findViewById(R.id.checkBox);
        c2 = findViewById(R.id.checkBox1);
        c3 = findViewById(R.id.checkBox2);
        c4 = findViewById(R.id.checkBox3);
        etQuizName = findViewById(R.id.etQuizName);

        c1.setChecked(true);
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton == c1 && b) {
                    c4.setChecked(false);
                    c3.setChecked(false);
                    c2.setChecked(false);
                    ans = 0;
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton == c2 && b) {
                    c4.setChecked(false);
                    c3.setChecked(false);
                    c1.setChecked(false);
                    ans = 1;
                }
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton == c4 && b) {
                    c3.setChecked(false);
                    c1.setChecked(false);
                    c2.setChecked(false);
                    ans = 3;
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton == c3 && b) {
                    c4.setChecked(false);
                    c1.setChecked(false);
                    c2.setChecked(false);
                    ans = 2;
                }
            }
        });
        btnAddTopic = findViewById(R.id.btnAddTopic);

        FirebaseFirestore.getInstance().document(cpath).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                courseModel = documentSnapshot.toObject(CourseModel.class);
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                btnAddQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newQuiz();
                    }
                });

                showlist();
            }
        });


    }

    private void opoenDialog() {
        topicDialog = new TopicDialog(courseModel.getSubjects(),1);
        topicDialog.show(getSupportFragmentManager(), "Topic Dialog");
    }


    @Override
    public void applyTexts(ModelClass topicModel,int i) {

        topic = topicModel;
        Toast.makeText(this, "" + topicModel.getName(), Toast.LENGTH_SHORT).show();

    }

    private void showlist() {
        stage=0;
        frameLayout.setVisibility(View.VISIBLE);
        lladdQuiz.setVisibility(View.GONE);
        btnAddQuiz.setVisibility(View.VISIBLE);

        if (findViewById(R.id.TopicFragContainer) != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListFragment listFragment = new ListFragment(courseModel.getQuiz());

            fragmentTransaction.replace(R.id.TopicFragContainer, listFragment, null);
            fragmentTransaction.commit();
        }
    }

    private void newQuiz() {
        stage=1;
        frameLayout.setVisibility(View.GONE);
        lladdQuiz.setVisibility(View.VISIBLE);
        btnAddQuiz.setVisibility(View.GONE);
        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opoenDialog();
            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos == 0) {
                    Toast.makeText(QuizActivity.this, "First", Toast.LENGTH_SHORT).show();
                } else {
                    if (pos == ques.size()) {
                        addT();
                    }
                    pos--;
                    puta();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos == opt.size()) {
                    Toast.makeText(QuizActivity.this, "Last", Toast.LENGTH_SHORT).show();
                } else {
                    if (pos == ques.size() - 1) {
                        pos++;
                        putT();
                    } else {
                        pos++;
                        puta();
                    }

                }
            }
        });

        btnAddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String q, o1, o2, o3, o4;
                q = etques.getText().toString();
                o1 = etop1.getText().toString();
                o2 = etop2.getText().toString();
                o3 = etop3.getText().toString();
                o4 = etop4.getText().toString();
                if (!(o1.equals("") || o2.equals("") || o3.equals("") || o4.equals("") || q.equals(""))) {
                    addd();
                    pos++;
                    clearet();
                } else {
                    Toast.makeText(QuizActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q, o1, o2, o3, o4, qname;
                q = etques.getText().toString();
                o1 = etop1.getText().toString();
                o2 = etop2.getText().toString();
                o3 = etop3.getText().toString();
                o4 = etop4.getText().toString();
                qname = etQuizName.getText().toString();
                if (!(o1.equals("") || o2.equals("") || o3.equals("") || o4.equals("") || q.equals(""))) {
                    addd();
                    startProgress(QuizActivity.this);
                    quizListModel.setQues(ques);
                    quizListModel.setOptions(opt);
                    quizListModel.setAnswer(anss);
                    quizListModel.setTopic(topic);
                    areff = FirebaseFirestore.getInstance().collection("QUIZ").document();
                    quizListModel.setSelf(new ModelClass(qname, areff));
                    areff.set(quizListModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (courseModel.getQuiz() == null) {
                                courseModel.setQuiz(new ArrayList<ModelClass>());
                            }
                            courseModel.getQuiz().add(quizListModel.getSelf());
                            courseModel.getSelf().getReff().set(courseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(QuizActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                    stopProgress();
                                    showlist();

                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(QuizActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void putT() {
        etques.setText(tQues);
        etop1.setText(tOpt.get(0));
        etop2.setText(tOpt.get(1));
        etop3.setText(tOpt.get(2));
        etop4.setText(tOpt.get(3));
        setAns(tans);

    }

    private void setAns(int tans) {
        c1.setChecked(false);
        c2.setChecked(false);
        c3.setChecked(false);
        c4.setChecked(false);

        if (tans == 0) {
            c1.setChecked(true);

        } else if (tans == 1) {
            c2.setChecked(true);
        } else if (tans == 2) {
            c3.setChecked(true);
        } else if (tans == 3) {
            c4.setChecked(true);
        }
    }


    private void addT() {
        tQues = etques.getText().toString();
        tOpt.add(etop1.getText().toString());
        tOpt.add(etop2.getText().toString());
        tOpt.add(etop3.getText().toString());
        tOpt.add(etop4.getText().toString());
        tans = ans;
    }

    private void addd() {
        ques.add(etques.getText().toString());
        ArrayList<String> options = new ArrayList<>();
        options.add(etop1.getText().toString());
        options.add(etop2.getText().toString());
        options.add(etop3.getText().toString());
        options.add(etop4.getText().toString());
        opt.put(String.valueOf(ques.size() - 1), options);
        anss.add(ans);
        Log.w("data", ques.toString());
        Log.w("data", opt.toString());
        Log.w("data", anss.toString());
    }

    private void puta() {
        etques.setText(ques.get(pos));
        etop1.setText(opt.get(String.valueOf(pos)).get(0));
        etop2.setText(opt.get(String.valueOf(pos)).get(1));
        etop3.setText(opt.get(String.valueOf(pos)).get(2));
        etop4.setText(opt.get(String.valueOf(pos)).get(3));
        setAns(anss.get(pos));


    }

    private void clearet() {
        etop1.setText("");
        etop2.setText("");
        etop3.setText("");
        etop4.setText("");
        etques.setText("");
        c1.setChecked(true);
        c2.setChecked(false);
        c3.setChecked(false);
        c4.setChecked(false);
        ans = 0;


    }

    @Override
    public void onBackPressed() {
        if (stage==1)
        {
            showlist();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
