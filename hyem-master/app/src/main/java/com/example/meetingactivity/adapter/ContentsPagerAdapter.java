package com.example.meetingactivity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.meetingactivity.Fragment.BoardFragment;
import com.example.meetingactivity.Fragment.CalendarFragment;
import com.example.meetingactivity.Fragment.InforFragment;
import com.example.meetingactivity.Fragment.PhotoFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {

        super(fm);
        this.mPageCount = pageCount;
    }


    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InforFragment inforFragment = new InforFragment();
                return inforFragment;

            case 1:
                BoardFragment boardFragment = new BoardFragment();
                return boardFragment;

            case 2:
                PhotoFragment photoFragment = new PhotoFragment();
                return photoFragment;

            case 3:
                CalendarFragment calendarFragment = new CalendarFragment();
                return calendarFragment;

            default:
                return null;

        }

    }


    @Override

    public int getCount() {
        return mPageCount;
    }

}
