package com.kugot;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ListNotesFragment extends Fragment {

    private boolean isLandscape;
    private Note[] notes;
    private Note currentNote;
    private int mCurrentNoteIndex = -1;

    public ListNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_notes_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {
        notes = new Note[]{
                new Note(getString(R.string.first_note_title), getString(R.string.first_note_content), Calendar.getInstance()),
                new Note(getString(R.string.second_note_title), getString(R.string.second_note_content), Calendar.getInstance()),
                new Note(getString(R.string.third_note_title), getString(R.string.third_note_content), Calendar.getInstance()),
        };
        for (Note note : notes) {
            Context context = getContext();
            if (context != null) {
                LinearLayout linearView = (LinearLayout) view;
                TextView firstTextView = new TextView(context);
                TextView secondTextView = new TextView(context);
                firstTextView.setText(note.getTitle());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                secondTextView.setText(formatter.format(note.getCreationDate().getTime()));
                linearView.addView(firstTextView);
                linearView.addView(secondTextView);
                firstTextView.setPadding(0, 22, 0, 0);
                View.OnClickListener onClickListener = v -> {
                    currentNote = note;
                    showOrientatedNote(currentNote);
                };
                firstTextView.setOnClickListener(onClickListener);
                secondTextView.setOnClickListener(onClickListener);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NoteFragment.CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(NoteFragment.CURRENT_NOTE);
        } else {
            currentNote = notes[notes.length-1];
        }
        if (isLandscape) {
            showLandscapeNote(currentNote);
        }
    }

    private void showOrientatedNote(Note currentNote) {
        if (isLandscape) {
            showLandscapeNote(currentNote);
        } else {
            showPortraitNote(currentNote);
        }
    }

    private void showLandscapeNote(Note currentNote) {
        NoteFragment fragment = NoteFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_layout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    private void showPortraitNote(Note currentNote) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteFragment.CURRENT_NOTE, currentNote);
        startActivity(intent);
    }
}
