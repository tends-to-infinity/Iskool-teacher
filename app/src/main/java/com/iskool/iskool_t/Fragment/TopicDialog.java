package com.iskool.iskool_t.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.iskool.iskool_t.Adapter.TopicSelectorAdapter;
import com.iskool.iskool_t.Models.ChapterModel;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.Models.SubjectModel;
import com.iskool.iskool_t.R;

import java.util.ArrayList;

public class TopicDialog extends AppCompatDialogFragment {
    Spinner spChpas,spSubs,spTopics;
    private ExampleDialogListener listener;
    ArrayList<ModelClass> subs,chpas,tops;
    ChapterModel chapterModel;
    SubjectModel subjectModel;
    ModelClass topicModel;

    public ModelClass getTopicModel() {
        return topicModel;
    }

    ArrayList<ModelClass> list;
    int it;

    public TopicDialog(ArrayList<ModelClass> list, int it) {
        this.list = list;
        this.it = it;
    }



    Context context;




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.topic_selector_view, null);
        builder.setView(view).setTitle("SELECT Topic").setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (topicModel!=null)
                    listener.applyTexts(topicModel,it);
                else
                {
                    Toast.makeText(context, "Select A Topic", Toast.LENGTH_SHORT).show();
                }
            }
        });
        context=getActivity();

        spChpas=view.findViewById(R.id.spChapter);
        spSubs=view.findViewById(R.id.spSubject);
        spTopics=view.findViewById(R.id.spTopic);
        subs=list;
        spChpas.setVisibility(View.GONE);
        spSubs.setVisibility(View.GONE);
        spTopics.setVisibility(View.GONE);


        if (it==1)
        {
            selectTopic();
        }
        else
        {
            selectElse();
        }


        return builder.create();

    }

    private void selectElse() {

        final ArrayList<ModelClass> temp1 = list;
        if (!temp1.get(0).getName().equalsIgnoreCase("Select Subect"))
        {
            temp1.add(0,new ModelClass("Select Subect",null));
        }
        TopicSelectorAdapter topicSelectorAdapter = new TopicSelectorAdapter(context,temp1);
        spSubs.setVisibility(View.VISIBLE);
        spSubs.setAdapter(topicSelectorAdapter);

        spSubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                {
                    topicModel =temp1.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void selectTopic() {
        final ArrayList<ModelClass> temp1 = list;
        if (!temp1.get(0).getName().equalsIgnoreCase("Select Subect"))
        {
            temp1.add(0,new ModelClass("Select Subect",null));
        }
        TopicSelectorAdapter topicSelectorAdapter = new TopicSelectorAdapter(context,temp1);
        spSubs.setVisibility(View.VISIBLE);
        spSubs.setAdapter(topicSelectorAdapter);

        spSubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i!=0)
                {
                    temp1.get(i).getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            subjectModel = documentSnapshot.toObject(SubjectModel.class);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (subjectModel.getChapters()!=null)
                            {
                                final ArrayList<ModelClass> temp2 = subjectModel.getChapters();
                                temp2.add(0,new ModelClass("Select Chapter",null));
                                TopicSelectorAdapter topicSelectorAdapter1 = new TopicSelectorAdapter(context,subjectModel.getChapters());
                                if (spChpas != null) {
                                    spChpas.setVisibility(View.VISIBLE);

                                    spChpas.setAdapter(topicSelectorAdapter1);
                                    spChpas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            if (i!=0)
                                            {
                                                temp2.get(i).getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        chapterModel = documentSnapshot.toObject(ChapterModel.class);

                                                    }
                                                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (chapterModel.getTopics()!=null)
                                                        {
                                                            final ArrayList<ModelClass> temp3 = chapterModel.getTopics();
                                                            temp3.add(0,new ModelClass("Select TOpics",null));
                                                            TopicSelectorAdapter topicSelectorAdapter2 = new TopicSelectorAdapter(context,temp3);
                                                            if (spTopics!=null)
                                                            {
                                                                spTopics.setVisibility(View.VISIBLE);
                                                                spTopics.setAdapter(topicSelectorAdapter2);
                                                                spTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                        if (i!=0)
                                                                        {
                                                                            topicModel=temp3.get(i);

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                                    }
                                                                });
                                                            }

                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {


                                        }
                                    });
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"In=mpliment exa");
        }


    }

    public interface ExampleDialogListener {
        void applyTexts(ModelClass topicModel,int i);
    }
}
