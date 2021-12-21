package com.c323.midtermproject.dherthog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment to allow the user to choose the layout of the displayed Items.
 */
public class ViewOptionFragment extends Fragment {

    private DisplayOptionListener activityCommunicator;

    /**
     * Interface for communicating the user's selected option.
     */
    public interface DisplayOptionListener {
        public void optionChanged(ItemDisplayActivity.DisplayOption option);
    }

    /**
     * Creates a ViewOptionFragment.
     * @param activityCommunicator An Object that implements the Updatable interface. Allows the
     *                             fragment to notify it's activity if a City has been added or not
     */
    public ViewOptionFragment(DisplayOptionListener activityCommunicator) {
        this.activityCommunicator = activityCommunicator;
    }

    /**
     * Creates a hierarchy of Views for the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                 attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return Return the View for the fragment's UI, or null
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_view_options, container, false);

        setClickListeners(viewHierarchy);

        return viewHierarchy;
    }

    /**
     * Sets click listeners for each button in the fragment.
     * @param viewHierarchy The hierarchy of Views containing Views to set click listeners for
     */
    private void setClickListeners(View viewHierarchy) {
        Button listViewBtn = viewHierarchy.findViewById(R.id.btn_recycler_view);
        listViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityCommunicator.optionChanged(ItemDisplayActivity.DisplayOption.RECYCLERVIEW);
            }
        });

        Button cardViewBtn = viewHierarchy.findViewById(R.id.btn_card_view);
        cardViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityCommunicator.optionChanged(ItemDisplayActivity.DisplayOption.CARDVIEW);
            }
        });
    }
}
