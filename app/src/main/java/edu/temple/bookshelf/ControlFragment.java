package edu.temple.bookshelf;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.service.controls.Control;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

import edu.temple.audiobookplayer.AudiobookService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {

    private static final String BOOK = "book";
    private Book book;

    Button playButton;
    Button stopButton;
    Button pauseButton;
    TextView nowPlayingTextView;
    SeekBar seekBar;
    ControlInterface parentActivity;

    public ControlFragment() {
        // Required empty public constructor
    }

    public static ControlFragment newInstance(Book book) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ControlFragment.ControlInterface){
            parentActivity = (ControlFragment.ControlInterface) context;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind our service
//        Intent serviceIntent = new Intent(getActivity(), AudiobookService.class);
//        serviceIntent.putExtra(BOOK, book);
//        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        if (getArguments() != null) {
            book = getArguments().getParcelable(BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        playButton = view.findViewById(R.id.playButton);
        stopButton = view.findViewById(R.id.stopButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        nowPlayingTextView = view.findViewById(R.id.nowPlayingTextView);
        seekBar = view.findViewById(R.id.seekBar);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.pause();
                parentActivity.play();
                nowPlayingTextView.setText(parentActivity.nowPlaying());
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.stop();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.pause();
            }
        });

        return view;
    }

    interface ControlInterface {
        void play();
        void stop();
        void pause();
        void update(int prog);
        String nowPlaying();
    }
}