package com.iskool.iskool_t.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iskool.iskool_t.Adapter.ModelListAdapter;
import com.iskool.iskool_t.Models.ChapterModel;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.Models.SubjectModel;
import com.iskool.iskool_t.Models.TopicModel;
import com.iskool.iskool_t.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicListFrag extends Fragment {

    ModelClass modelClass;
    ArrayList<ModelClass> modelClasses;

    CourseModel courseModel;
    SubjectModel subjectModel;
    ChapterModel chapterModel;

    public TopicListFrag(ChapterModel chapterModel) {
        this.chapterModel = chapterModel;
    }

    public TopicListFrag(SubjectModel subjectModel) {
        this.subjectModel = subjectModel;
    }

    public TopicListFrag(CourseModel courseModel) {
        this.courseModel = courseModel;
    }

    public TopicListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (courseModel!=null)
        {

            modelClasses = courseModel.getSubjects();
            modelClass= courseModel.getSelf();
        }
        else if (subjectModel!=null)
        {

            modelClasses=subjectModel.getChapters();
            modelClass=subjectModel.getSelf();
        }
        else if (chapterModel!=null)
        {
            modelClasses=chapterModel.getTopics();
            modelClass=chapterModel.getSelf();
        }

        if (modelClasses==null)
        {
            modelClasses= new ArrayList<>();
            Toast.makeText(getActivity(), "Khali", Toast.LENGTH_SHORT).show();
        }
        return inflater.inflate(R.layout.fragment_topic_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button addNew = view.findViewById(R.id.btnaddNew);
        RecyclerView rvTopicList = view.findViewById(R.id.rvTopicList);
        if (modelClasses!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            ModelListAdapter modelListAdapter = new ModelListAdapter(modelClasses,R.layout.subject_chapters_view_rv,fragmentTransaction);
            rvTopicList.setAdapter(modelListAdapter);
            rvTopicList.setLayoutManager(manager);
        }

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFun();

            }
        });
    }


    private void addFun() {

        final AppCompatDialog appCompatDialog = new AppCompatDialog(getActivity());
        appCompatDialog.show();
        appCompatDialog.setContentView(R.layout.addview);
        appCompatDialog.setTitle("Add New "+modelClass.getReff().getParent().getId());
        Button btnadd= appCompatDialog.findViewById(R.id.btnSubmit);
        final EditText etName=appCompatDialog.findViewById(R.id.etName);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                if (modelClass.getReff().getParent().getId().equalsIgnoreCase("COURSES"))
                {
                    DocumentReference sreff = FirebaseFirestore.getInstance().collection("SUBJECTS").document();
                    final ModelClass nModel = new ModelClass(etName.getText().toString(),sreff);
                    final SubjectModel subjectModel = new SubjectModel(nModel,modelClass);
                    sreff.set(subjectModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            appCompatDialog.dismiss();
                            modelClasses.add(nModel);
                            courseModel.setSubjects(modelClasses);
                            courseModel.getSelf().getReff().set(courseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else if (modelClass.getReff().getParent().getId().equalsIgnoreCase("SUBJECTS"))
                {
                    DocumentReference sreff = FirebaseFirestore.getInstance().collection("CHAPTERS").document();
                    final ModelClass nModel = new ModelClass(etName.getText().toString(),sreff);
                    ChapterModel chapterModel = new ChapterModel(nModel,modelClass);
                    sreff.set(chapterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            appCompatDialog.dismiss();
                            modelClasses.add(nModel);
                            subjectModel.setChapters(modelClasses);
                            subjectModel.getSelf().getReff().set(subjectModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (modelClass.getReff().getParent().getId().equalsIgnoreCase("CHAPTERS"))
                {
                    DocumentReference sreff = FirebaseFirestore.getInstance().collection("TOPICS").document();
                    final ModelClass nModel = new ModelClass(etName.getText().toString(),sreff);
                    TopicModel topicModel = new TopicModel(nModel,modelClass);
                    sreff.set(topicModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            appCompatDialog.dismiss();
                            modelClasses.add(nModel);
                            chapterModel.setTopics(modelClasses);
                            chapterModel.getSelf().getReff().set(chapterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


    }
}
