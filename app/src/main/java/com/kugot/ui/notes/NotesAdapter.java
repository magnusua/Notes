package com.kugot.ui.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.kugot.Note;
import com.kugot.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private NoteListFragment mFragment;

    public void setFragment(NoteListFragment noteListFragment) {
        mFragment = noteListFragment;
    }

    public interface OnNoteClickListener {
        void onNoteClicked(int position, Note note);
    }
    public interface OnNoteLongClickListener {
        void onNoteLongClicked(int position, Note note);
    }
    private List<Note> mNotes;

    private OnNoteClickListener onNoteClickListener;
    private OnNoteLongClickListener onNoteLongClickListener;

    public void setNotes(List<Note> notes) {
        this.mNotes = notes;
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        onNoteClickListener = listener;
    }
    public void setOnNoteLongClickListener(OnNoteLongClickListener listener) {
        onNoteLongClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.note_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.itemTitle.setText(note.getTitle());
        holder.itemTitle.setVisibility(note.getTitle().isEmpty() ? View.GONE : View.VISIBLE);
        holder.itemDescription.setText(note.getDescription());

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public TextView itemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.note_list_item_title);
            itemDescription = itemView.findViewById(R.id.note_list_item_description);
            if (mFragment != null) {
                mFragment.registerForContextMenu(itemView);
            }
            itemView.setOnLongClickListener((v) -> {
                if (onNoteLongClickListener != null) {
                    onNoteLongClickListener.onNoteLongClicked(getAdapterPosition(), mNotes.get(getAdapterPosition()));
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                if (onNoteClickListener != null)
                    onNoteClickListener.onNoteClicked(getAdapterPosition(), mNotes.get(getAdapterPosition()));
            });

        }
    }
}
