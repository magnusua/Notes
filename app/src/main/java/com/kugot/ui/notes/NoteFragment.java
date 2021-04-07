package com.kugot.ui.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.kugot.Note;
import com.kugot.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class NoteFragment extends Fragment {

    private NoteViewModel mNoteViewModel;
    private Note note;
    private TextView noteContent;
    private TextView noteTitle;
    private TextView noteDate;

    public NoteFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        noteContent = view.findViewById(R.id.fragment_note_content);
        noteTitle = view.findViewById(R.id.fragment_note_title);
        noteDate = view.findViewById(R.id.fragment_note_date);

        if (mNoteViewModel.getNoteToShow().getValue() == null)
            renderEmptyNote();

        mNoteViewModel.getNoteToShow().observe(getViewLifecycleOwner(), note -> {
            this.note = note;
            if (note == null) {
                renderEmptyNote();
            } else {
                renderNote(note);
            }
        });
    }

    private void renderNote(Note note) {
        noteContent.setGravity(Gravity.START);
        noteDate.setVisibility(View.VISIBLE);
        noteTitle.setVisibility(View.VISIBLE);
        noteContent.setText(note.getDescription());
        noteTitle.setText(note.getTitle());
        noteDate.setText(
                LocalDateTime
                        .ofInstant(
                                Instant.ofEpochMilli(note.getDateTime()),
                                ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
    }

    private void renderEmptyNote() {
        noteContent.setText(R.string.empty_note);
        noteContent.setGravity(Gravity.CENTER);
        noteDate.setVisibility(View.GONE);
        noteTitle.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (note == null) return false;
            int actionId = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? R.id.action_list_to_edit : R.id.action_note_to_edit;
            Bundle bundle = new Bundle();
            bundle.putString(NoteEditFragment.NOTE_ID, note.getId());
            findNavController(this).navigate(actionId, bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}