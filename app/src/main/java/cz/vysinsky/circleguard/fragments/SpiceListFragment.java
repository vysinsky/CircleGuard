package cz.vysinsky.circleguard.fragments;

import android.app.ListFragment;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

public abstract class SpiceListFragment extends ListFragment {

    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }
}
