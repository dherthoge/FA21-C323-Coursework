package com.example.project4dylanherthoge;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInformationFragment extends Fragment implements View.OnClickListener {


    // References of Views in SettingsFragment
    View view;
    EditText editUserName;
    ImageButton buttonEdit;
    ImageButton buttonConfirm;
    Drawable greyCheck;
    Drawable greenCheck;
    Drawable blueCheck;

    // Whether or not the user is editing their name
    boolean editing = false;

    /**
     * Creates a PersonalInformationFragment object.
     */
    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param savedInstanceState The state of a previous instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Generates a View representing the fragment.
     * @param inflater A LayoutInflater object
     * @param container The container for the current fragment.
     * @param savedInstanceState The state of a previous instance
     * @return a View representing a PersonalInformationFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        editUserName = view.findViewById(R.id.edit_user_location);
        buttonEdit = view.findViewById(R.id.button_edit);
        buttonConfirm = view.findViewById(R.id.button_confirm);
        editUserName.setFocusable(false);
        buttonEdit.setOnClickListener(this::onClick);
        buttonConfirm.setOnClickListener(this::onClick);
        greyCheck = getResources().getDrawable(R.drawable.circle_grey_checkmark);
        greenCheck = getResources().getDrawable(R.drawable.circle_green_checkmark);
        blueCheck = getResources().getDrawable(R.drawable.circle_light_blue_checkmark);

        return view;
    }

    /**
     * Changes the active button and enables/disables editing.
     * @param view The clicked button
     */
    @Override
    public void onClick(View view) /**/{
        switch (view.getId()) {
            case R.id.button_edit:
                if (!editing) {
                    // Sets button colors
                    buttonEdit.setImageDrawable(greyCheck);
                    buttonConfirm.setImageDrawable(greenCheck);

                    // Enables editing
                    editUserName.setFocusableInTouchMode(true);
                    editing = !editing;
                }
                break;
            case R.id.button_confirm:
                if (editing) {
                    // Sets button colors
                    buttonEdit.setImageDrawable(blueCheck);
                    buttonConfirm.setImageDrawable(greyCheck);

                    // Disables editing
                    editUserName.setFocusable(false);
                    editing = !editing;
                }
                break;
        }
    }
}