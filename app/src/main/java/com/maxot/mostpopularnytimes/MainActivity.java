package com.maxot.mostpopularnytimes;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.maxot.mostpopularnytimes.fragments.ViewedFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding toolbar to MainScreen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
        //Add Fragments to Tabs
        private void setupViewPager(ViewPager viewPager){
            Adapter adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new ViewedFragment(),"Viewed");
            adapter.addFragment(new ViewedFragment(),"Viewed");
            adapter.addFragment(new ViewedFragment(),"Viewed");
            viewPager.setAdapter(adapter);
    }
    //Adapter creating
    private class Adapter extends FragmentPagerAdapter{
        private List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private List<String> mFragmentTitleList = new ArrayList<String>();

        public Adapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}

