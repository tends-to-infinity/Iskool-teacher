package com.iskool.iskool_t.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.iskool.iskool_t.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PdfViewFragment extends Fragment {
    Uri uri;

    public PdfViewFragment(Uri uri) {
        this.uri = uri;
    }

    public PdfViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PDFView pdfView = view.findViewById(R.id.pdfView);
        if (uri!=null)
        {
            Toast.makeText(getActivity(), "Khali nahi  bheja", Toast.LENGTH_SHORT).show();
            pdfView.fromUri(uri).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    Toast.makeText(getActivity(), "Loaded", Toast.LENGTH_SHORT).show();
                }
            }).load();
        }
        else
        {
            Toast.makeText(getActivity(), "Khali q bheja", Toast.LENGTH_SHORT).show();

        }
    }
}
