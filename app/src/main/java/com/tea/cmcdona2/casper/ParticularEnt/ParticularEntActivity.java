package com.tea.cmcdona2.casper.ParticularEnt;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;
import com.tea.cmcdona2.casper.R;

public class ParticularEntActivity extends AppCompatActivity{

    public Toolbar toolbar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ViewPager viewpager;
        setContentView(R.layout.particular_ent_activity);
        FragmentPageAdapter ft;
        viewpager = (ViewPager) findViewById(R.id.pager1);
        ft = new FragmentPageAdapter(getSupportFragmentManager());
        viewpager.setAdapter(ft);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class FragmentPageAdapter extends FragmentPagerAdapter {
        public FragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }


        String temp = getIntent().getStringExtra("swipeEventId");
        String[] swipeEventId = temp.split(",");
        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub

            int i = 0;
            int count = getCount();

            for (i=0;i< count; i++)

            {
                if (arg0 == i) {
                    Log.v("DisplayDates", "" + arg0);

                    Bundle bundle = new Bundle();
                    int position = getIntent().getIntExtra("swipePosition", 0);
                    bundle.putInt("eventId", Integer.parseInt(swipeEventId[position+i]));
                    Fragment fragment = new ParticularEntFrag();
                    fragment.setArguments(bundle);
                    return fragment;
                }

            }
            return null;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = getIntent().getIntExtra("swipeCount",0);
            int swipePosition = getIntent().getIntExtra("swipePosition",0);
            count = count - swipePosition;
            return count;
        }
    }

}