package com.kugot;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NoteFirebaseRepo {
    private static final String TAG = "NoteFirebaseRepo";

    private static FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }
    private static CollectionReference notes() {
        return db().collection("notes");
    }

    public static void getNotesAndThen(Consumer<List<Note>> consumer) {
        List<Note> result = new ArrayList<>();
        notes()
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(task -> {
                    task.forEach(document -> {
                        Note note = document.toObject(Note.class);
                        note.setId(document.getId());
                        result.add(note);
                    });
                    consumer.accept(result);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("Firebase", e.toString());
                });
    }

    public static void addNoteAndThen(Note note, Consumer<Note> consumer) {
        notes()
                .add(note)
                .addOnSuccessListener(documentReference -> {
                    note.setId(documentReference.getId());
                    consumer.accept(note);
                });
    }

    public static void updateNote(Note note, Consumer<Note> consumer) {
         notes().document(note.getId()).set(note).addOnSuccessListener(aVoid -> {
             consumer.accept(note);
         });
    }

    public static void removeNoteAndThen(Note note, Consumer<Note> consumer) {
        notes().document(note.getId()).delete().addOnSuccessListener(aVoid -> {
            consumer.accept(note);
        });
    }
}
