package com.indekode.bakingapp.data;

import android.text.TextUtils;
import android.util.Log;
import com.indekode.bakingapp.model.Ingredients;
import com.indekode.bakingapp.model.Recipe;
import com.indekode.bakingapp.model.Steps;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class BakingJson {

    private static final String LOG_TAG = BakingJson.class.getSimpleName();

    static List<Recipe> fetchRecipeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractResultFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the recipe JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Recipe> extractResultFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse))
            return null;

        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray root = new JSONArray(jsonResponse);
            for (int i = 0; i < root.length(); i++) {

                JSONObject currentRecipe = root.getJSONObject(i);

                int id = currentRecipe.getInt("id");
                String name = currentRecipe.getString("name");
                String servings = currentRecipe.getString("servings");
                JSONArray ingredients = currentRecipe.getJSONArray("ingredients");
                List<Ingredients> ingredientsList = jsonIngredients(ingredients);
                JSONArray steps = currentRecipe.getJSONArray("steps");
                List<Steps> stepsList = jsonSteps(steps);
                Recipe recipe = new Recipe(id, name, servings, ingredientsList, stepsList);

                recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private static List<Ingredients> jsonIngredients(JSONArray jsonArray) throws JSONException {

        List<Ingredients> recipes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject currentIngredients = jsonArray.getJSONObject(i);

            String quantity = currentIngredients.getString("quantity");
            String measure = currentIngredients.getString("measure");
            String ingredient = currentIngredients.getString("ingredient");
            Ingredients recipe = new Ingredients(quantity, measure, ingredient);
            recipes.add(recipe);
        }
        return recipes;
    }

    private static List<Steps> jsonSteps(JSONArray jsonArray) throws JSONException {

        List<Steps> steps = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); j++) {

            JSONObject currentSteps = jsonArray.getJSONObject(j);
            String shortDescription = currentSteps.getString("shortDescription");
            String description = currentSteps.getString("description");
            String thumbnailURL = currentSteps.getString("thumbnailURL");
            String videoURL = currentSteps.getString("videoURL");

            Steps step = new Steps(shortDescription, description, videoURL, thumbnailURL);
            steps.add(step);
        }
        return steps;
    }
}