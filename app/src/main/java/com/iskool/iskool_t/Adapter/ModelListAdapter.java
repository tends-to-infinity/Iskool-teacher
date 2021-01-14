package com.iskool.iskool_t.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.iskool.iskool_t.Activities.HomeActivity;
import com.iskool.iskool_t.Fragment.AddVideoFrag;
import com.iskool.iskool_t.Fragment.TopicListFrag;
import com.iskool.iskool_t.Models.ChapterModel;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.Models.SubjectModel;
import com.iskool.iskool_t.Models.TopicModel;
import com.iskool.iskool_t.R;

import java.util.ArrayList;
import java.util.Random;

public class ModelListAdapter  extends RecyclerView.Adapter<ModelListAdapter.ViewHolder> {


    ArrayList<ModelClass> modelClasses;
    int lay;
    FragmentTransaction fragmentTransaction;


    public ModelListAdapter(ArrayList<ModelClass> modelClasses, int lay, FragmentTransaction fragmentTransaction) {
        this.modelClasses = modelClasses;
        this.lay = lay;
        this.fragmentTransaction = fragmentTransaction;
    }

    public ModelListAdapter(ArrayList<ModelClass> modelClasses, int lay) {
        this.modelClasses = modelClasses;
        this.lay = lay;
    }

    public ModelListAdapter(ArrayList<ModelClass> modelClasses) {
        this.modelClasses = modelClasses;

    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(lay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Random random = new Random();
        holder.tvName.setText(modelClasses.get(position).getName());
        if (holder.initial!=null)
        {
            holder.initial.setText(String.valueOf(modelClasses.get(position).getName().charAt(0)));
            //holder.initial.setBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String parent = modelClasses.get(position).getReff().getParent().getId();
                if (parent.equalsIgnoreCase("COURSES"))
                {
                    view.getContext().startActivity(new Intent(view.getContext(), HomeActivity.class).putExtra("cpath",modelClasses.get(position).getReff().getPath()));

                }
                else if (parent.equalsIgnoreCase("SUBJECTS"))
                {
                    modelClasses.get(position).getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            SubjectModel subjectModel = documentSnapshot.toObject(SubjectModel.class);
                            if (fragmentTransaction!=null)
                            {
                                TopicListFrag topicListFrag = new TopicListFrag(subjectModel);
                                fragmentTransaction.replace(R.id.TopicFragContainer,topicListFrag,null).commit();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (parent.equalsIgnoreCase("CHAPTERS"))
                {

                    modelClasses.get(position).getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                           ChapterModel chapterModel = documentSnapshot.toObject(ChapterModel.class);

                           if (fragmentTransaction!=null)
                           {
                               TopicListFrag topicListFrag = new TopicListFrag(chapterModel);
                               fragmentTransaction.replace(R.id.TopicFragContainer,topicListFrag,null).commit();
                           }
                           else
                           {
                               Toast.makeText(view.getContext(), "Gadbad", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(view.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });


                }
                else if (parent.equalsIgnoreCase("TOPICS"))
                {

                    modelClasses.get(position).getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            TopicModel topicModel=documentSnapshot.toObject(TopicModel.class);
                            if (fragmentTransaction!=null)
                            {
                                AddVideoFrag addVideoFrag = new AddVideoFrag(topicModel);
                                fragmentTransaction.replace(R.id.TopicFragContainer,addVideoFrag,null).commit();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(view.getContext(), ""+parent, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,initial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.modelName);
            initial = itemView.findViewById(R.id.tvInitial);

        }

    }
}
