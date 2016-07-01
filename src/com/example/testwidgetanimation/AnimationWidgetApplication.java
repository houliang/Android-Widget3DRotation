package com.example.testwidgetanimation;

import android.app.Application;

import com.example.testutilshelperlibrary.process.ProcessUtils;
import com.example.testwidgetanimation.widget.WidgetUpdateGlobalImpl;

public class AnimationWidgetApplication extends Application {
	
	private static AnimationWidgetApplication sSelfInstance; 
	
	public static AnimationWidgetApplication getInstance() {
		return sSelfInstance;
	}
	
	public AnimationWidgetApplication() {
		sSelfInstance = this;
	}
	
	public void onCreate() {
		ProcessUtils.printPidInfo(this);
	}
	
	public void updateWidgetsId(int id, boolean addId) {
		if (addId) {
			WidgetUpdateGlobalImpl.getInstance().addWidgetId(id);
		} else {
			WidgetUpdateGlobalImpl.getInstance().removeWidgetId(id);
		}
	}
	
	public void handleEventWidgetsId(int id) {
		WidgetUpdateGlobalImpl.getInstance().updateWidgetId(id);
	}

}
