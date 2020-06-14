package com.indekode.bakingapp.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.indekode.bakingapp.R;
import com.indekode.bakingapp.adapters.IngredientsAdapter;
import com.indekode.bakingapp.data.Contractor.IngredientsEntry;
import com.indekode.bakingapp.model.Ingredients;
import com.indekode.bakingapp.model.Recipe;
import com.indekode.bakingapp.widget.RecipeWidgetProvider;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {
    public static final String RECIPE_LIST = "extra_list_recipe";
    public static final String TITLE = "extra_title";
    @BindView(R.id.ingredients_grid_view)
    GridView ingredientsGridView;
    @BindView(R.id.tvAddToWidgets)
    TextView addToWidget;
    @BindView(R.id.imgAddToWidget)
    ImageView imgAddFav;
    private boolean isAdded = false;
    private String mTitle;
    private Context mContext;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);
        mContext = this;
        Intent intent = getIntent();
        if (intent == null)
            closeOnError();

        mRecipe = (Recipe) Objects.requireNonNull(intent).getSerializableExtra(RECIPE_LIST);
        mTitle = intent.getStringExtra(TITLE);

        this.setTitle(mTitle);
        IngredientsAdapter mAdapter = new IngredientsAdapter(this, mRecipe.getIngredient());
        ingredientsGridView.setAdapter(mAdapter);
        setFavorites();

        imgAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdded)
                    removeList();
                else
                    insertList();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setFavorites() {
        Uri queryUri = IngredientsEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf( mRecipe.getId())).build();
        Cursor cursor = getContentResolver().query(queryUri, null, null, null, null);

        if (cursor == null)
            return;

        if (cursor.moveToFirst()) {
            addToWidget.setText("Added to Widget!");
            imgAddFav.setImageResource(R.drawable.ic_favorite);
            isAdded = true;
            cursor.close();
        }
    }

    @SuppressLint("SetTextI18n")
    private void insertList() {
        ContentValues values = new ContentValues();
        values.put(IngredientsEntry.COLUMN_RECIPE_ID, mRecipe.getId());
        values.put(IngredientsEntry.COLUMN_RECIPE_NAME, mTitle);

        for (Ingredients ingredients : mRecipe.getIngredient()){
            values.put(IngredientsEntry.COLUMN_INGREDIENT_INGREDIENT, ingredients.getIngredient());
            values.put(IngredientsEntry.COLUMN_INGREDIENT_MEASURE, ingredients.getMeasure());
            values.put(IngredientsEntry.COLUMN_INGREDIENT_QUANTITY, ingredients.getQuantity());

            Uri mUri = getContentResolver().insert(IngredientsEntry.CONTENT_URI, values);

            if (mUri != null) {
                Toast.makeText(IngredientsActivity.this, mTitle + " " + getString(R.string.list_widget_added), Toast.LENGTH_SHORT).show();
                addToWidget.setText("Added to Widget!");
                imgAddFav.setImageResource(R.drawable.ic_favorite);
                isAdded = true;
                RecipeWidgetProvider.sendRefreshBroadcast(mContext);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void removeList() {
        Uri deleteUri = IngredientsEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf( mRecipe.getId())).build();
        int delete = getContentResolver().delete(deleteUri, null, null);
        if (delete != 0) {
            Toast.makeText(IngredientsActivity.this, mTitle + " " + getString(R.string.list_widget_removed), Toast.LENGTH_SHORT).show();
            addToWidget.setText("Add to Widget!");
            imgAddFav.setImageResource(R.drawable.ic_favorite_border);
            isAdded = false;
            RecipeWidgetProvider.sendRefreshBroadcast(mContext);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
