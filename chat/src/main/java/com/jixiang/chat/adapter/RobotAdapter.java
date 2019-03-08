package com.jixiang.chat.adapter;

import java.util.List;

import com.jixiang.chat.R;
import com.jixiang.chat.entity.Robot;
import com.jixiang.chat.ui.MainActivity;
import com.jixiang.chat.util.ToastControll;
import com.jixiang.chat.util.UBB;
import com.jixiang.chat.util.UrlSpanUBB;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 显示机器人的listview的adapter
 * 
 * @author Wangda
 */
public class RobotAdapter extends BaseAdapter {
	private List<Robot> robotList;
	private Context context;
	private LayoutInflater inflater;
	private Handler handler;
	private int opstionsCount;
	private int mMaxWidth;

	public RobotAdapter(Context context, List<Robot> robotList, Handler handler) {
		this.context = context;
		this.robotList = robotList;
		this.handler = handler;
		this.opstionsCount = opstionsCount;
		inflater = LayoutInflater.from(context);
		// 获取屏幕的宽度
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		// 最大宽度为屏幕宽度的百分之七十
		mMaxWidth = (int) (outMetrics.widthPixels * 0.3f);
	}

	@Override
	public int getCount() {
		return robotList.size();
	}

	@Override
	public Robot getItem(int position) {
		return robotList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			String dos = ". ";
			final Robot robot = getItem(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.chat_robot_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.tv_robot_title);
				holder.no = (TextView) convertView.findViewById(R.id.tv_no);
				holder.yes = (TextView) convertView.findViewById(R.id.tv_yes);
				holder.know = (TextView) convertView.findViewById(R.id.tv_no_know);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(robot.getIsList() != 2){
				if (robot.getBody().contains("[url")) {
					SpannableStringBuilder text = new UrlSpanUBB(context, holder.title)
							.updateClickSpan(Html.fromHtml(UBB.UbbDecode(robot.getSort() + dos + robot.getBody())));
					holder.title.setText(text);
				} else {
					holder.title.setText(Html.fromHtml(UBB.UbbDecode(robot.getSort() + dos + robot.getBody())));
				}
			}
			holder.title.setMovementMethod(LinkMovementMethod.getInstance());
			if (robot.isOptions()) {
				int size = robot.getOptions().size();
				holder.title.setTextColor(context.getResources().getColor(R.color.chat_from_content));
				if (size >= 1) {
					holder.no.setVisibility(View.VISIBLE);
					holder.no.setMovementMethod(LinkMovementMethod.getInstance());
					holder.no.setMaxWidth(mMaxWidth);
					if (robot.getOptions().get(0).getText() != null) {
						holder.no.setText(Html.fromHtml(UBB.UbbDecode(robot.getOptions().get(0).getText())));
						holder.no.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									if (!MainActivity.isLoading) {
										MainActivity.isLoading = true;
										Message msg = handler.obtainMessage();
										msg.what = MainActivity.ROBOTREQUEST;
										msg.arg1 = robot.getOptions().get(0).getMsgid();
										msg.obj = robot.getOptions().get(0).getBody();
										handler.sendMessage(msg);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						holder.no.setVisibility(View.GONE);
					}
				} else {
					holder.no.setVisibility(View.GONE);
				}
				if (size >= 2) {
					holder.yes.setVisibility(View.VISIBLE);
					holder.yes.setMovementMethod(LinkMovementMethod.getInstance());
					holder.yes.setMaxWidth(mMaxWidth);
					if (robot.getOptions().get(1).getText() != null) {
						holder.yes.setText(Html.fromHtml(UBB.UbbDecode(robot.getOptions().get(1).getText())));
						holder.yes.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									if (!MainActivity.isLoading) {
										MainActivity.isLoading = true;
										Message msg = handler.obtainMessage();
										msg.what = MainActivity.ROBOTREQUEST;
										msg.arg1 = robot.getOptions().get(1).getMsgid();
										msg.obj = robot.getOptions().get(1).getBody();
										handler.sendMessage(msg);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						holder.yes.setVisibility(View.GONE);
					}
				} else {
					holder.yes.setVisibility(View.GONE);
				}
				if (size >= 3) {
					holder.know.setVisibility(View.VISIBLE);
					holder.know.setMovementMethod(LinkMovementMethod.getInstance());
					if (robot.getOptions().get(2).getText() != null) {
						holder.know.setText(Html.fromHtml(UBB.UbbDecode(robot.getOptions().get(2).getText())));
						holder.know.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									if (!MainActivity.isLoading) {
										MainActivity.isLoading = true;
										Message msg = handler.obtainMessage();
										msg.what = MainActivity.ROBOTREQUEST;
										msg.arg1 = robot.getOptions().get(2).getMsgid();
										msg.obj = robot.getOptions().get(2).getBody();
										handler.sendMessage(msg);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						holder.know.setVisibility(View.GONE);
					}

				} else {
					holder.know.setVisibility(View.GONE);
				}
			} else {
				holder.no.setVisibility(View.GONE);
				holder.yes.setVisibility(View.GONE);
				holder.know.setVisibility(View.GONE);
				if (robot.getIsList() == 1) {
					holder.title.setTextColor(context.getResources().getColor(R.color.chat_kefutextcolor));
					holder.title.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								if (!MainActivity.isLoading) {
									MainActivity.isLoading = true;
									Message msg = handler.obtainMessage();
									msg.what = MainActivity.ROBOTREQUEST;
									msg.arg1 = robot.getMsgid();
									msg.obj = robot.getBody();
									handler.sendMessage(msg);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} else {
					if (11 == robot.getMsgid()) {
						holder.title.setTextColor(context.getResources().getColor(R.color.chat_from_content));
						/*
						 * Spanned sp =
						 * Html.fromHtml(UBB.UbbDecode(robot.getBody()));
						 * SpannableStringBuilder text = new UrlSpanUBB(context,
						 * holder.title) .updateClickSpan(sp);
						 * holder.title.setText(text);
						 */
						holder.title.setText(Html.fromHtml(UBB.UbbDecode(robot.getBody())));
					} else {
						holder.title.setTextColor(context.getResources().getColor(R.color.chat_kefutextcolor));
						holder.title.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									if (!MainActivity.isLoading) {
										MainActivity.isLoading = true;
										Message msg = handler.obtainMessage();
										msg.what = MainActivity.ROBOTREQUEST;
										msg.arg1 = robot.getMsgid();
										msg.obj = robot.getBody();
										handler.sendMessage(msg);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		private TextView title;
		private TextView yes;
		private TextView no;
		private TextView know;
	}
}
