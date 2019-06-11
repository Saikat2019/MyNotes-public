package com.kajal.mynotes.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kajal.mynotes.ui.NotesFragment;
import com.kajal.mynotes.ui.NotificationsFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
            {
                NotesFragment notesFragment = new NotesFragment();
                return notesFragment;
            }
            case 1:
            {
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;
            }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Notes";
            case 1:
                return "Notifications";
            default:
                return "Tab";
        }

    }
}
