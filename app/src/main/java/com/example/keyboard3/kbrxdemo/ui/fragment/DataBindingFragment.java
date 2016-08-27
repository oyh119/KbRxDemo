package com.example.keyboard3.kbrxdemo.ui.fragment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keyboard3.kbrxdemo.BR;
import com.example.keyboard3.kbrxdemo.Custom;
import com.example.keyboard3.kbrxdemo.R;

/**
 * Desc:
 * Author: ganchunyu
 * Date: 2016-08-27 17:16
 */
public class DataBindingFragment extends com.trello.rxlifecycle.components.support.RxFragment {

    private User user;

    public static DataBindingFragment newInstance() {

        Bundle args = new Bundle();

        DataBindingFragment fragment = new DataBindingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Custom binding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.data_bind, container, false);
        initData(binding);
        return binding.getRoot();
    }

    private void initData(Custom binding) {
        user = new User();
        binding.setUser(user);
        binding.setFrg(this);
    }

    public void click(View view) {
        user.setFirstName("qibin");
        user.setLastName("asdf");
    }

    public static class User extends BaseObservable {
        private String firstName;
        private String lastName;

        @Bindable
        public String getFirstName() {
            return this.firstName;
        }

        @Bindable
        public String getLastName() {
            return this.lastName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
            notifyPropertyChanged(BR.firstName);
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
            notifyPropertyChanged(BR.lastName);
        }
    }
}