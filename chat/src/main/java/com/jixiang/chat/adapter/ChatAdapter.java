package com.jixiang.chat.adapter;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jixiang.chat.R;
import com.jixiang.chat.entity.ChatEntity;
import com.jixiang.chat.entity.Emoji;
import com.jixiang.chat.ui.ImageActivity;
import com.jixiang.chat.ui.MainActivity;
import com.jixiang.chat.ui.PlayVideoActivity;
import com.jixiang.chat.util.DisplayUtil;
import com.jixiang.chat.util.ImageUtil;
import com.jixiang.chat.util.ToastControll;
import com.jixiang.chat.util.UBB;
import com.jixiang.chat.voice.MediaPlayerManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天内容的adapter
 * 
 * @author Wangda
 * 
 */
@SuppressLint("NewApi")
public class ChatAdapter extends BaseAdapter {
	private static Context context;
	private List<ChatEntity> chatList;
	private List<Emoji> emojiList;
	private LayoutInflater Inflater;

	private static int fixwidth = 104;

	private int COME_MSG = 0;
	private int TO_MSG = 1;
	// item的最小宽度
	private int mMinWidth;
	// item的最大宽度
	private int mMaxWidth;
	private int headMaxWidth;
	// 执行动画的时间
	protected long mAnimationTime = 150;
	private AudioManager mAudioManager;
	private boolean isMode = false;
	// 判断语音是否播放
	private boolean isPlay = false;
	private ChatHolder holder;
	private int frist;
	private int second;
	private int three;
	private int four;
	private View view;
	public static boolean isSuccess = false;
	private int max;
	private Bitmap avatarbitmap;
	private Bitmap serverbitmap;
	private boolean islistFirst = true;
	private Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

	public ChatAdapter(Context context, List<ChatEntity> chatList) {
		super();
		this.context = context;
		this.chatList = chatList;
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		// 获取屏幕的宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		headMaxWidth = (int) (outMetrics.widthPixels / 10 * 9);
		max = (int) (outMetrics.widthPixels / 10 * 6.5f);
		// 最大宽度为屏幕宽度的百分之七十
		mMaxWidth = (int) (outMetrics.widthPixels * 0.7f);
		// 最大宽度为屏幕宽度的百分之十五
		mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
		Inflater = LayoutInflater.from(this.context);
	}

	public void setList(List<ChatEntity> list) {
		this.chatList = list;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public ChatEntity getItem(int position) {
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatEntity entity = chatList.get(position);
		if (entity.isComeMsg()) {
			return COME_MSG;
		} else {
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				holder = new ChatHolder();
				convertView = Inflater.inflate(R.layout.chat_all_item2, null);
				holder.fromLength = convertView
						.findViewById(R.id.id_recoder_lenght_from);
				holder.toLength = convertView
						.findViewById(R.id.id_recoder_lenght_to);
				holder.fromTv = (TextView) holder.fromLength
						.findViewById(R.id.tv_from_content);
				holder.toTv = (TextView) holder.toLength
						.findViewById(R.id.tv_to_content);
				holder.fromSeconds = (TextView) convertView
						.findViewById(R.id.id_recoder_time_from);
				holder.toSeconds = (TextView) convertView
						.findViewById(R.id.id_recoder_time_to);
				holder.fromIv = (ImageView) convertView
						.findViewById(R.id.iv_server_image);
				holder.toIv = (ImageView) convertView
						.findViewById(R.id.iv_user_image);
				holder.animViewFrom = convertView
						.findViewById(R.id.id_recoder_anim_from);
				holder.animViewTo = convertView
						.findViewById(R.id.id_recoder_anim_to);
				holder.timeTo = (TextView) convertView
						.findViewById(R.id.tv_time_to);
				holder.timeFrom = (TextView) convertView
						.findViewById(R.id.tv_time_from);
				holder.head = (TextView) convertView.findViewById(R.id.tv_head);
				holder.lvRobot = (ListView) holder.fromLength
						.findViewById(R.id.lv_robot);		
				holder.serverName = (TextView) convertView
						.findViewById(R.id.tv_server_name);
				holder.pbSmall = (ProgressBar) convertView
						.findViewById(R.id.pb_small);
				convertView.setTag(holder);
				
			} else {
				holder = (ChatHolder) convertView.getTag();
			}
			if (chatList.get(position).isLoading()) {
				holder.fromIv.setVisibility(View.GONE);
				holder.fromLength.setVisibility(View.GONE);
				holder.fromSeconds.setVisibility(View.GONE);
				holder.fromTv.setVisibility(View.GONE);
				holder.animViewFrom.setVisibility(View.GONE);
				holder.timeFrom.setVisibility(View.GONE);
				holder.toIv.setVisibility(View.GONE);
				holder.toLength.setVisibility(View.GONE);
				holder.toSeconds.setVisibility(View.GONE);
				holder.toTv.setVisibility(View.GONE);
				holder.animViewTo.setVisibility(View.GONE);
				holder.timeTo.setVisibility(View.GONE);
				holder.lvRobot.setVisibility(View.GONE);
				holder.serverName.setVisibility(View.GONE);
				holder.pbSmall.setVisibility(View.VISIBLE);
				holder.head.setVisibility(View.GONE);
				return convertView;
			}
			if (chatList.get(position).isComeMsg()) {
				if (chatList.get(position).isTip()) {
					if (chatList.get(position).getTipMsg() != null
							&& chatList.get(position).getTipMsg() != ""
							&& chatList.get(position).getTipMsg() != " ") {
						holder.fromIv.setVisibility(View.GONE);
						holder.fromLength.setVisibility(View.GONE);
						holder.fromSeconds.setVisibility(View.GONE);
						holder.fromTv.setVisibility(View.GONE);
						holder.animViewFrom.setVisibility(View.GONE);
						holder.timeFrom.setVisibility(View.GONE);
						holder.toIv.setVisibility(View.GONE);
						holder.toLength.setVisibility(View.GONE);
						holder.toSeconds.setVisibility(View.GONE);
						holder.toTv.setVisibility(View.GONE);
						holder.animViewTo.setVisibility(View.GONE);
						holder.timeTo.setVisibility(View.GONE);
						holder.lvRobot.setVisibility(View.GONE);
						holder.serverName.setVisibility(View.GONE);
						holder.pbSmall.setVisibility(View.GONE);
						holder.head.setVisibility(View.VISIBLE);
						holder.head.setMaxHeight(headMaxWidth);
						holder.head
								.setText(Html.fromHtml(UBB.UbbDecode(chatList
										.get(position).getTipMsg())));
					}
				} else {
					if (chatList.get(position).isYuYin()) {
						holder.fromIv.setVisibility(View.VISIBLE);
						holder.fromLength.setVisibility(View.VISIBLE);
						holder.fromSeconds.setVisibility(View.VISIBLE);
						holder.fromTv.setVisibility(View.VISIBLE);
						holder.animViewFrom.setVisibility(View.VISIBLE);
						holder.timeFrom.setVisibility(View.VISIBLE);
						holder.fromTv.setTag("position3");
						holder.animViewFrom.setTag("position3");
						holder.toIv.setVisibility(View.GONE);
						holder.toLength.setVisibility(View.GONE);
						holder.toSeconds.setVisibility(View.GONE);
						holder.timeTo.setVisibility(View.GONE);
						holder.head.setVisibility(View.GONE);
						holder.lvRobot.setVisibility(View.GONE);
						holder.pbSmall.setVisibility(View.GONE);
						if (MainActivity.isOnService) {
							if (chatList.get(position).isHistory()) {
								holder.serverName.setVisibility(View.VISIBLE);
								if (chatList.get(position).getHistory()
										.getNickname() != null
										|| chatList.get(position).getHistory()
												.getNickname() != ""
										|| chatList.get(position).getHistory()
												.getNickname() != "null") {
									holder.serverName.setText(chatList
											.get(position).getHistory()
											.getNickname());
								}
								if (chatList.get(position).getHistory()
										.getBitmap() != null) {
									holder.fromIv.setImageBitmap(chatList
											.get(position).getHistory()
											.getBitmap());
								}
							} else {
								holder.serverName.setVisibility(View.VISIBLE);
								if (chatList.get(position).getServerName() != null) {
									holder.serverName.setText(chatList.get(
											position).getServerName());
								}
								if (chatList.get(position).getServerAvatar() != null) {
									holder.fromIv.setImageBitmap(chatList.get(
											position).getServerAvatar());
								}
							}
						}
						holder.fromTv.setText("");
						holder.fromLength.setBackgroundResource(0);
						holder.fromLength.setPaddingRelative(0, 0, 0, 0);
						holder.fromSeconds.setText(Math.round(chatList.get(
								position).getTime())
								+ "\"");
						holder.timeFrom.setText(chatList.get(position)
								.getChatTime());
						ViewGroup.LayoutParams lp = holder.fromTv
								.getLayoutParams();
						if (lp.width < max) {
							lp.width = (int) (mMinWidth + (mMaxWidth / 100f)
									* chatList.get(position).getTime());
						} else {
							lp.width = max;
						}
						TextView tv = (TextView) holder.fromTv
								.findViewWithTag("position3");
						final View anim = holder.animViewFrom
								.findViewWithTag("position3");
						tv.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (isPlay) {
									four = position + 3;
									if (four != three) {
										MediaPlayerManager.pause();
										MediaPlayerManager.release();
										if (view != null) {
											view.setBackgroundResource(R.drawable.chat_v_anim6);
										}
										// 声音播放动画
										anim.setBackgroundResource(R.drawable.chat_play_from_anim);
										AnimationDrawable animation = (AnimationDrawable) anim
												.getBackground();
										animation.start();
										isPlay = true;
										three = position + 3;
										view = anim;
										// 播放录音
										MediaPlayerManager
												.playSound(
														chatList.get(position)
																.getFilePath(),
														new MediaPlayer.OnCompletionListener() {

															public void onCompletion(
																	MediaPlayer mp) {
																// 播放完成后修改图片
																anim.setBackgroundResource(R.drawable.chat_v_anim6);
															}
														});
									} else {
										MediaPlayerManager.pause();
										MediaPlayerManager.release();
										anim.setBackgroundResource(R.drawable.chat_v_anim6);
										isPlay = false;
									}
								} else {
									// 声音播放动画
									anim.setBackgroundResource(R.drawable.chat_play_to_anim);
									AnimationDrawable animation = (AnimationDrawable) anim
											.getBackground();
									animation.start();
									isPlay = true;
									frist = position + 3;
									view = anim;
									// 播放录音
									MediaPlayerManager
											.playSound(
													chatList.get(position)
															.getFilePath(),
													new MediaPlayer.OnCompletionListener() {

														public void onCompletion(
																MediaPlayer mp) {
															// 播放完成后修改图片
															anim.setBackgroundResource(R.drawable.chat_adj);
														}
													});

								}
							}
						});
						if (isMode) {
							tv.setOnLongClickListener(new YuYin1Dialog(
									convertView, position, chatList.get(
											position).isComeMsg()));
						} else {
							tv.setOnLongClickListener(new YuYinDialog(
									convertView, position, chatList.get(
											position).isComeMsg()));
						}
					} else {
						if (chatList.get(position).isPhoto()) {
							holder.fromIv.setVisibility(View.VISIBLE);
							holder.fromLength.setVisibility(View.VISIBLE);
							holder.fromTv.setVisibility(View.VISIBLE);
							holder.timeFrom.setVisibility(View.VISIBLE);
							holder.fromTv.setTag("position2");
							holder.toIv.setVisibility(View.GONE);
							holder.toLength.setVisibility(View.GONE);
							holder.toSeconds.setVisibility(View.GONE);
							holder.fromSeconds.setVisibility(View.GONE);
							holder.animViewFrom.setVisibility(View.GONE);
							holder.timeTo.setVisibility(View.GONE);
							holder.head.setVisibility(View.GONE);
							holder.lvRobot.setVisibility(View.GONE);
							holder.pbSmall.setVisibility(View.GONE);
							if (MainActivity.isOnService) {
								if (chatList.get(position).isHistory()) {
									holder.serverName
											.setVisibility(View.VISIBLE);
									if (chatList.get(position).getHistory()
											.getNickname() != null
											|| chatList.get(position)
													.getHistory().getNickname() != "") {
										holder.serverName.setText(chatList
												.get(position).getHistory()
												.getNickname());
									}
									if (chatList.get(position).getHistory()
											.getBitmap() != null) {
										holder.fromIv.setImageBitmap(chatList
												.get(position).getHistory()
												.getBitmap());
									}
								} else {
									holder.serverName
											.setVisibility(View.VISIBLE);
									if (chatList.get(position).getServerName() != null) {
										holder.serverName.setText(chatList.get(
												position).getServerName());
									}
									if (chatList.get(position)
											.getServerAvatar() != null) {
										holder.fromIv.setImageBitmap(chatList
												.get(position)
												.getServerAvatar());
									}
								}
							}
							holder.fromTv.setText("");
							holder.fromLength.setBackgroundResource(0);
							holder.fromLength.setPaddingRelative(0, 0, 0, 0);
							ViewGroup.LayoutParams lp = holder.fromTv
									.getLayoutParams();
							lp.width = LayoutParams.WRAP_CONTENT;
							holder.fromTv.setMaxWidth((int) (max * 3.2 / 4));
							holder.fromTv.setMaxHeight((int) (max * 3.2 / 4));
							holder.fromTv.setText(chatList.get(position)
									.getBitmap());
							holder.timeFrom.setText(chatList.get(position)
									.getChatTime());
							TextView tv = (TextView) holder.fromTv
									.findViewWithTag("position2");
							tv.setOnLongClickListener(new ImageDialog(
									convertView, position, chatList.get(
											position).isComeMsg()));
							tv.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									if (chatList.get(position).isVideo()) {
										if (isPlay) {
											MediaPlayerManager.pause();
											MediaPlayerManager.release();
											if (view != null) {
												view.setBackgroundResource(R.drawable.chat_adj);
											}
											Intent intent = new Intent(context,
													PlayVideoActivity.class);
											intent.putExtra("a",
													chatList.get(position)
															.getVideoPath());
											context.startActivity(intent);
											isPlay = false;
										} else {
											Intent intent = new Intent(context,
													PlayVideoActivity.class);
											intent.putExtra("a",
													chatList.get(position)
															.getVideoPath());
											context.startActivity(intent);
										}
									} else {
										if (isPlay) {
											MediaPlayerManager.pause();
											MediaPlayerManager.release();
											if (view != null) {
												view.setBackgroundResource(R.drawable.chat_adj);
											}
											Intent intent = new Intent(context,
													ImageActivity.class);
											intent.putExtra("a",
													chatList.get(position)
															.getPhotoPath());
											context.startActivity(intent);
											isPlay = false;
										} else {
											Intent intent = new Intent(context,
													ImageActivity.class);
											intent.putExtra("a",
													chatList.get(position)
															.getPhotoPath());
											context.startActivity(intent);
										}
									}
								}
							});
						} else {
							if (chatList.get(position).isRobot()) {
								holder.fromIv.setVisibility(View.VISIBLE);
								holder.fromLength.setVisibility(View.VISIBLE);
								holder.fromTv.setVisibility(View.GONE);
								holder.timeFrom.setVisibility(View.VISIBLE);
								holder.toIv.setVisibility(View.GONE);
								holder.toLength.setVisibility(View.GONE);
								holder.toSeconds.setVisibility(View.GONE);
								holder.fromSeconds.setVisibility(View.GONE);
								holder.animViewFrom.setVisibility(View.GONE);
								holder.timeTo.setVisibility(View.GONE);
								holder.head.setVisibility(View.GONE);
								holder.serverName.setVisibility(View.GONE);
								holder.pbSmall.setVisibility(View.GONE);
								holder.lvRobot.setVisibility(View.VISIBLE);
								holder.lvRobot.setTag("robot");
								if (serverbitmap != null) {
									holder.fromIv.setImageBitmap(serverbitmap);
								}
								holder.timeFrom.setText(chatList.get(position)
										.getChatTime());
								holder.fromLength
										.setBackgroundResource(R.drawable.chat_wwwww);
								holder.fromLength.setPaddingRelative(
										mMinWidth / 4, 0, 0, 0);
								ListView lv = (ListView) holder.lvRobot
										.findViewWithTag("robot");
								lv.setAdapter(chatList.get(position)
										.getAdapter());
								setListViewHeight(lv);
							} else {
								holder.fromIv.setVisibility(View.VISIBLE);
								holder.fromLength.setVisibility(View.VISIBLE);
								holder.fromTv.setVisibility(View.VISIBLE);
								holder.timeFrom.setVisibility(View.VISIBLE);
								holder.fromTv.setTag("position1");
								holder.toIv.setVisibility(View.GONE);
								holder.toLength.setVisibility(View.GONE);
								holder.toSeconds.setVisibility(View.GONE);
								holder.fromSeconds.setVisibility(View.GONE);
								holder.animViewFrom.setVisibility(View.GONE);
								holder.timeTo.setVisibility(View.GONE);
								holder.head.setVisibility(View.GONE);
								holder.lvRobot.setVisibility(View.GONE);
								holder.pbSmall.setVisibility(View.GONE);
								if (MainActivity.isOnService) {
									if (chatList.get(position).isHistory()) {
										holder.serverName
												.setVisibility(View.VISIBLE);
										if (chatList.get(position).getHistory()
												.getNickname() != null
												|| chatList.get(position)
														.getHistory()
														.getNickname() != "") {
											holder.serverName.setText(chatList
													.get(position).getHistory()
													.getNickname());
										}
										if (chatList.get(position).getHistory()
												.getBitmap() != null) {
											holder.fromIv
													.setImageBitmap(chatList
															.get(position)
															.getHistory()
															.getBitmap());
										}
									} else {
										holder.serverName
												.setVisibility(View.VISIBLE);
										if (chatList.get(position)
												.getServerName() != null) {
											holder.serverName.setText(chatList
													.get(position)
													.getServerName());
										}
										if (chatList.get(position)
												.getServerAvatar() != null) {
											holder.fromIv
													.setImageBitmap(chatList
															.get(position)
															.getServerAvatar());
										}
									}
								} else {
									holder.serverName.setVisibility(View.GONE);
									if (serverbitmap != null) {
										holder.fromIv
												.setImageBitmap(serverbitmap);
									}
								}
								holder.fromTv.setText("");
								holder.fromLength.setBackgroundResource(0);
								holder.fromLength
										.setPaddingRelative(0, 0, 0, 0);
								// 对内容做处理
								SpannableStringBuilder sb = handler(
										holder.fromTv, chatList.get(position)
												.getContent(), position);
								holder.fromTv.setMaxWidth(max);
								holder.fromTv.setText(sb);
								holder.timeFrom.setText(chatList.get(position)
										.getChatTime());
								TextView tv = (TextView) holder.fromTv
										.findViewWithTag("position1");
								tv.setOnLongClickListener(new WenZiDialog(
										convertView, position, chatList.get(
												position).isComeMsg()));
								tv.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								});
							}
						}
					}
				}
			} else {
				if (chatList.get(position).isYuYin()) {
					holder.toIv.setVisibility(View.VISIBLE);
					holder.toLength.setVisibility(View.VISIBLE);
					holder.toSeconds.setVisibility(View.VISIBLE);
					holder.toTv.setVisibility(View.VISIBLE);
					holder.animViewTo.setVisibility(View.VISIBLE);
					holder.timeTo.setVisibility(View.VISIBLE);
					if (chatList.get(position).isSuccess()) {
						holder.timeTo.setText(chatList.get(position)
								.getChatTime());
					} else {
						holder.timeTo.setText("发送中...");
					}
					holder.animViewTo.setTag(3 + position);
					holder.toTv.setTag(3 + position);
					holder.fromIv.setVisibility(View.GONE);
					holder.fromLength.setVisibility(View.GONE);
					holder.fromSeconds.setVisibility(View.GONE);
					holder.head.setVisibility(View.GONE);
					holder.timeFrom.setVisibility(View.GONE);
					holder.lvRobot.setVisibility(View.GONE);
					holder.serverName.setVisibility(View.GONE);
					holder.pbSmall.setVisibility(View.GONE);
					if (MainActivity.isOnService) {
						if (avatarbitmap != null) {
							holder.toIv.setImageBitmap(avatarbitmap);
						}
					} else {
						if (avatarbitmap != null) {
							holder.toIv.setImageBitmap(avatarbitmap);
						}
					}
					holder.toTv.setText("");
					holder.toSeconds.setText((int) (Math.round(chatList.get(
							position).getTime()))
							+ "\"");
					ViewGroup.LayoutParams lp = holder.toTv.getLayoutParams();
					if (lp.width < max) {
						lp.width = (int) (mMinWidth + (mMaxWidth / 100f)
								* chatList.get(position).getTime());
					} else {
						lp.width = max;
					}
					TextView tv = (TextView) holder.toTv
							.findViewWithTag(3 + position);
					final View anim = holder.animViewTo
							.findViewWithTag(3 + position);
					tv.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (isPlay) {
								second = position + 3;
								if (second != frist) {
									MediaPlayerManager.pause();
									MediaPlayerManager.release();
									if (view != null) {
										view.setBackgroundResource(R.drawable.chat_adj);
									}
									// 声音播放动画
									anim.setBackgroundResource(R.drawable.chat_play_to_anim);
									AnimationDrawable animation = (AnimationDrawable) anim
											.getBackground();
									animation.start();
									isPlay = true;
									frist = position + 3;
									view = anim;
									// 播放录音
									MediaPlayerManager
											.playSound(
													chatList.get(position)
															.getFilePath(),
													new MediaPlayer.OnCompletionListener() {

														public void onCompletion(
																MediaPlayer mp) {
															// 播放完成后修改图片
															anim.setBackgroundResource(R.drawable.chat_adj);
														}
													});
								} else {
									MediaPlayerManager.pause();
									MediaPlayerManager.release();
									anim.setBackgroundResource(R.drawable.chat_adj);
									isPlay = false;
								}
							} else {
								// 声音播放动画
								anim.setBackgroundResource(R.drawable.chat_play_to_anim);
								AnimationDrawable animation = (AnimationDrawable) anim
										.getBackground();
								animation.start();
								isPlay = true;
								frist = position + 3;
								view = anim;
								// 播放录音
								MediaPlayerManager.playSound(
										chatList.get(position).getFilePath(),
										new MediaPlayer.OnCompletionListener() {

											public void onCompletion(
													MediaPlayer mp) {
												// 播放完成后修改图片
												anim.setBackgroundResource(R.drawable.chat_adj);
											}
										});

							}
						}
					});
					if (isMode) {
						tv.setOnLongClickListener(new YuYin1Dialog(convertView,
								position, chatList.get(position).isComeMsg()));
					} else {
						tv.setOnLongClickListener(new YuYinDialog(convertView,
								position, chatList.get(position).isComeMsg()));
					}
				} else {
					if (chatList.get(position).isPhoto()) {
						holder.toIv.setVisibility(View.VISIBLE);
						holder.toLength.setVisibility(View.VISIBLE);
						holder.toTv.setVisibility(View.VISIBLE);
						holder.timeTo.setVisibility(View.VISIBLE);
						if (chatList.get(position).isSuccess()) {
							holder.timeTo.setText(chatList.get(position)
									.getChatTime());
						} else {
							holder.timeTo.setText("发送中...");
						}
						holder.toTv.setTag(2 + "position");
						holder.fromIv.setVisibility(View.GONE);
						holder.fromLength.setVisibility(View.GONE);
						holder.fromSeconds.setVisibility(View.GONE);
						holder.toSeconds.setVisibility(View.GONE);
						holder.animViewTo.setVisibility(View.GONE);
						holder.timeFrom.setVisibility(View.GONE);
						holder.head.setVisibility(View.GONE);
						holder.lvRobot.setVisibility(View.GONE);
						holder.serverName.setVisibility(View.GONE);
						holder.pbSmall.setVisibility(View.GONE);
						if (MainActivity.isOnService) {
							if (avatarbitmap != null) {
								holder.toIv.setImageBitmap(avatarbitmap);
							}
						} else {
							if (avatarbitmap != null) {
								holder.toIv.setImageBitmap(avatarbitmap);
							}
						}
						holder.toTv.setText("");
						ViewGroup.LayoutParams lp = holder.toTv
								.getLayoutParams();
						lp.width = LayoutParams.WRAP_CONTENT;
						lp.height = LayoutParams.WRAP_CONTENT;
						holder.toTv.setMaxWidth((int) (max * 3.2 / 4));
						holder.toTv.setMaxHeight((int) (max * 3.2 / 4));
						holder.toTv.setText(chatList.get(position).getBitmap());
						TextView tv = (TextView) holder.toTv
								.findViewWithTag(2 + "position");
						tv.setOnLongClickListener(new ImageDialog(convertView,
								position, chatList.get(position).isComeMsg()));
						tv.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								if (chatList.get(position).isVideo()) {
									if (isPlay) {
										MediaPlayerManager.pause();
										MediaPlayerManager.release();
										if (view != null) {
											view.setBackgroundResource(R.drawable.chat_adj);
										}
										Intent intent = new Intent(context,
												PlayVideoActivity.class);
										intent.putExtra("a",
												chatList.get(position)
														.getVideoPath());
										context.startActivity(intent);
										isPlay = false;
									} else {
										Intent intent = new Intent(context,
												PlayVideoActivity.class);
										intent.putExtra("a",
												chatList.get(position)
														.getVideoPath());
										context.startActivity(intent);
									}
								} else {
									if (isPlay) {
										MediaPlayerManager.pause();
										MediaPlayerManager.release();
										if (view != null) {
											view.setBackgroundResource(R.drawable.chat_adj);
										}
										Intent intent = new Intent(context,
												ImageActivity.class);
										intent.putExtra("a",
												chatList.get(position)
														.getPhotoPath());
										context.startActivity(intent);
										isPlay = false;
									} else {
										Intent intent = new Intent(context,
												ImageActivity.class);
										intent.putExtra("a",
												chatList.get(position)
														.getPhotoPath());
										context.startActivity(intent);
									}
								}
							}
						});
					} else {
						holder.toIv.setVisibility(View.VISIBLE);
						holder.toLength.setVisibility(View.VISIBLE);
						holder.toTv.setVisibility(View.VISIBLE);
						holder.timeTo.setVisibility(View.VISIBLE);
						if (chatList.get(position).isSuccess()) {
							holder.timeTo.setText(chatList.get(position)
									.getChatTime());
						} else {
							holder.timeTo.setText("发送中...");
						}
						holder.toTv.setTag(1 + position);
						holder.fromIv.setVisibility(View.GONE);
						holder.fromLength.setVisibility(View.GONE);
						holder.fromSeconds.setVisibility(View.GONE);
						holder.toSeconds.setVisibility(View.GONE);
						holder.animViewTo.setVisibility(View.GONE);
						holder.timeFrom.setVisibility(View.GONE);
						holder.head.setVisibility(View.GONE);
						holder.lvRobot.setVisibility(View.GONE);
						holder.serverName.setVisibility(View.GONE);
						holder.pbSmall.setVisibility(View.GONE);
						if (MainActivity.isOnService) {
							if (avatarbitmap != null) {
								holder.toIv.setImageBitmap(avatarbitmap);
							}
						} else {
							if (avatarbitmap != null) {
								holder.toIv.setImageBitmap(avatarbitmap);
							}
						}
						holder.toTv.setText("");
						ViewGroup.LayoutParams lp = holder.toTv
								.getLayoutParams();
						lp.width = LayoutParams.WRAP_CONTENT;
						// 对内容做处理
						SpannableStringBuilder sb = handler(holder.toTv,
								chatList.get(position).getContent(), position);
						holder.toTv.setMaxWidth(max);
						holder.toTv.setText(sb);
						TextView tv = (TextView) holder.toTv
								.findViewWithTag(1 + position);
						tv.setOnLongClickListener(new WenZiDialog(convertView,
								position, chatList.get(position).isComeMsg()));
						tv.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

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

	/**
	 * 对消息内容做处理(里面含有表情)
	 * 
	 * @return SpannableStringBuilder对象
	 */
	private SpannableStringBuilder handler(final TextView gifTextView,
			String content, int position) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "\\[[\u4E00-\u9FA5]{1,2}\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			String realText = null;
			for (int i = 0; i < emojiList.size(); i++) {
				if (emojiList.get(i).getText().equals(tempText)) {
					realText = emojiList.get(i).getFile();
					i = emojiList.size();
				}
			}
			try {
				String path = Environment.getExternalStorageDirectory() + "/"
						+ "PngImage" + "/" + realText + ".png";
				Bitmap bitmap = getByCache(path);
				if (bitmap != null) {
					sb.setSpan(new ImageSpan(context, bitmap), m.start(),
							m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else {
					Bitmap b = ImageUtil.zoomImg(
							BitmapFactory.decodeFile(path),
							DisplayUtil.sp2px(context, 27f),
							DisplayUtil.sp2px(context, 27f));
					savaToCache(path, b);
					Bitmap bitmap1 = getByCache(path);
					sb.setSpan(new ImageSpan(context, bitmap1), m.start(),
							m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} catch (Exception e) {
				try {
					String path = Environment.getExternalStorageDirectory()
							+ "/" + "PngImage" + "/" + realText + ".png";
					Bitmap bitmap = getByCache(path);
					if (bitmap != null) {
						sb.setSpan(new ImageSpan(context, bitmap), m.start(),
								m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else {
						Bitmap b = ImageUtil.zoomImg(
								BitmapFactory.decodeFile(path),
								DisplayUtil.sp2px(context, 27f),
								DisplayUtil.sp2px(context, 27f));
						savaToCache(path, b);
						Bitmap bitmap1 = getByCache(path);
						sb.setSpan(new ImageSpan(context, bitmap1), m.start(),
								m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}

	private class ChatHolder {
		private TextView timeTo;
		private TextView timeFrom;
		private TextView toTv;
		private TextView fromTv;
		private TextView fromSeconds;
		private TextView toSeconds;
		private View fromLength;
		private View toLength;
		private ImageView fromIv;
		private ImageView toIv;
		private View animViewFrom;
		private View animViewTo;
		private TextView head;
		private ListView lvRobot;
		private TextView serverName;
		private ProgressBar pbSmall;
	}

	/**
	 * 屏蔽listitem的所有事件
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	/**
	 * 长按图片消息的dialog
	 * 
	 * @author Wangda
	 * 
	 */
	public class ImageDialog implements OnLongClickListener {
		int position;
		View view;
		boolean isCome;

		public ImageDialog(View view, int position, boolean isCome) {
			this.position = position;
			this.view = view;
			this.isCome = isCome;
		}

		@Override
		public boolean onLongClick(View v) {
			if (!chatList.get(position).isVideo()) {
				AlertDialog dialog;
				final String[] str = new String[] { "保存到本地", "取消" };
				AlertDialog.Builder builder = new AlertDialog.Builder(context,
						R.style.ChatAlertDialog);
				builder.setItems(str, new OnClickListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String name = ImageUtil.saveImageToGallery(
									context,
									BitmapFactory.decodeFile(chatList.get(
											position).getPhotoPath()));
							ToastControll.showToast("图片已保存至" + name, context);
							break;
						case 1:
							break;
						}
					}
				});
				dialog = builder.create();
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
			return true;
		}

	}

	/**
	 * 长按语音消息的dialog
	 * 
	 * @author Wangda
	 */
	public class YuYinDialog implements OnLongClickListener {
		int position;
		View view;
		boolean isCome;

		public YuYinDialog(View view, int position, boolean isCome) {
			this.position = position;
			this.view = view;
			this.isCome = isCome;
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog dialog;
			final String[] str = new String[] { "使用听筒模式播放", "取消" };
			AlertDialog.Builder builder = new AlertDialog.Builder(context,
					R.style.ChatAlertDialog);
			builder.setItems(str, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						isMode = true;
						Toast.makeText(context, "已切换为听筒模式播放",
								Toast.LENGTH_SHORT).show();
						setInCallBySdk();
						notifyDataSetChanged();
						break;
					case 1:
						break;
					}
				}
			});
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			return true;
		}

	}

	/**
	 * 长按语音消息的dialog
	 * 
	 * @author Wangda
	 */
	public class YuYin1Dialog implements OnLongClickListener {
		int position;
		View view;
		boolean isCome;

		public YuYin1Dialog(View view, int position, boolean isCome) {
			this.position = position;
			this.view = view;
			this.isCome = isCome;
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog dialog;
			final String[] str = new String[] { "使用扬声器模式播放", "取消" };
			AlertDialog.Builder builder = new AlertDialog.Builder(context,
					R.style.ChatAlertDialog);
			builder.setItems(str, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						isMode = false;
						Toast.makeText(context, "已切换为扬声器模式播放",
								Toast.LENGTH_SHORT).show();
						setModeNormal();
						notifyDataSetChanged();
						break;
					case 1:
						break;
					}
				}
			});
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			return true;
		}

	}

	/**
	 * 长按文本消息的dialog
	 * 
	 * @author Wangda
	 * 
	 */
	public class WenZiDialog implements OnLongClickListener {
		int position;
		View view;
		boolean isCome;

		public WenZiDialog(View view, int position, boolean isCome) {
			this.position = position;
			this.view = view;
			this.isCome = isCome;
		}

		@Override
		public boolean onLongClick(View v) {
			AlertDialog dialog;
			final String[] str = new String[] { "复制", "取消" };
			AlertDialog.Builder builder = new AlertDialog.Builder(context,
					R.style.ChatAlertDialog);
			builder.setItems(str, new OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						String content = chatList.get(position).getContent();
						ClipboardManager cm = (ClipboardManager) context
								.getSystemService(Context.CLIPBOARD_SERVICE);
						// 将文本数据复制到剪贴板
						cm.setText(content);
						break;
					case 1:
						break;
					}
				}
			});
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			return true;
		}

	}

	/**
	 * 设置语音模式为听筒
	 */
	private void setInCallBySdk() {
		if (mAudioManager == null) {
			return;
		}
		if (Build.VERSION.SDK_INT >= 21) {
			if (mAudioManager.getMode() != AudioManager.MODE_IN_COMMUNICATION) {
				mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
			}
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("android.media.AudioSystem");
				@SuppressWarnings("unchecked")
				Method m = clazz.getMethod("setForceUse", new Class[] {
						int.class, int.class });
				m.invoke(null, 1, 1);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			if (mAudioManager.getMode() != AudioManager.MODE_IN_CALL) {
				mAudioManager.setMode(AudioManager.MODE_IN_CALL);
			}
		}
		if (mAudioManager.isSpeakerphoneOn()) {
			mAudioManager.setSpeakerphoneOn(false);
			mAudioManager
					.setStreamVolume(
							AudioManager.STREAM_VOICE_CALL,
							mAudioManager
									.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
							AudioManager.STREAM_VOICE_CALL);
		}
	}

	/**
	 * 设置语音模式为扬声器
	 */
	private void setModeNormal() {
		if (mAudioManager == null) {
			return;
		}
		mAudioManager.setSpeakerphoneOn(true);
		mAudioManager.setMode(AudioManager.MODE_NORMAL);

		if (!mAudioManager.isSpeakerphoneOn()) {
			mAudioManager.setSpeakerphoneOn(true);

			mAudioManager
					.setStreamVolume(
							AudioManager.STREAM_VOICE_CALL,
							mAudioManager
									.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
							AudioManager.STREAM_VOICE_CALL);
		}

	}

	/**
	 * 从弱引用中取出表情图片
	 * 
	 * @param path
	 *            表情图片路径
	 * @return 返回表情图片
	 */
	public Bitmap getByCache(String path) {
		SoftReference<Bitmap> ref = cache.get(path);
		if (ref == null) {
			return null;
		}
		Bitmap bitmap = ref.get();
		return bitmap;
	}

	/**
	 * 弱引用储存表情图片
	 * 
	 * @param path
	 *            表情图片路径
	 * @param bitmap
	 *            表情图片
	 */
	public void savaToCache(String path, Bitmap bitmap) {
		SoftReference<Bitmap> ref = new SoftReference<Bitmap>(bitmap);
		cache.put(path, ref);
	}

	public void setServerAvatar(Bitmap bitmap) {
		if (bitmap != null) {
			this.serverbitmap = bitmap;
		}
	}

	/**
	 * 设置用户头像
	 * 
	 * @param avatar
	 *            用户头像
	 */
	public void setAvatar(Bitmap avatar) {
		if (avatar != null) {
			this.avatarbitmap = avatar;
		}
	}

	/**
	 * 设置表情list
	 * 
	 * @param emojiList
	 *            表情集合
	 */
	public void setEmojiList(List<Emoji> emojiList) {
		this.emojiList = emojiList;
	}

	private boolean in = false;
	private boolean isFirst = true;

	/**
	 * 设置显示机器人的listview的高度和宽度(由于是listview嵌套listview，所以需要设置，否则内部的listview显示不全)
	 * 
	 * @param listView
	 *            需要设置的listview
	 */
	public void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int maxWidth = mMaxWidth;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
	                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			listItem.setLayoutParams(params);
			listItem.measure(0, 0);
			// listItem.getMeasuredHeight();
			int width = listItem.getMeasuredWidth();
			if (width > mMaxWidth) {
				maxWidth = mMaxWidth;
				in = true;
			} else {
				if (in) {
					maxWidth = mMaxWidth;
				} else {
					if (isFirst) {
						maxWidth = width;
						in = false;
						isFirst = false;
					} else {
						if (width > maxWidth) {
							maxWidth = width;
						}
					}
				}
			}

		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.width = maxWidth;
		Log.i("all_height", params.height + "");
		listView.setLayoutParams(params);
		in = false;
		isFirst = true;
	}

}
