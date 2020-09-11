package com.example.gadsleaderboard.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gadsleaderboard.R;
import com.example.gadsleaderboard.databinding.FragmentLearnersBinding;
import com.example.gadsleaderboard.models.Learner;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class LearnersFragment extends Fragment {

    private static final String IS_IQ_LEADER = "iqLeader";
    private boolean isIQLeader;
    private List<Learner> leadersList = new ArrayList<>();
    private FragmentLearnersBinding binding;
    private LeadersAdapter itemsAdapter;
    private LeadersViewModel leadersViewModel;


    public static LearnersFragment newInstance(boolean isIQLeader) {
        Bundle args = new Bundle();
        args.putBoolean(IS_IQ_LEADER, isIQLeader);

        LearnersFragment fragment = new LearnersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LearnersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isIQLeader = getArguments() != null && getArguments().getBoolean(IS_IQ_LEADER);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_learners, container, false);

        initRecyclerView();
        setupViewModel();
        leadersViewModel.getLeaders(isIQLeader);

        return binding.getRoot();
    }

    private void setupViewModel() {
        leadersViewModel = new ViewModelProvider(this).get(LeadersViewModel.class);
        leadersViewModel.successRs.observe(getViewLifecycleOwner(), learners -> itemsAdapter.addLeaders(learners));
        leadersViewModel.failureMsg.observe(getViewLifecycleOwner(), this::displaySnakeBar);
        leadersViewModel.showPD.observe(getViewLifecycleOwner(), showPD -> {
            if (showPD) binding.indeterminateBar.setVisibility(View.VISIBLE);
            else binding.indeterminateBar.setVisibility(View.GONE);
        });
    }

    private void initRecyclerView() {
        itemsAdapter = new LeadersAdapter(getActivity(), leadersList, isIQLeader);
        binding.rvItems.setAdapter(itemsAdapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void displaySnakeBar(String msg){
        Snackbar.make(binding.rvItems, msg,Snackbar.LENGTH_LONG).show();
    }

}