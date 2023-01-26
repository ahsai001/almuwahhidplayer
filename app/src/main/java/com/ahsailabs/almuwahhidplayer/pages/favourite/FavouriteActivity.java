package com.ahsailabs.almuwahhidplayer.pages.favourite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ahsailabs.alcore.core.BaseActivity;
import com.ahsailabs.almuwahhidplayer.pages.favourite.fragments.FavouriteFolderListActivityFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.events.FavFABEvent;

import org.greenrobot.eventbus.EventBus;

public class FavouriteActivity extends BaseActivity {
    public FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enableUpNavigation();

        showFragment(R.id.fragment, FavouriteFolderListActivityFragment.class,null, null,null);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new FavFABEvent());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count > 0){
                getSupportFragmentManager().popBackStack();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context){
        Intent intent = new Intent(context, FavouriteActivity.class);
        context.startActivity(intent);
    }

}
