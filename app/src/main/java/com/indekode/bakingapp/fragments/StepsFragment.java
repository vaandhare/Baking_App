package com.indekode.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indekode.bakingapp.R;
import com.indekode.bakingapp.adapters.StepsAdapter;
import com.indekode.bakingapp.model.Steps;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment {

    private static String STEPS_ARGS_KEY = "key";
    public List<Steps> steps;
    private StepsAdapter.StepAdapterClickHandler clickHandler;
    public int position;
    private OnIngredientsClickListener mCallback;
    @BindView(R.id.rvSteps)
    RecyclerView mRecycleView;
    @BindView(R.id.tvIngredientsBtn)
    TextView ingredientButton;

    public void setPosition(int position) {
        this.position = position;
    }

    public interface OnIngredientsClickListener {
        void onClickHandlerIngredients();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnIngredientsClickListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnIngredientsClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        assert args != null;
        if(args.containsKey(STEPS_ARGS_KEY)) {
            String stepsJson = args.getString(STEPS_ARGS_KEY);
            Type stepsListType = new TypeToken<List<Steps>>() {}.getType();
            this.steps = new Gson().fromJson(stepsJson, stepsListType);
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_recipe_fragment, container, false);
        ButterKnife.bind(this,view);

        StepsAdapter StepsAdapter = new StepsAdapter(getContext(),steps,clickHandler,position);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(StepsAdapter);

        ingredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickHandlerIngredients();
            }
        });
        return view;
    }

    public void setStep(List<Steps> recipe1) {
        steps = recipe1;
    }

    public void setContext(Context context) {
    }

    public void setClickHandler(StepsAdapter.StepAdapterClickHandler handler){
        clickHandler = handler;
    }

    public static StepsFragment newInstance(List<Steps> steps) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        String strSteps = new Gson().toJson(steps);
        args.putString(STEPS_ARGS_KEY, strSteps);
        fragment.setArguments(args);
        return fragment;
    }
}