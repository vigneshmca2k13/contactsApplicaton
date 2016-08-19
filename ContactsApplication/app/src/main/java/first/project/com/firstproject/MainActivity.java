package first.project.com.firstproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    Context mContext;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    SharedPreferences mpreference;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
//        mpreference = PreferenceManager.getDefaultSharedPreferences(mContext);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout) ;
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.bounce_interpolator);
        layout.setAnimation(animAnticipateOvershoot);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, layout, mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch

//        dbhelper.addNotes(new Note(1,"vignesh","22-01-2016","12:13","hai how are you?"));
//        dbhelper.addNotes(new Note(1,"note","22-01-2016","12:13","hai how are you?"));
        displayView(0);

    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new ContactsFragment();
                title = getString(R.string.nav_item_contacts);
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
            //   showNotification();
            //  alarmManager();
        }
    }

}





