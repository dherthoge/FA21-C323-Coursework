package com.example.project4dylanherthoge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInformationFragment extends Fragment {

    EditText editUserName;

    View view;

    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editUserName = view.findViewById(R.id.edit_user_location);
        editUserName.setFocusable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal_information, container, false);

        return view;
    }
}