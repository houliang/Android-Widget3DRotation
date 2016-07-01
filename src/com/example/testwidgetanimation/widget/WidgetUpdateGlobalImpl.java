package com.example.testwidgetanimation.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.testutilshelperlibrary.singleton.Singleton;
import com.example.testwidgetanimation.AnimationWidgetApplication;
import com.example.testwidgetanimation.R;
import com.example.testwidgetanimation.animation.AnimationFrameUpdate;
import com.example.testwidgetanimation.animation.Rotate3DAnimationDriver;
import com.example.testwidgetanimation.animation.Rotate3DAnimationHelper;

public class WidgetUpdateGlobalImpl implements WidgetUpdate, AnimationFrameUpdate {
	
	private static final String TAG = "WidgetUpdateGlobalImpl";
	
	public static final String ACTION_PREFIX = "com.example.testwidgetanimation.widget";
	
	public static final String INTENT_ON_CLICK_FORMAT = ACTION_PREFIX + ".id.%d.click";
	
	public static final String EXTRA_WIDGET_ID = "widgetId";
	
	
	private Context mContext;
	private List<Integer> mWidgetIdList;
	
	public static WidgetUpdateGlobalImpl getInstance() {
		return sSingletonInstance.getInstance();
	}
	
	private WidgetUpdateGlobalImpl() {
		mWidgetIdList = new ArrayList<Integer>();
	}

	@Override
	public void addWidgetId(int id) {
		if (!mWidgetIdList.contains(id)) {
			mWidgetIdList.add(id);
		}
		initWidgetId(id);
	}

	@Override
	public void removeWidgetId(int id) {
		if (mWidgetIdList.contains(id)) {
			mWidgetIdList.remove((Integer)id);
			return;
		}
	}

	@Override
	public void updateWidgetId(int id) {
		Toast.makeText(getContext(), "updateWidgetId:"+id, Toast.LENGTH_SHORT).show();
		start3DRotateAnimation(id);
	}
	
	@Override
	public void onAnimationFrame(int id, Bitmap bitmap) {
		sendRemoteViewBitmap(id, bitmap);
	}
	
	private void start3DRotateAnimation(int id) {
		int centerX = getContext().getResources().getDimensionPixelSize(R.dimen.animation_widget_min_width);
		int centerY = getContext().getResources().getDimensionPixelSize(R.dimen.animation_widget_min_height);
		Rotate3DAnimationHelper.start3DAnimation(this, id, centerX>>1, centerY>>1);
	}
	
	private Context getContext() {
		if (mContext == null) {
			mContext = AnimationWidgetApplication.getInstance().getApplicationContext();
		}
		return mContext;
	} 
	
	private PendingIntent getClickIntent(int widgetId) {
		Intent intent = new Intent(String.format(INTENT_ON_CLICK_FORMAT, widgetId));
		intent.setClass(getContext(), AnimationWidgetProvider.class);
		intent.putExtra(EXTRA_WIDGET_ID, widgetId);
		PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pi;
	}
	
	private void initWidgetId(int id) {
		Context context = getContext();
		RemoteViews rviews = new RemoteViews(context.getPackageName(), R.layout.animation_widget_layout);
		rviews.setImageViewResource(R.id.widget_img, R.drawable.img1_1);
		rviews.setOnClickPendingIntent(R.id.widget_img, getClickIntent(id));
		AppWidgetManager.getInstance(context).updateAppWidget(id, rviews);
	}
	
	private void sendRemoteViewBitmap(int id, Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return;
		}
		Context context = getContext();
		RemoteViews rviews = new RemoteViews(context.getPackageName(), R.layout.animation_widget_layout);
		rviews.setImageViewBitmap(R.id.widget_img, bitmap);
		AppWidgetManager.getInstance(context).updateAppWidget(id, rviews);
	}

	private static final Singleton<WidgetUpdateGlobalImpl> sSingletonInstance = new Singleton<WidgetUpdateGlobalImpl>(){
		@Override
		protected WidgetUpdateGlobalImpl newInstance() {
			return new WidgetUpdateGlobalImpl();
		}
	};


}
