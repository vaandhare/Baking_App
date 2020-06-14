package com.indekode.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.indekode.bakingapp.R;
import com.indekode.bakingapp.adapters.StepsAdapter;
import com.indekode.bakingapp.fragments.FullStepsFragment;
import com.indekode.bakingapp.fragments.StepsFragment;
import com.indekode.bakingapp.model.Recipe;
import com.indekode.bakingapp.model.Steps;

import java.io.Serializable;
import java.util.List;

public class DetailRecipeActivity extends AppCompatActivity implements StepsAdapter.StepAdapterClickHandler,
        StepsFragment.OnIngredientsClickListener {

    public static final String RECIPE_ID = "extra_position";
    public static final String INGREDIENTS_LIST = "extra_list";
    public List<Steps> mStep;
    public String mTitle;
    private boolean mTwoPane = false;
    private int mPosition;
    public Recipe mRecipe;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(INGREDIENTS_LIST,mRecipe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_recipe);

        Intent intent = getIntent();
        if (intent == null)
            closeOnError();

        assert intent != null;
        mRecipe = (Recipe) intent.getSerializableExtra(RECIPE_ID);

        assert mRecipe != null;
        mStep = mRecipe.getSteps();
        mTitle = mRecipe.getName();
        this.setTitle(mTitle);
        FragmentManager manager = getSupportFragmentManager();
        StepsFragment stepsFragment = StepsFragment.newInstance(mStep);
        stepsFragment.setContext(getApplicationContext());
        stepsFragment.setStep(mStep);
        stepsFragment.setPosition(10);
        stepsFragment.setClickHandler(this);
        manager.beginTransaction().replace(R.id.fragmentSteps, stepsFragment).commit();

        if (findViewById(R.id.linear_layoutRecipe) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FullStepsFragment fragment = FullStepsFragment.newInstanceDetail(mStep);
                fragment.setPosition(mPosition);
                fragment.setStep(mStep);
                fragment.setTwoPane(mTwoPane);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentStepsDetail, fragment)
                        .commit();
            } else {
                mTwoPane = false;
            }
        }
    }

    private void launchStepDetailActivity(int position, String title, List<Steps> steps) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(StepDetailActivity.POSITION, position);
        intent.putExtra(StepDetailActivity.RECIPE_TITLE, title);
        intent.putExtra(StepDetailActivity.RECIPE_LIST, (Serializable) steps);
        startActivity(intent);
    }

    private void launchDetailIngredientActivity() {
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra(IngredientsActivity.RECIPE_LIST, mRecipe);
        intent.putExtra(IngredientsActivity.TITLE, mTitle);
        startActivity(intent);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickHandler(int position) {
        if (mTwoPane) {
            FullStepsFragment fragment = FullStepsFragment.newInstanceDetail(mStep);
            fragment.setStep(mStep);
            fragment.setTwoPane(mTwoPane);
            fragment.setPosition(position);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentStepsDetail, fragment)
                    .commit();
        } else {
            launchStepDetailActivity(position, mTitle, mStep);
        }
    }

    @Override
    public void onClickHandlerIngredients() {
        launchDetailIngredientActivity();
    }

    public void onClickHandler(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onClickHandlerIngredients(View view) {
        launchDetailIngredientActivity();
    }
}
