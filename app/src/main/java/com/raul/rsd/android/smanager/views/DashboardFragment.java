package com.raul.rsd.android.smanager.views;

import android.accounts.NetworkErrorException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.raul.rsd.android.smanager.App;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.adapters.CustomItem;
import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.domain.Skill;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.NetworkHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper.*;
import com.raul.rsd.android.smanager.managers.DataManager;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
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

    private EditText descriptionET, durationET;
    private Spinner typeSP;
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

        loadFromDatabase();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromServer();
            }
        });
    }

    private void loadFromDatabase(){
        mFastAdapter.clear();
        mSwipeRefresh.setEnabled(false);
        mSwipeRefresh.setRefreshing(false);
        mToolbar.getMenu().findItem(R.id.menu_server).setVisible(true);
        mToolbar.getMenu().findItem(R.id.menu_tasks).setVisible(false);

        // Insert the tasks in the RecyclerView
        for (Task task : activeUser.getTasks())
            mFastAdapter.add(mCustomItemProvider.get().withTask(task.getId(), task.getDescription(),
                    task.getRequiredSkill().getName(), task.getDuration()));
    }

    private void loadFromServer(){
        mSwipeRefresh.setRefreshing(true);

        NetworkHelper.getRequestedResource(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if(response == null || response.body() == null) {
                    onFailure(call, new NetworkErrorException("Resource request failed"));
                    return;
                }

                List<Resource> resources = response.body();
                addResourcesToAdapter(resources);

                mDataManager.saveResources(resources);

                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                Log.e(TAG, "onCreate: onFailure: ", t);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void addResourcesToAdapter(List<Resource> resources){
        mToolbar.getMenu().findItem(R.id.menu_server).setVisible(false);
        mToolbar.getMenu().findItem(R.id.menu_tasks).setVisible(true);
        mFastAdapter.clear();
        for (Resource res : resources)
            mFastAdapter.add(mCustomItemProvider.get().withResource(res.getFarm_name(),
                    res.getFarmer_id(), res.getPhone1(), res.getZipcode(), res.getLocation()));
    }

    private void adaptActionBar(){
        mToolbar.inflateMenu(R.menu.dashboard_menu);
        mToolbar.setTitle(activeUser.getName());
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_exit:
                        // Log the user out by erasing its id.
                        PreferencesHelper.setLong(getContext(), Keys.LOGGED_USER, Defaults.LOGGED_USER);

                        // Back to the login page
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.main_fragment_container, new LoginFragment()).commit();

                        // Make sure to hide the FAB if the user was an Admin
                        if(activeUser.getUserType() == User.ADMIN)
                            getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
                        break;
                    case R.id.menu_server:
                        mSwipeRefresh.setEnabled(true);
                        RealmResults<Resource> resources = mDataManager.findAllResources();
                        if(resources.size() != 0)
                            addResourcesToAdapter(resources);
                        else
                            loadFromServer();
                        break;
                    case R.id.menu_tasks:
                        loadFromDatabase();
                        break;
                }
                return false;
            }
        });

        View floatingActionButton = getActivity().findViewById(R.id.fab);
        if(activeUser.getUserType() == User.TECH) {
            floatingActionButton.setVisibility(View.GONE);
            return;
        }

        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskCreationDialog();
            }
        });

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

    private void showTaskCreationDialog(){
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.create_new_task)
                .customView(R.layout.dialog_add_task, true)
                .positiveText(R.string.save)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // Extract the info from the dialog
                        String description = descriptionET.getText().toString();
                        String durationStr = durationET.getText().toString();
                        int duration = Integer.parseInt(durationStr);
                        int position = typeSP.getSelectedItemPosition();
                        Skill skill = mDataManager.findAllSkills().get(position);

                        // Validate the data before saving, warn the user if wrong
                        if(description.length() == 0 || duration == 0)
                            Toast.makeText(getContext(), R.string.task_incomplete_toast, Toast.LENGTH_SHORT).show();
                        else {
                            Task saved = mDataManager.saveTask(
                                                new Task(description, skill, duration));
                            mFastAdapter.add(mCustomItemProvider.get().withTask(saved.getId()
                                    , saved.getDescription()
                                    , saved.getRequiredSkill().getName()
                                    , saved.getDuration()));
                        }
                    }
                })
                .build();

        //Get the necessary views from
        View dialogView = dialog.getCustomView();
        if(dialogView == null)
            return;

        descriptionET = (EditText) dialogView.findViewById(R.id.description_et);
        durationET = (EditText) dialogView.findViewById(R.id.duration_et);
        typeSP = (Spinner) dialogView.findViewById(R.id.task_type_sp);

        // Store all the skills in the adapter and assign the adapter to the Spinner
        List<String> types = new ArrayList<>();
        for(Skill skill : mDataManager.findAllSkills())
            types.add(skill.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, types);
        typeSP.setAdapter(adapter);

        dialog.show();
    }
}
