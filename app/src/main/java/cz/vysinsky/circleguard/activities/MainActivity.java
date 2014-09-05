package cz.vysinsky.circleguard.activities;

import android.app.Activity;
import android.os.Bundle;

import cz.vysinsky.circleguard.R;
import cz.vysinsky.circleguard.fragments.BuildsListFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        BuildsListFragment buildsListFragment = new BuildsListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.main_container, buildsListFragment)
                .commit();
    }

}
