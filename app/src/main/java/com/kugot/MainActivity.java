package com.kugot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialButton btn;

        btn = findViewById(R.id.btn_fragment1);
        btn.setOnClickListener((view) -> {
            f1();
        });

        btn = findViewById(R.id.btn_fragment2);
        btn.setOnClickListener((view) -> {
            f2();
        });

        btn = findViewById(R.id.btn_fragment3);
        btn.setOnClickListener((view) -> {
            f3();
        });

        final MaterialButton menuBtn = findViewById(R.id.btn_back);
        menuBtn.setOnClickListener((view) -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, menuBtn);
            popup.inflate(R.menu.button_menu);
            popup.setOnMenuItemClickListener((item) -> {
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.menu_item_remove_fragment: {
                        manager.popBackStack();
                        return true;
                    }
                    case R.id.menu_item_remove_2_fragments: {
                        manager.popBackStack();
                        manager.popBackStack();
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            });
            popup.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_item_f1: {
                f1();
                return true;
            }
            case R.id.toolbar_menu_item_f2: {
                f2();
                return true;
            }
            case R.id.toolbar_menu_item_f3: {
                f3();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void f1() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, new Fragment1());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void f2() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, new Fragment2());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void f3() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, new Fragment3());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}