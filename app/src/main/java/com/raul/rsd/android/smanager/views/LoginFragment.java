package com.raul.rsd.android.smanager.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.raul.rsd.android.smanager.App;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.domain.User;
import com.raul.rsd.android.smanager.helpers.PreferencesHelper;
import com.raul.rsd.android.smanager.managers.DataManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.password_et) EditText passwordET;
    @BindView(R.id.username_et) EditText usernameET;
    @BindView(R.id.warning_tv) TextView warningTV;
    @Inject DataManager dataManager;

    @Inject
    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Dagger and ButterKnife init
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getComponent().inject(this);
        return view;
    }

    @OnClick(R.id.login_bn)
    void login(){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        User user = dataManager.findUserByName(username);

        if(user == null) {
            warningTV.setVisibility(View.VISIBLE);
            warningTV.setText(R.string.incorrect_user);
        } else if (!user.getPassword().equals(password)) {
            warningTV.setVisibility(View.VISIBLE);
            warningTV.setText(R.string.incorrect_password);
            passwordET.setText("");
        } else {
            warningTV.setVisibility(View.INVISIBLE);
            PreferencesHelper.setLong(getContext(), PreferencesHelper.Keys.LOGGED_USER, user.getId());
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_container, new DashboardFragment()).commit();
        }
    }
}
