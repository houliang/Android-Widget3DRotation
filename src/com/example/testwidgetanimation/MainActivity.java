package com.example.testwidgetanimation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.testwidgetanimation.animation.AnimationFrameUpdate;
import com.example.testwidgetanimation.animation.Rotate3DAnimationHelper;

public class MainActivity extends Activity implements AnimationFrameUpdate {
	
	private ImageView mImg1;
	private ImageView mImg2;
	private ImageView mImg3;
	private ImageView mAnimationImg;
	private Button mBtnMerge;
	private Button mBtnAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		init();
	}
	
	void init() {
		mImg1 = (ImageView)findViewById(R.id.img1);
		mImg2 = (ImageView)findViewById(R.id.img2);
		mImg3 = (ImageView)findViewById(R.id.img3);
		mAnimationImg = (ImageView)findViewById(R.id.animation_img);
		mBtnMerge = (Button)findViewById(R.id.merge);
		mBtnAnimation = (Button)findViewById(R.id.animation);
		mBtnMerge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rollImage();
			}
		});
		
		mBtnAnimation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Rotate3DAnimationHelper.start3DAnimation(MainActivity.this, R.id.animation_img, 
						mAnimationImg.getWidth()>>1, mAnimationImg.getHeight()>>1);
			}
		});
	}
	
	
	private void rollImage() {
		
		Bitmap img1 = ((BitmapDrawable) mImg1.getDrawable()).getBitmap();
		Bitmap img2 = ((BitmapDrawable) mImg2.getDrawable()).getBitmap();
		
		Bitmap mergeBmp = Bitmap.createBitmap(mImg3.getWidth(), mImg3.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mergeBmp);
		
		//从起点开始将img1绘制到canvas的指定位置(left,top)
		canvas.drawBitmap(img1, 0, mImg3.getHeight()>>1, null);
		canvas.drawBitmap(img2, 0, -mImg3.getHeight()>>1, null);
		
		mImg3.setImageBitmap(mergeBmp);
	}


	@Override
	public void onAnimationFrame(int id, Bitmap bitmap) {
		if (id != R.id.animation_img || bitmap == null || bitmap.isRecycled()) {
			return;
		}
		mAnimationImg.setImageBitmap(bitmap);
	}


}
