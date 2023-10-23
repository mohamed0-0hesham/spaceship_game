package com.hesham0_0.spaceship.ui.menu;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesham0_0.spaceship.R;
import com.hesham0_0.spaceship.ui.game.GameActivity;

public class MenuFragment extends Fragment {

    private MenuViewModel mViewModel;
    private View startBtn;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
    }
    private void initUi(View view){
        startBtn = view.findViewById(R.id.next_btn);
        startBtn.setOnClickListener(v -> {
            navToGameActivity();
        });
    }

    private void navToGameActivity() {
        Intent intent = new Intent(requireActivity(), GameActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}