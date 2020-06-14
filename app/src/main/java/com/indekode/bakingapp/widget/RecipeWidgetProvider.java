package com.indekode.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.indekode.bakingapp.R;
import com.indekode.bakingapp.activity.IngredientsActivity;
import com.indekode.bakingapp.activity.MainActivity;


public class RecipeWidgetProvider extends AppWidgetProvider   {

    public static void sendRefreshBroadcast(Context context ) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        assert action != null;
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lvWidgetIngredients);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);

            String title = context.getString(R.string.appwidget_text);
            Intent intent = new Intent(context, WidgetRemoteViewService.class);
            views.setRemoteAdapter(R.id.lvWidgetIngredients, intent);
            views.setTextViewText(R.id.tvWidgetName, title );
            views.setEmptyView(R.id.lvWidgetIngredients, R.id.empty_view);

            Intent appIntent = new Intent(context, MainActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.empty_text_recipe_trigger, appPendingIntent);

            Intent ingred = new Intent(context, IngredientsActivity.class);
            PendingIntent appPendingIngre = PendingIntent.getActivity(context, 0, ingred, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.tvWidgetName, appPendingIngre);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}