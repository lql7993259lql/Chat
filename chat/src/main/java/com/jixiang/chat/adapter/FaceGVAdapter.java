package com.jixiang.chat.adapter;

import java.util.List;

import com.jixiang.chat.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 表情GridView的adapter
 * @author  Wangda
 *
 */
public class FaceGVAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private static final String TAG = "FaceGVAdapter";
	private List<String> list;
	private Context mContext;

	public FaceGVAdapter(List<String> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
	}

	public void clear() {
		this.mContext = null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler hodler;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_face_image, null);
			hodler.iv = (ImageView) convertView.findViewById(R.id.face_img);
			hodler.tv = (TextView) convertView.findViewById(R.id.face_text);
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		try {
			String path = Environment.getExternalStorageDirectory()+"/"+"PngImage"+"/";
			if(!list.get(position).contains("shanchu")){
				Bitmap mBitmap = BitmapFactory.decodeFile(path+list.get(position));
				hodler.iv.setImageBitmap(mBitmap);
			}else{
				hodler.iv.setImageResource(R.drawable.chat_shanchu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		hodler.tv.setText(list.get(position));

		return convertView;
	}

	class ViewHodler {
		ImageView iv;
		TextView tv;
	}
}
