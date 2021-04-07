package com.kugot.ui.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kugot.Note;
import com.kugot.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class NoteEditFragment extends Fragment {

    public static final String NOTE_ID = "noteId";

    private Note note;
    private NoteViewModel mNoteViewModel;
    private EditText etTitle;
    private EditText etContent;

    public NoteEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_note_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        if (getArguments() != null && !getArguments().getString(NOTE_ID).equals("-1")) {
            note = mNoteViewModel.getNote(getArguments().getString(NOTE_ID));
        }
        if (note == null) {
            note = new Note("", "", System.currentTimeMillis());
        }
        etTitle = view.findViewById(R.id.fragment_note_edit_title);
        etTitle.setText(note.getTitle());

        etContent = view.findViewById(R.id.fragment_note_edit_content);
        etContent.setText(note.getDescription());
        if (etContent.requestFocus()) {
            etContent.postDelayed(() -> {
                getInputMethodManager()
                        .showSoftInput(
                                etContent,
                                InputMethodManager.RESULT_UNCHANGED_SHOWN
                        );
            }, 200);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (note == null) return false;
            mNoteViewModel.setNoteToShow(note);
            note.setTitle(etTitle.getText().toString());
            note.setDescription(etContent.getText().toString());
            mNoteViewModel.saveNote(note);
            findNavController(this).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            getInputMethodManager()
                    .hideSoftInputFromWindow(
                            view.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN
                    );
        }
    }

    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}