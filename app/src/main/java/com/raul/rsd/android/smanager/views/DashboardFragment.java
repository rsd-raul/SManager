package com.raul.rsd.android.smanager.views;

import android.accounts.NetworkErrorException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.raul.rsd.android.smanager.App;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.adapters.CustomItem;
import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.NetworkHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.managers.DataManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_rv) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefresh;
    @Inject DataManager mDataManager;
    @Inject FastItemAdapter<CustomItem> mFastAdapter;
    @Inject Provider<CustomItem> mCustomItemProvider;

    private User activeUser;

    @Inject
    public DashboardFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Dagger and ButterKnife init
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getComponent().inject(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        long uId = PreferencesHelper.getLong(getContext(), Keys.LOGGED_USER, Defaults.LOGGED_USER);
        activeUser = mDataManager.findUserById(uId);
        adaptActionBar();

        // Configure the Recycler view and its adapter
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFastAdapter);
        mRecyclerView.setHasFixedSize(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<CustomItem>() {
            @Override
            public boolean onClick(View v, IAdapter<CustomItem> adapter, CustomItem item, int position) {

                return false;
            }
        });

        queryDatabase();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryServer();
            }
        });
    }

    private void queryDatabase(){
        mFastAdapter.clear();
        mSwipeRefresh.setEnabled(false);
        mToolbar.getMenu().findItem(R.id.menu_server).setVisible(true);
        mToolbar.getMenu().findItem(R.id.menu_tasks).setVisible(false);

        // Insert the tasks in the RecyclerView
        for (Task task : activeUser.getTasks())
            mFastAdapter.add(mCustomItemProvider.get().withTask(task.getId(), task.getDescription(),
                    task.getRequiredSkill().getName(), task.getDuration()));
    }

    private void queryServer(){
        mFastAdapter.clear();
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.setEnabled(true);
        mToolbar.getMenu().findItem(R.id.menu_server).setVisible(false);
        mToolbar.getMenu().findItem(R.id.menu_tasks).setVisible(true);

        NetworkHelper.getRequestedResource(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(response == null || response.body() == null) {
                    onFailure(call, new NetworkErrorException("Resource request failed"));
                    return;
                }

                for (Resource res : response.body())
                    mFastAdapter.add(mCustomItemProvider.get().withResource(res.getFarm_name(),
                            res.getFarmer_id(), res.getPhone1(), res.getZipcode(), res.getLocation()));

                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Log.e(TAG, "onCreate: onFailure: ", t);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void adaptActionBar(){
        mToolbar.inflateMenu(R.menu.dashboard_menu);
        mToolbar.setTitle(activeUser.getName());
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_exit:
                        PreferencesHelper.setLong(getContext(), Keys.LOGGED_USER, Defaults.LOGGED_USER);
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new LoginFragment()).commit();
                        break;
                    case R.id.menu_server:
                        queryServer();
                        break;
                    case R.id.menu_tasks:
                        queryDatabase();
                        break;
                }
                return false;
            }
        });

        if(activeUser.getUserType() != User.ADMIN)
            return;

        // Get the ActionBar and customize color
        mToolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        // If the Android version allows, get the StatusBar and customize it
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }
}
