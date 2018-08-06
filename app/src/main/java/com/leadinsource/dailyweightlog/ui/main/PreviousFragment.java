package com.leadinsource.dailyweightlog.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leadinsource.dailyweightlog.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousFragment extends Fragment {


    public PreviousFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_previous, container, false);
    }

}
