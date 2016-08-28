package com.example.keyboard3.kbrxdemo.ui.fragment.DataBind;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.keyboard3.kbrxdemo.ActivityMainBinding;
import com.example.keyboard3.kbrxdemo.R;
import com.github.markzhai.recyclerview.BaseViewAdapter;
import com.github.markzhai.recyclerview.BindingViewHolder;
import com.github.markzhai.recyclerview.MultiTypeAdapter;
import com.github.markzhai.recyclerview.SingleTypeAdapter;

import java.util.ArrayList;

/**
 * Desc:
 * Author: ganchunyu
 * Date: 2016-08-27 17:16
 */
public class DataBindingFragment extends com.trello.rxlifecycle.components.support.RxFragment {

    public static DataBindingFragment newInstance() {

        Bundle args = new Bundle();

        DataBindingFragment fragment = new DataBindingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.data_bind, container, false);

        mBinding.setPresenter(new Presenter());

        mSingleTypeAdapter = new SingleTypeAdapter<>(getContext(), R.layout.item_employee);

        mSingleTypeAdapter.setPresenter(new DemoAdapterPresenter());

        // you can use the built-in presenter
//        mSingleTypeAdapter.setPresenter(new SingleTypeAdapter.Presenter<EmployeeViewModel>() {
//
//            @Override
//            public void onItemClick(EmployeeViewModel model) {
//                Toast.makeText(MainActivity.this, model.name, Toast.LENGTH_SHORT).show();
//            }
//        });

        mSingleTypeAdapter.setDecorator(new DemoAdapterDecorator());

        mMultiTypeAdapter = new MultiTypeAdapter(getContext());
        mMultiTypeAdapter.setPresenter(new DemoAdapterPresenter());

        mMultiTypeAdapter.addViewTypeToLayoutMap(VIEW_TYPE_HEADER, R.layout.item_header);
        mMultiTypeAdapter.addViewTypeToLayoutMap(VIEW_TYPE_EMPLOYEE, R.layout.item_employee);
        mMultiTypeAdapter.addViewTypeToLayoutMap(VIEW_TYPE_EMPLOYER, R.layout.item_employer);

        mBinding.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.setAdapter(mSingleTypeAdapter);

        mSingleTypeAdapter.addAll(EMPLOYEE_LIST);
        mMultiTypeAdapter.add(null, VIEW_TYPE_HEADER);
        mMultiTypeAdapter.addAll(EMPLOYEE_LIST, VIEW_TYPE_EMPLOYEE);
        mMultiTypeAdapter.addAll(EMPLOYER_LIST, VIEW_TYPE_EMPLOYER);
        return mBinding.getRoot();
    }

    private ActivityMainBinding mBinding;

    private SingleTypeAdapter<EmployeeViewModel> mSingleTypeAdapter;
    private MultiTypeAdapter mMultiTypeAdapter;

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_EMPLOYEE = 1;
    private static final int VIEW_TYPE_EMPLOYER = 2;
    private static final ArrayList<EmployeeViewModel> EMPLOYEE_LIST = new ArrayList<>();
    private static final ArrayList<EmployerViewModel> EMPLOYER_LIST = new ArrayList<>();

    static {
        EmployeeViewModel model1 = new EmployeeViewModel("markzhai", 26);
        EmployeeViewModel model2 = new EmployeeViewModel("dim", 25);
        EmployeeViewModel model3 = new EmployeeViewModel("abner", 25);
        EmployeeViewModel model4 = new EmployeeViewModel("cjj", 26);

        EMPLOYEE_LIST.add(model1);
        EMPLOYEE_LIST.add(model2);
        EMPLOYEE_LIST.add(model3);
        EMPLOYEE_LIST.add(model4);

        EmployerViewModel model5 = new EmployerViewModel("boss1", 30,
                "https://avatars2.githubusercontent.com/u/1106500?v=3&s=150", "CEO");

        EmployerViewModel model6 = new EmployerViewModel("boss2", 31,
                "https://avatars3.githubusercontent.com/u/11629640?v=3&s=150", "CTO");

        EmployerViewModel model7 = new EmployerViewModel("boss3", 38,
                "https://avatars2.githubusercontent.com/u/1468623?v=3&s=150", "CAO");

        EMPLOYER_LIST.add(model5);
        EMPLOYER_LIST.add(model6);
        EMPLOYER_LIST.add(model7);
    }

    public class Presenter {
        public void onToggleClick(View view) {
            if (mBinding.getAdapter() == mMultiTypeAdapter) {
                mBinding.setAdapter(mSingleTypeAdapter);
            } else {
                mBinding.setAdapter(mMultiTypeAdapter);
            }
        }
    }

    public class DemoAdapterPresenter implements BaseViewAdapter.Presenter {
        public void onItemClick(EmployeeViewModel model) {
            Toast.makeText(getContext(), "employee " + model.name, Toast.LENGTH_SHORT).show();

        }

        public void onItemClick(EmployerViewModel model) {
            Toast.makeText(getContext(), "employer " + model.name, Toast.LENGTH_SHORT).show();
        }
    }

    public class DemoAdapterDecorator implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, int position, int viewType) {
            // you may do something according to position or view type
        }
    }
}