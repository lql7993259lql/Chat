package com.jixiang.chat.animation;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.jixiang.chat.util.DisplayUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AnimatedGifDrawable extends AnimationDrawable {
	private Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
	private int mCurrentIndex = 0;
	private UpdateListener mListener;

	public AnimatedGifDrawable(InputStream source, Context context, UpdateListener listener) {
		mListener = listener;
		GifDecoder decoder = new GifDecoder();
		decoder.read(source);
		// Iterate through the gif frames, add each as animation frame
		for (int i = 0; i < decoder.getFrameCount(); i++) {
			Bitmap bitmap = decoder.getFrame(i);
			@SuppressWarnings("deprecation")
			BitmapDrawable drawable = new BitmapDrawable(bitmap);
			// Explicitly set the bounds in order for the frames to display
			drawable.setBounds(0, 0, DisplayUtil.sp2px(context, 25f), DisplayUtil.sp2px(context, 25f));
			addFrame(drawable, decoder.getDelay(i));
			if (i == 0) {
				// Also set the bounds for this container drawable
				setBounds(0, 0, DisplayUtil.sp2px(context, 25f), DisplayUtil.sp2px(context, 25f));
			}
		}
	}


	/**
	 * Naive method to proceed to next frame. Also notifies listener.
	 */
	public void nextFrame() {
		mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
		if (mListener != null)
			mListener.update();
	}

	/**
	 * Return display duration for current frame
	 */
	public int getFrameDuration() {
		return getDuration(mCurrentIndex);
	}

	/**
	 * Return drawable for current frame
	 */
	public Drawable getDrawable() {
		return getFrame(mCurrentIndex);
	}

	/**
	 * Interface to notify listener to update/redraw Can't figure out how to
	 * invalidate the drawable (or span in which it sits) itself to force redraw
	 */
	public interface UpdateListener {
		void update();
	}

	public Bitmap getByCache(String path) {
		SoftReference<Bitmap> ref = cache.get(path);
		if (ref == null) {
			return null;
		}
		Bitmap bitmap = ref.get();
		return bitmap;
	}

	public void savaToCache(String path, Bitmap bitmap) {
		SoftReference<Bitmap> ref = new SoftReference<Bitmap>(bitmap);
		cache.put(path, ref);
	}

}