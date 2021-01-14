package com.iskool.iskool_t.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iskool.iskool_t.Activities.CoursesActivity;
import com.iskool.iskool_t.Adapter.ModelListAdapter;
import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    ArrayList<ModelClass> list;
    RecyclerView rvModelList;

    public ListFragment(ArrayList<ModelClass> list) {
        this.list = list;
    }


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvModelList=view.findViewById(R.id.rvListModel);

       if (list!=null)
       {
           ModelListAdapter modelListAdapter = new ModelListAdapter(list,R.layout.model_list_view);
           RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
           rvModelList.setAdapter(modelListAdapter);
           rvModelList.setLayoutManager(layoutManager);
           rvModelList.setHasFixedSize(true);
       }


    }


}
