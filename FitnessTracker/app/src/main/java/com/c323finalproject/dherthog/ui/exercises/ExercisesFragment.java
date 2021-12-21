package com.c323finalproject.dherthog.ui.exercises;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c323finalproject.dherthog.Database;
import com.c323finalproject.dherthog.Exercise;
import com.c323finalproject.dherthog.ExerciseDao;
import com.c323finalproject.dherthog.ImageManager;
import com.c323finalproject.dherthog.ModeDao;
import com.c323finalproject.dherthog.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays Exercises from the Database.
 */
public class ExercisesFragment extends Fragment {

    private ExerciseRecyclerViewAdapter exerciseRecyclerViewAdapter;
    private List<Exercise> exercises;
    private List<Bitmap> bitmaps;
    private ExerciseDao exerciseDao;
    private ModeDao modeDao;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;


    /**
     * Obtains references for necessary Views.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *                 fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *                  attached to. The fragment should not add the view itself, but this can be
     *                  used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercises, container, false);

        recyclerView = root.findViewById(R.id.rv_exercises);
        fab = root.findViewById(R.id.fab_add_exercise);

        return root;
    }

    /**
     * Gets Exercises and Bitmaps and sets up recyclerView and fab.
     * @param view The root View
     * @param savedInstanceState Used for configuration changes
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get exercises and their corresponding bitmaps
        getModel();

        // Creates the RecyclerView
        createRecyclerView();

        // Navigates to AddExerciseFragment when fab is pressed
        fab.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Navigates to AddExerciseFragment.
                     * @param v The clicked View
                     */
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(v).navigate(R.id.action_nav_exercises_to_nav_add_exercise_fragment);
                    }
                }
        );
    }

    /**
     * Creates and attaches an adapter and listener to recyclerView.
     */
    private void createRecyclerView() {

        // Set up the Adapter for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exerciseRecyclerViewAdapter = new ExerciseRecyclerViewAdapter(exercises, bitmaps);
        recyclerView.setAdapter(exerciseRecyclerViewAdapter);
        // Sets helper for swiping recycler view items
        setRecyclerViewListeners(recyclerView);
    }

    /**
     * Sets click and swipe listeners for the given RecyclerView and it's adapter.
     * @param recyclerView The instance of the RecyclerView
     */
    private void setRecyclerViewListeners(RecyclerView recyclerView) {
        // Set click listener to perform exercise
        exerciseRecyclerViewAdapter.setClickListener(new ExerciseClickListener() {
            /**
             * Navigates to PerformExerciseFragment.
             * @param view The clicked View
             * @param position The position of the exercise to perform from this.exercises.
             */
            @Override
            public void onItemClick(View view, int position) {
                Exercise exercise = exercises.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("exerciseName", exercise.getName());
                bundle.putString("filePath", exercise.getImageFilePath());
                bundle.putString("time", modeDao.getAllMode().get(0).getTime()+"");
                Navigation.findNavController(view).navigate(R.id.action_nav_exercises_to_nav_perform_exercise_fragment, bundle);
            }
        });

        // Set swipe listener for exercise deletion
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            /**
             * Unused import.
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Creates an AlertDialog to confirm exercise deletion.
             * @param viewHolder The swiped ViewHolder
             * @param direction The direction the user swiped
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.delete_exercise_msg)
                        .setPositiveButton(R.string.yes_msg, new DialogInterface.OnClickListener() {
                            /**
                             * Deletes the Exercise displayed in viewHolder.
                             * @param dialogInterface
                             * @param i
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int exercisePosition = viewHolder.getAdapterPosition();
                                Exercise exercise = exercises.get(exercisePosition);
                                ImageManager.deleteImage(exercise.getImageFilePath());
                                exerciseDao.delete(exercise);
                                exercises.remove(exercisePosition);
                                bitmaps.remove(exercisePosition);

                                // Recreate the RecyclerView
                                createRecyclerView();
                            }
                        })
                        .setNegativeButton(R.string.no_msg, new DialogInterface.OnClickListener() {
                            /**
                             * Does not perform deletion.
                             * @param dialogInterface
                             * @param i
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            /**
             * Overridden to remove swipe animation when swiping right.
             * @param c
             * @param recyclerView
             * @param viewHolder
             * @param dX
             * @param dY
             * @param actionState
             * @param isCurrentlyActive
             */
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setItemAnimator(null);
    }

    /**
     * Gets all exercises from the database and reads their corresponding Bitmaps.
     */
    private void getModel() {
        // Get database
        Database database = Database.getInstance(getContext());
        exerciseDao = database.getExerciseDAO();
        modeDao = database.getModeDAO();

        exercises = exerciseDao.getAllExercise();
        bitmaps = new ArrayList<>();
        for (Exercise exercise: exercises)
            bitmaps.add(ImageManager.readImage(exercise.getImageFilePath()));
    }

    /**
     * A RecyclerView Adapter for Exercises.
     */
    private static class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder> {

        private final List<Exercise> exercises;
        private final List<Bitmap> bitmaps;
        private ExerciseClickListener clickListener;

        /**
         * @param exercises A List<Exercise> to display.
         * @param bitmaps A corresponding List<Exercise> to display.
         */
        ExerciseRecyclerViewAdapter(List<Exercise> exercises, List<Bitmap> bitmaps) {
            this.exercises = exercises;
            this.bitmaps = bitmaps;
        }

        /**
         * Unused necessary override.
         * @param parent
         * @param viewType
         * @return
         */
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
            return new ViewHolder(root);
        }

        /**
         * Sets content for the given ViewHolder.
         * @param holder A reference to the ViewHolder to populate.
         * @param position The absolute position of holder in the adapter.
         */
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.nameTv.setText(exercises.get(position).getName());
            holder.exerciseIv.setImageBitmap(bitmaps.get(position));
        }

        /**
         * @return The number of items in the adapter.
         */
        @Override
        public int getItemCount() {
            return exercises.size();
        }


        /**
         * A class to hold data for each View in the adapter.
         */
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView nameTv;
            ImageView exerciseIv;

            /**
             * Obtains references for necessary Views in the ViewHolder and sets an OnClickListener
             * for the View.
             * @param itemView The View to obtain references from
             */
            ViewHolder(View itemView) {
                super(itemView);
                nameTv = itemView.findViewById(R.id.tv_exercise_list_item_name);
                exerciseIv = itemView.findViewById(R.id.iv_exercise_list_item_image);
                itemView.setOnClickListener(this);
            }

            /**
             * Notifies the clickListener an item was clicked.
             * @param view The clicked view
             */
            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
            }
        }

        /**
         * @param itemClickListener The listener
         */
        void setClickListener(ExerciseClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
    }

    /**
     * Notifies the adapter an item has been added.
     */
    @Override
    public void onResume() {
        super.onResume();

        exerciseRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Parent of the RecyclerView implements this to listen for item clicks.
     */
    public interface ExerciseClickListener {
        void onItemClick(View view, int position);
    }
}