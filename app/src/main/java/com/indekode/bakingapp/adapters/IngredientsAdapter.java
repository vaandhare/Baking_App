package com.indekode.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.indekode.bakingapp.R;
import com.indekode.bakingapp.model.Ingredients;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends ArrayAdapter<Ingredients> {

    @BindView(R.id.tvIngredient) TextView ingredient;
    @BindView(R.id.tvQuantity) TextView quantity;
    @BindView(R.id.tvMeasure) TextView measure;

    public IngredientsAdapter(Context context, List<Ingredients> ingredients) {
        super(context, 0, ingredients);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View gridIngredientView = convertView;

        if (gridIngredientView == null) {
            gridIngredientView = LayoutInflater.from(getContext()).inflate(R.layout.ingredients_item, parent, false);
        }

        Ingredients currentIngredient = getItem(position);
        ButterKnife.bind(this,gridIngredientView);

        assert currentIngredient != null;
        ingredient.setText(currentIngredient.getIngredient());
        quantity.setText(currentIngredient.getQuantity());
        measure.setText(currentIngredient.getMeasure());

        return gridIngredientView;
    }
}
