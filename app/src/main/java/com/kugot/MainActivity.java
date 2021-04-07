package com.kugot;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.kugot.ui.notes.NoteViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NoteViewModel mNoteViewModel;
    private NavController mNavController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
        R.id.nav_notes, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();
        mNavController.addOnDestinationChangedListener((controller, destination, bundle) -> {
            if (destination.getId() == R.id.nav_notes) {
//                mNoteViewModel.select(null);
                fab.show();
            } else {
                fab.hide();
            }
        });

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, mNavController);

        fab.setOnClickListener(view -> mNavController.navigate(R.id.action_list_to_edit));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem save = menu.findItem(R.id.action_save);
        MenuItem edit = menu.findItem(R.id.action_edit);


        search.setVisible(noteListIsVisible());
        share.setVisible(noteIsVisible());
        edit.setVisible(noteIsVisible());
        save.setVisible(editNoteFragmentIsVisible());

        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    private boolean noteIsVisible() {
        return findViewById(R.id.fragment_note_date) != null;
    }

    private boolean editNoteFragmentIsVisible() {
        if (mNavController.getCurrentDestination() == null) return false;
        return (mNavController.getCurrentDestination().getId() == R.id.nav_edit_note);
    }

    private boolean noteListIsVisible() {
        if (mNavController.getCurrentDestination() != null) {
            return (mNavController.getCurrentDestination().getId() == R.id.nav_notes);
        } else {
            View noteList = findViewById(R.id.note_list);
            View noteSidebar = findViewById(R.id.note_detail);
            View noteSingle = findViewById(R.id.note);
            return (noteList != null && noteSidebar != null) || (noteList != null && noteSingle == null);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}