package com.indekode.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.indekode.bakingapp.R;
import com.indekode.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    @BindView(R.id.tvRecipeTitle)
    TextView recipeName;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;

        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_view, parent, false);
        }

        Recipe currentRecipe = getItem(position);

        assert currentRecipe != null;
        String name = currentRecipe.getName();
        ButterKnife.bind(this,gridItemView);
        recipeName.setText(name);
        return gridItemView;
    }
}
