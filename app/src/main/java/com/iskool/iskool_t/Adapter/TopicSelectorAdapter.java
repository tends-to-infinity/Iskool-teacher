package com.iskool.iskool_t.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iskool.iskool_t.Models.ModelClass;
import com.iskool.iskool_t.R;

import java.util.ArrayList;

public class TopicSelectorAdapter extends ArrayAdapter<ModelClass> {
    public TopicSelectorAdapter(Context context, ArrayList<ModelClass> countryList) {
        super(context, 0, countryList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.model_list_view, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.modelName);
        final ModelClass currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem.getName());

        }
        return convertView;
    }
}

