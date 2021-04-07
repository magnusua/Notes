package com.kugot.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import com.kugot.ListLiveData;
import com.kugot.Note;
import com.kugot.NoteFirebaseRepo;

public class NoteViewModel extends ViewModel {
    private final MutableLiveData<Note> noteToShow = new MutableLiveData<>();
    private final MutableLiveData<Note> noteToEdit = new MutableLiveData<>();
    private ListLiveData<Note> notes;

    public ListLiveData<Note> getNotes() {
        if (notes == null) {
            notes = new ListLiveData<>(new ArrayList<>());
            NoteFirebaseRepo.getNotesAndThen(notes::populate);
        }
        return notes;
    }

    public void setNoteToShow(Note note) {
        noteToShow.setValue(note);
    }

    public void setNoteToEdit(Note note) {
        noteToEdit.setValue(note);
    }

    public LiveData<Note> getNoteToShow() {
        return noteToShow;
    }

    public LiveData<Note> getNoteToEdit() {
        return noteToEdit;
    }

    public Note getNote(String id) {
        if (getNotes().getValue() == null) return null;
        return getNotes().getValue().getList().stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    public void saveNote(Note note) {
        if (note.isNew()) {
            NoteFirebaseRepo.addNoteAndThen(note, getNotes()::setOrAddItem);
        } else {
            NoteFirebaseRepo.updateNote(note, getNotes()::setOrAddItem);
        }
    }

    public void deleteNote(Note note) {
        if (note == null) return;
        NoteFirebaseRepo.removeNoteAndThen(note, getNotes()::removeItem);

    }
}
