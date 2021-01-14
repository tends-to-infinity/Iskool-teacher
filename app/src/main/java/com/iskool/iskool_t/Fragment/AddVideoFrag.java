package com.iskool.iskool_t.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.iskool.iskool_t.Models.ChapterModel;
import com.iskool.iskool_t.Models.TopicModel;
import com.iskool.iskool_t.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVideoFrag extends Fragment {

    TopicModel topicModel;
    PlayerView playerView;
    Uri uri;
    Button btnUp;
    SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;



    public AddVideoFrag(TopicModel topicModel) {
        this.topicModel = topicModel;
    }

    public AddVideoFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnUp =view.findViewById(R.id.btnVideoupload);
        playerView = view.findViewById(R.id.player);



        if (topicModel.getVpath()==null)
        {
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galleryIntent, 1);
                }
            });
        }
        else
        {
            Toast.makeText(getContext(), "Video Hai", Toast.LENGTH_SHORT).show();
            FirebaseStorage.getInstance().getReferenceFromUrl(topicModel.getVpath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri nuri) {
                    uri =nuri;
                    if (Util.SDK_INT >= 24) {
                        initializePlayer();
                    }
                }
            });
            btnUp.setText("Done");
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fini();
                }
            });
        }

    }

    private void fini() {

        topicModel.getParent().getReff().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChapterModel chapterModel = documentSnapshot.toObject(ChapterModel.class);
                TopicListFrag topicListFrag =new TopicListFrag(chapterModel);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.TopicFragContainer,topicListFrag,null).commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode== Activity.RESULT_OK&&data!=null&&data.getData()!=null)
        {
            uri=data.getData();
            btnUp.setText("Upload");
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnUp.setEnabled(false);
                    FirebaseStorage.getInstance().getReference(topicModel.getSelf().getReff().getPath()).child("Video").putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    topicModel.setVpath(taskSnapshot.getStorage().toString());
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            long per = (taskSnapshot.getBytesTransferred()*100)/(taskSnapshot.getTotalByteCount());
                            Toast.makeText(getContext(), ""+per, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            topicModel.getSelf().getReff().set(topicModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    fini();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }

    private void initializePlayer() {
        if (uri!=null)
        {
            player = ExoPlayerFactory.newSimpleInstance(getContext());
            playerView.setPlayer(player);
            MediaSource mediaSource = buildMediaSource(uri);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare(mediaSource, false, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}
