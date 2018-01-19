/*
 * Copyright (C) 2007-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.os.Handler;

import org.geometerplus.fbreader.fbreader.FBReaderApp;

public class SelectionShareAction extends FBAndroidAction {
    private String selectedText;
    private String bookTitle;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//			switch(msg.what){
//			case ShareConstants.COMPLETE:
//				Toast.makeText(BaseActivity, R.string.share_completed, Toast.LENGTH_SHORT).show();
//				break;
//			case ShareConstants.ERROR:
//				Toast.makeText(BaseActivity, R.string.share_failed, Toast.LENGTH_SHORT).show();
//				break;
//			case ShareConstants.CANCEL:
//				Toast.makeText(BaseActivity, R.string.share_canceled, Toast.LENGTH_SHORT).show();
//				break;
//			}
        }

        ;
    };

    SelectionShareAction(FBReader baseActivity, FBReaderApp fbreader) {
        super(baseActivity, fbreader);
    }

    @Override
    protected void run(Object... params) {
        selectedText = Reader.getTextView().getSelectedText();
        bookTitle = Reader.Model.Book.getTitle();
        Reader.getTextView().clearSelection();
        /*
		String url = "http://v.t.sina.com.cn/share/share.php?title=" +text;
		Uri uri = Uri.parse(url); //
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		BaseActivity.startActivity(Intent.createChooser(intent, null));
		*/
		/*
		Intent intent = new Intent(BaseActivity,ShareMenuActivity.class);
		intent.putExtra(ShareMenuActivity.EXTRA_SELECTED_TEXT, text);
		intent.putExtra(ShareMenuActivity.EXTRA_BOOK_TITLE, title);
		BaseActivity.startActivity(intent);
		BaseActivity.overridePendingTransition(R.anim.slide_in_up, 0);
		*/
//		showShare(true, SinaWeibo.NAME);

    }

    private void showShare(final boolean silent, final String platform) {
//
//		 final FBReaderApp fbReader = (FBReaderApp) FBReaderApp
//					.Instance();
//		final Dialog shareDialog = new Dialog(BaseActivity,
//				R.style.Translucent_NoTitle);
//		View view_dialog = LayoutInflater.from(BaseActivity).inflate(
//				R.layout.dialog_share_layout, null);
//		shareDialog.setContentView(view_dialog);
//		shareDialog.setCanceledOnTouchOutside(true);
//		Window window = shareDialog.getWindow();
//		android.view.WindowManager.LayoutParams params = window.getAttributes();
//		params.gravity = Gravity.BOTTOM;
////		 params.width = LayoutParams.MATCH_PARENT;
////		 params.height = 426;
//		window.setAttributes(params);
//		shareDialog.show();
//		Button cancelBtn = (Button) view_dialog.findViewById(R.id.button1);
//		ImageView img_sina = (ImageView) view_dialog.findViewById(R.id.img_sina);
//		ImageView img_wechat = (ImageView) view_dialog.findViewById(R.id.img_wechat);
//		ImageView img_wechatcomment = (ImageView) view_dialog
//				.findViewById(R.id.img_wechatcomment);
//		img_sina.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
////				shareDialog.dismiss();
////				OnekeyShare oks = new OnekeyShare();
////				oks.setNotification(R.drawable.icon, BaseActivity.getResources().getString(R.string.app_name_share));
////				if (!StringUtils.isEmpty(selectedText) && selectedText.length() > 70) {
////					selectedText = selectedText.substring(0, 70)+"...";
////				}
////				oks.setText(String.format(BaseActivity.getResources().getString(R.string.selected_share_content),selectedText,bookTitle));
////				oks.setImageUrl(FBReader.coverPath);
////				oks.setSilent(silent);
////				if (platform != null) {
////					oks.setPlatform(platform);
////				}
////				
////				// 去除注释，则快捷分享的分享加过将听过OneKeyShareCallback回调
////				oks.setCallback(new PlatformActionListener(){
////
////					@Override
////					public void onCancel(Platform arg0, int arg1) {
////						handler.obtainMessage(ShareConstants.CANCEL).sendToTarget();
////					}
////
////					@Override
////					public void onComplete(Platform arg0, int arg1,
////							HashMap<String, Object> arg2) {
////						handler.obtainMessage(ShareConstants.COMPLETE).sendToTarget();
////					}
////
////					@Override
////					public void onError(Platform arg0, int arg1, Throwable arg2) {
////						handler.obtainMessage(ShareConstants.ERROR).sendToTarget();
////					}
////					
////				});
////				/*
////				oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback(){
////
////					@Override
////					public void onShare(Platform platform, ShareParams paramsToShare) {
////						// 改写新浪微博分享内容中的text字段，其他平台不改写
////						if (SinaWeibo.NAME.equals(platform.getName())) {
////							SinaWeibo.ShareParams sp = (SinaWeibo.ShareParams) paramsToShare;
////							sp.text += " -- from Share SDK";
////						}
////						
////					}
////					
////				});
////				*/
////				oks.show(BaseActivity);
//			}
//		});
//		img_wechat.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//				Wechat.ShareParams sp = new Wechat.ShareParams();
//				sp.shareType = Platform.SHARE_WEBPAGE;
//				sp.imagePath = FBReader.coverPath;
//				sp.url = "http://v.dajianet.com/client.html?validated=true";
//				sp.text = "";
//				sp.title = "#好书推荐#  《" + fbReader.Model.Book.getTitle() + "》 ";
//				Platform platform = ShareSDK.getPlatform(
//						BaseActivity, Wechat.NAME);
//				if (platform != null && platform.isValid()) {
//					platform.share(sp);
//				}
//
//			}
//		});
//		img_wechatcomment.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//
//				 WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
//					sp.shareType = Platform.SHARE_WEBPAGE;
//					sp.imagePath = FBReader.coverPath;
//					sp.url = "http://v.dajianet.com/client.html?validated=true";
//					sp.text = "";
//					sp.title = "#好书推荐#  《" + fbReader.Model.Book.getTitle() + "》 ";
//					Platform platform = ShareSDK.getPlatform(BaseActivity, WechatMoments.NAME);
//					if (platform.isValid()) {
//						platform.share(sp);
//					}
//			 
//			}
//		});
//		cancelBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				shareDialog.dismiss();
//			}
//		});
//	
    }
}
