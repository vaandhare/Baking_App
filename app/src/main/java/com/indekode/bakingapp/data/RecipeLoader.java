package com.indekode.bakingapp.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.indekode.bakingapp.model.Recipe;
import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private String url;

    public RecipeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {

        if (url == null)
            return null;

        return BakingJson.fetchRecipeData(url);
    }
}