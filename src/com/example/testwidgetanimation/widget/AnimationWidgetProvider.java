package com.example.testwidgetanimation.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.testutilshelperlibrary.log.LogPrinter;
import com.example.testwidgetanimation.AnimationWidgetApplication;

public class AnimationWidgetProvider extends AppWidgetProvider {
	
	private static final String TAG = "AnimationWidgetProvider";
	
	public void onReceive(Context context, Intent intent) {
		LogPrinter.print(TAG, "onReceive action:", intent.getAction());
		super.onReceive(context, intent); 
		
		if (intent.getAction().startsWith(WidgetUpdateGlobalImpl.ACTION_PREFIX)) {
			int id = intent.getIntExtra(WidgetUpdateGlobalImpl.EXTRA_WIDGET_ID, 0);
			((AnimationWidgetApplication) context.getApplicationContext()).handleEventWidgetsId(id);
		}
	}
	
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	LogPrinter.print(TAG, "onUpdate");
    	
    	for (int id:appWidgetIds) {
    		((AnimationWidgetApplication) context.getApplicationContext()).updateWidgetsId(id, true);
    	}
    }
    
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, Bundle newOptions) {
    	LogPrinter.print(TAG, "onAppWidgetOptionsChanged");
    }
    
    public void onDeleted(Context context, int[] appWidgetIds) {
    	LogPrinter.print(TAG, "onDeleted");
    	for (int id:appWidgetIds) {
    		((AnimationWidgetApplication) context.getApplicationContext()).updateWidgetsId(id, false);
    	}
    }
    
    public void onEnabled(Context context) {
    	LogPrinter.print(TAG, "onEnabled");
    }
    
    public void onDisabled(Context context) {
    	LogPrinter.print(TAG, "onDisabled");
    }
}
