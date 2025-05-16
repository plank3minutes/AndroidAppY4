/*
 * Copyright (c) 2025. rogergcc
 */

package com.appsnipp.education.ui.menuprofile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.appsnipp.education.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private ImageView backSettingImageView;
    private Button changeThemeButton;
    private Button changeFontButton;
    private Button informationButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        backSettingImageView = view.findViewById(R.id.back_setting_img_view_id);
        backSettingImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
        return view;
    }
}