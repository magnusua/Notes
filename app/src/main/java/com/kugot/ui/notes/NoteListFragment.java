package com.kugot.ui.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kugot.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class NoteListFragment extends Fragment {

    private NoteViewModel mNoteViewModel;
    private final NotesAdapter mAdapter = new NotesAdapter();

    public NoteListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        initList(view);
    }

    private void updateFragments() {
        if (!isLandscape()) findNavController(this).navigate(R.id.action_list_to_note);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initList(View view) {
        RecyclerView rvNotes = view.findViewById(R.id.note_list);
        mAdapter.setNotes(mNoteViewModel.getNotes().getValue().getList());

        mNoteViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            android.util.Log.e("Note", "notes changed: " + notes.getList().toString() );
            notes.applyChange(mAdapter);
        });

        mAdapter.setFragment(this);
        mAdapter.setOnNoteClickListener((position, note) -> {
            mNoteViewModel.setNoteToShow(note);
            updateFragments();
        });
        mAdapter.setOnNoteLongClickListener((position, note) -> {
            mNoteViewModel.setNoteToEdit(note);
        });
        rvNotes.setAdapter(mAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.note_list_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (mNoteViewModel.getNoteToEdit().getValue() != null) {
                Bundle bundle = new Bundle();
                bundle.putString(NoteEditFragment.NOTE_ID, mNoteViewModel.getNoteToEdit().getValue().getId());
                findNavController(this).navigate(R.id.action_list_to_edit, bundle);
                mNoteViewModel.setNoteToEdit(null);
                return true;
            } else {
                return false;
            }
        } else if (item.getItemId() == R.id.action_delete) {
            if (mNoteViewModel.getNoteToEdit().getValue() != null) {
                mNoteViewModel.deleteNote(mNoteViewModel.getNoteToEdit().getValue());
                mNoteViewModel.setNoteToEdit(null);
                return true;
            } else {
                return false;
            }
        } else {
            return super.onContextItemSelected(item);
        }
    }

}