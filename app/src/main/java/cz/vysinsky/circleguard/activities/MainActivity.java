package cz.vysinsky.circleguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cz.vysinsky.circleguard.R;
import cz.vysinsky.circleguard.fragments.BuildsListFragment;
import cz.vysinsky.circleguard.fragments.SettingsFragment;


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



    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    private void openSettings() {
        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

}
