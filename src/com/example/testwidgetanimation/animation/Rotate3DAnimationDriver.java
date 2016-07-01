package com.example.testwidgetanimation.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.example.testutilshelperlibrary.log.LogPrinter;
import com.example.testwidgetanimation.AnimationWidgetApplication;
import com.example.testwidgetanimation.R;

public class Rotate3DAnimationDriver {
	private static final String TAG ="Rotate3DAnimationHelper";
	
	
	private static final int sMsgActionWhatDriver = 1;
	private static final int sMsgActionWhatEnd = 3;
	
	private int mWidgetId;
	private Handler mHandler;
	
	private float mFromDegrees;
	private float mToDegrees;
	private float mCenterX;
	private float mCenterY;
	private float mDepthZ;
	private boolean mReverse;
	private Camera mCamera;
	
	private int mDurationInMs = 1000;
	private long mAnmiStartTime;
	private boolean mStarted;
	
	private Canvas mCanvas;
	private Bitmap mCanvasBitmap;
	private Bitmap mSrcBitmap;
	private Context mContext;
	
	private AnimationFrameUpdate mAnimationFrameUpdateListener;
	
	public Rotate3DAnimationDriver(AnimationFrameUpdate listener) {
		mAnimationFrameUpdateListener = listener;
	}
	
	public void startAnimation(int id, float fromD, float toD, float centerX, float centerY, float depth) {
		
		LogPrinter.print(TAG, "statrAnimation");
		
		if (mStarted) {
			return;
		}
		
		long time = System.currentTimeMillis();
		mAnmiStartTime = time;
		mStarted = true;
		
		mReverse = false;
		
		mWidgetId = id;
		mFromDegrees = fromD;
		mToDegrees = toD;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depth;
		prepareAnimation();
		scheduleAnimation();
	}
	
	public void setAnimationTime(int duration) {
		mDurationInMs = duration;
	}
	
	public boolean isAnimationOver() {
		return !mStarted;
	}
	
	private void scheduleAnimation() {
		
		if (!mStarted) {
			return;
		}
		long curTime = System.currentTimeMillis();
		if (curTime > mAnmiStartTime + mDurationInMs) {
			mHandler.obtainMessage(sMsgActionWhatEnd).sendToTarget();
			return;
		}
		float progress = (curTime - mAnmiStartTime) /(float) mDurationInMs;

		mCanvasBitmap.eraseColor(0);
		Matrix matrix = applyTransformation(progress);
		mCanvas.drawBitmap(mSrcBitmap, matrix, null);
		
		if (mAnimationFrameUpdateListener != null) {
			mAnimationFrameUpdateListener.onAnimationFrame(mWidgetId, mCanvasBitmap);
		}
		
		mHandler.sendEmptyMessageAtTime(sMsgActionWhatDriver, SystemClock.uptimeMillis()+15);
		
		
		LogPrinter.print(TAG, "scheduleAnimation");
		
	}
	
	private void startReverseAnimation() {
		float tempDegree = mFromDegrees;
		mFromDegrees = -mToDegrees;
		mToDegrees = -tempDegree;
		mAnmiStartTime= System.currentTimeMillis();
		scheduleAnimation();
	}
	
	private void prepareAnimation() {
		
		LogPrinter.print(TAG, "prepareAnimation");
		
		if (mCanvas == null) {
			mCanvas = new Canvas();
		}
		
		if (mCamera == null) {
			mCamera = new Camera();
		}
		
		if (mHandler == null) {
			mHandler = new InnerHandler();
		}
		
		if (mSrcBitmap == null) {
			mSrcBitmap = ((BitmapDrawable)getContext().getResources().getDrawable(R.drawable.img1_1)).getBitmap();
		}
		if (mCanvasBitmap == null) {
			mCanvasBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), Config.ARGB_8888);
		}
		mCanvas.setBitmap(mCanvasBitmap);
	}
	
    protected Matrix applyTransformation(float interpolatedTime) {  
        final float fromDegrees = mFromDegrees;  
        // 生成中间角度  
        float degrees = fromDegrees  
                + ((mToDegrees - fromDegrees) * interpolatedTime);  
  
        final float centerX = mCenterX;  
        final float centerY = mCenterY;  
        final Camera camera = mCamera;  
  
        final Matrix matrix = new Matrix();  
  
        camera.save();  
        if (mReverse) {  
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);  
        } else {  
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));  
        }  
        camera.rotateY(degrees);  
        camera.getMatrix(matrix);  
        camera.restore();  
  
        matrix.preTranslate(-centerX, -centerY);  
        matrix.postTranslate(centerX, centerY);  
        
        return matrix;
    }
    
	private Context getContext() {
		if (mContext == null) {
			mContext = AnimationWidgetApplication.getInstance().getApplicationContext();
		}
		return mContext;
	} 
	
    private class InnerHandler extends Handler {
        public void handleMessage(Message msg) {

        	switch (msg.what) {
        	case sMsgActionWhatDriver:
        		scheduleAnimation();
        		break;
        	case sMsgActionWhatEnd:
        		handleAnimationEnd();
        		break;
        	}
        } 
        
        private void handleAnimationEnd() {
        	if (!mReverse) {
        		mReverse = true;
        		startReverseAnimation();
        		return;
        	}
        	if (mCanvasBitmap != null) {
        		mCanvasBitmap.recycle();
        		mCanvasBitmap = null;
        	}
        	mReverse = false;
        	mStarted = false;
        }
    }

}
