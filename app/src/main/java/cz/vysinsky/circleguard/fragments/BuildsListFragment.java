package cz.vysinsky.circleguard.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Random;

import cz.vysinsky.circleguard.R;
import cz.vysinsky.circleguard.adapters.BuildsListAdapter;
import cz.vysinsky.circleguard.model.entities.Build;
import cz.vysinsky.circleguard.model.lists.BuildsList;
import cz.vysinsky.circleguard.requests.BuildsRequest;

public class BuildsListFragment extends SpiceListFragment
        implements AbsListView.OnScrollListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private LinearLayout loadingPlaceholderContainer;
    private Boolean refreshing = false;
    private int currentVisibleItem;
    private int currentVisibleItemCount;
    private int totalItemCount;
    private BuildsListAdapter listViewAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimerThread();
    }



    private void startTimerThread() {
        Thread timerThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if(getActivity() == null) {
                            interrupt();
                            break;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listViewAdapter == null) {
                                    return;
                                }
                                Boolean changed = false;
                                for (int i = 0; i < listViewAdapter.getCount(); i++) {
                                    Build build = listViewAdapter.getItem(i);
                                    if (build.getStatus() == Build.STATUS_BLUE) {
                                        changed = true;
                                        break;
                                    }
                                }
                                if (changed) {
                                    listViewAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.i("TimerThread", "INTERRUPTED - " + e.getMessage());
                }
            }
        };
        timerThread.start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout wrapper = (LinearLayout) inflater.inflate(R.layout.fragment_builds_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) wrapper.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new OnBuildsRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        listView = (ListView) wrapper.findViewById(android.R.id.list);
        listView.setOnScrollListener(this);
        loadingPlaceholderContainer = (LinearLayout) wrapper.findViewById(R.id.list_loading_placeholder);
        refreshing = true;
        if (listViewAdapter == null) {
            loadData();
        } else {
            listView.setAdapter(listViewAdapter);
        }
        return wrapper;
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Build build = listViewAdapter.getItem(position);
        Toast.makeText(getActivity(), "Status of this build: " + build.getStatusText(), Toast.LENGTH_SHORT).show();
    }



    private void loadData() {
        performRequest(getRandom());
    }



    private void performRequest(String appendKey) {
        BuildsRequest buildsRequest = new BuildsRequest(getActivity());

        if (refreshing) {
            swipeRefreshLayout.setRefreshing(true);
            buildsRequest.setOffset(0);
            buildsRequest.setLimit(totalItemCount);
        } else {
            buildsRequest.setOffset(totalItemCount);
            buildsRequest.setLimit(BuildsRequest.DEFAULT_LIMIT);
        }

        String lastRequestCacheKey = buildsRequest.createCacheKey();
        spiceManager.execute(buildsRequest, lastRequestCacheKey + appendKey, DurationInMillis.ONE_MINUTE, new ListBuildsRequestListener());
    }



    public String getRandom() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 10; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }



    private void reloadData() {
        refreshing = true;
        performRequest(getRandom());
    }



    @Override
    public void onScrollStateChanged(AbsListView absListView, int currentScrollState) {
        if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE) {
            Boolean loadMore = currentVisibleItem + currentVisibleItemCount >= totalItemCount;
            if (loadMore) {
                loadingPlaceholderContainer.setVisibility(View.VISIBLE);
                loadData();
            }
        }
    }



    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }



    private class OnBuildsRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            reloadData();
        }
    }

    private class ListBuildsRequestListener implements RequestListener<BuildsList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.e(this.getClass().toString(), spiceException.getMessage());
            Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }



        @Override
        public void onRequestSuccess(BuildsList builds) {
            if (listView.getAdapter() == null && listViewAdapter == null || refreshing) {
                listViewAdapter = new BuildsListAdapter(getActivity(), builds);
                listView.setAdapter(listViewAdapter);
            } else {
                listViewAdapter.addAll(builds);
                listViewAdapter.notifyDataSetChanged();
            }

            if (refreshing) {
                refreshing = false;
            }

            loadingPlaceholderContainer.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
