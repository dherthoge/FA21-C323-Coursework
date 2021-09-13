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

    View view;
    EditText editUserName;
    ImageButton buttonEdit;
    ImageButton buttonConfirm;
    Drawable greyCheck;
    Drawable greenCheck;
    Drawable blueCheck;
    boolean editing = false;

    public interface personalInformationFragmentInterface {
    }
    personalInformationFragmentInterface activityCommunicator;

    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_edit:
                setActiveButton("edit");
                break;
            case R.id.button_confirm:
                setActiveButton("confirm");
                break;
        }
    }

    public void setActiveButton(String button) {

        switch (button) {
            case "edit":
                if (!editing) {
                    buttonEdit.setImageDrawable(greyCheck);
                    buttonConfirm.setImageDrawable(greenCheck);
                    editUserName.setFocusableInTouchMode(true);
                    editing = !editing;
                }
                break;
            case "confirm":
                if (editing) {
                    buttonEdit.setImageDrawable(blueCheck);
                    buttonConfirm.setImageDrawable(greyCheck);
                    editUserName.setFocusable(false);
                    editing = !editing;
                }
                break;
        }
    }
}