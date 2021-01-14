package com.iskool.iskool_t.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.iskool.iskool_t.Activities.HomeworkActivity;
import com.iskool.iskool_t.Adapter.ModelListAdapter;
import com.iskool.iskool_t.Models.CourseModel;
import com.iskool.iskool_t.Models.HomeworkModel;
import com.iskool.iskool_t.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkViewFragment extends Fragment {

    HomeworkModel homeworkModel;
    ModelListAdapter modelListAdapter,modelListAdapter2,modelListAdapter3;
    RecyclerView rvTOpics,rvQuiz,rvAss;

    TextView tvDate;
    CourseModel courseModel;

    public HomeworkViewFragment(HomeworkModel homeworkModel, CourseModel courseModel) {
        this.homeworkModel = homeworkModel;
        this.courseModel = courseModel;
    }



    public HomeworkViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homework_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAss=view.findViewById(R.id.rvAssignments);
        rvQuiz=view.findViewById(R.id.rvQuiz);
        rvTOpics=view.findViewById(R.id.rvTopics);
        tvDate = view.findViewById(R.id.tvDate);
        setData(homeworkModel);
    }

    private void setData(HomeworkModel hModel) {
        if (hModel!=null)
        {
            if (hModel.getSelf()!=null)
            {
                tvDate.setText(hModel.getSelf().getName());
                tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDate();
                    }
                });
            }

            if (hModel.getTopics()!=null)
            {
                intialise();
            }
            else
            {
                rvTOpics.setAdapter(null);

            }
            if (hModel.getAssignments()!=null)
            {
                intiAss();
            }
            else
            {
                rvAss.setAdapter(null);

            }
            if (hModel.getQuiz()!=null)
            {
                intiQuiz();
            }
            else
            {
                rvQuiz.setAdapter(null);
            }
        }

    }

    private void setDate() {

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {


                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("yyyyMMdd", calendar1).toString();
                tvDate.setText(dateText);


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
                            Toast.makeText(getActivity(), "Nahi h aaj", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        setData(homeworkModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //dates=dateText;
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();



    }

    private void intialise() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter = new ModelListAdapter(homeworkModel.getTopics(),R.layout.subject_chapters_view_rv,null);

        rvTOpics.setAdapter(modelListAdapter);
        rvTOpics.setLayoutManager(manager);
    }
    private void intiQuiz() {

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter3 = new ModelListAdapter(homeworkModel.getQuiz(),R.layout.subject_chapters_view_rv,null);

        rvQuiz.setAdapter(modelListAdapter3);
        rvQuiz.setLayoutManager(manager);

    }

    private void intiAss() {

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        modelListAdapter2 = new ModelListAdapter(homeworkModel.getAssignments(),R.layout.subject_chapters_view_rv,null);

        rvAss.setAdapter(modelListAdapter2);
        rvAss.setLayoutManager(manager);
        rvAss.setHasFixedSize(true);
    }

}
