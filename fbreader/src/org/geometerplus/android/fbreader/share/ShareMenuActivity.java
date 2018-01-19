package org.geometerplus.android.fbreader.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.geometerplus.android.fbreader.FBReader;
import org.geometerplus.zlibrary.ui.android.R;

public class ShareMenuActivity extends Activity {
    public static final String EXTRA_SELECTED_TEXT = "selectedText";
    public static final String EXTRA_BOOK_TITLE = "bookTitle";

    private String selectedText;
    private String bookTitle;

//	private Platform[] weiboList;

    /**
     * 将action转换为String
     */
    public static String actionToString(int action) {
        switch (action) {
//			case Platform.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";
//			case Platform.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";
//			case Platform.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";
//			case Platform.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";
//			case Platform.ACTION_TIMELINE: return "ACTION_TIMELINE";
//			case Platform.ACTION_USER_INFOR: return "ACTION_USER_INFOR";
//			case Platform.ACTION_SHARE: return "ACTION_SHARE";
            default: {
                return "UNKNOWN";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        selectedText = intent.getStringExtra(EXTRA_SELECTED_TEXT);
        bookTitle = intent.getStringExtra(EXTRA_BOOK_TITLE);

        setContentView(R.layout.share_menu);
        ListView lv = (ListView) findViewById(R.id.lv_share_menu);
//		weiboList = ShareSDK.getPlatformList(this);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//				String name = weiboList[position].getName();
//
//				/*
//				SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//				getResources().getDrawable(R.drawable.ic_list_buy).
//				*/
//				Intent shareIntent = getShareIntent(false, name);
//				new EditPage().show(ShareMenuActivity.this,shareIntent);
//
                close();
            }

        });
//
//		ShareMenuAdapter adapter = new ShareMenuAdapter(weiboList);
//		lv.setAdapter(adapter);
    }

    //	private Bitmap getPlatLogo(Platform weibo) {
//		if (weibo == null) {
//			return null;
//		}
//
//		String name = weibo.getName();
//		if (name == null) {
//			return null;
//		}
//
//		String resName = "logo_" + weibo.getName();
//		int resId = cn.sharesdk.framework.utils.R.getResId(R.drawable.class, resName);
//		return BitmapFactory.decodeResource(getResources(), resId);
//	}
    private Intent getShareIntent(boolean silent, String platform) {
        Intent i = new Intent();
        // 分享时Notification的图标
        i.putExtra("notif_icon", R.drawable.icon);
        // 分享时Notification的标题
        i.putExtra("notif_title", getResources().getString(R.string.zwtd));

        // title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供
        i.putExtra("title", getResources().getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供
        i.putExtra("titleUrl", "http://www.jxepub.com");
        // text是分享文本，所有平台都需要这个字段
        if (selectedText == null) {
            i.putExtra("text", getResources().getString(R.string.share_content));
        } else {
//			String text = String.format(getResources().getString(R.string.selected_share_content), bookTitle,selectedText);
//			i.putExtra("text", text);
        }
        // imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段
        i.putExtra("imagePath", FBReader.TEST_IMAGE);
        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
//		i.putExtra("imageUrl", "http://img.appgo.cn/imgs/sharesdk/content/2013/06/13/1371120300254.jpg");
        // url仅在微信（包括好友和朋友圈）中使用，否则可以不提供
        i.putExtra("url", "http://www.jxepub.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        i.putExtra("comment", getResources().getString(R.string.share));
        // site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
        i.putExtra("site", getResources().getString(R.string.zwtd));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
        i.putExtra("siteUrl", "http://www.jxepub.com");

        if (platform != null) {
            // platform是平台名称
            i.putExtra("platform", platform);
        }
        // 是否直接分享
        i.putExtra("silent", silent);

        return i;
    }

    public void close(View view) {
        close();
    }

    private void close() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    //	class ShareMenuAdapter extends BaseAdapter{
//		private Platform[] weiboList;
//
//
//		public ShareMenuAdapter(Platform[] weiboList) {
//			super();
//			this.weiboList = weiboList;
//		}
//
//		@Override
//		public int getCount() {
//			return weiboList.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return weiboList[position].getName();
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if(convertView==null){
//				convertView = View.inflate(ShareMenuActivity.this, R.layout.share_menu_item, null);
//				holder = new ViewHolder(convertView);
//				convertView.setTag(holder);
//			}else{
//				holder=(ViewHolder) convertView.getTag();
//			}
//
//			holder.iv.setImageBitmap(getPlatLogo(weiboList[position]));
//			String name = weiboList[position].getName();
//			String actualName=null;
//			if(SinaWeibo.NAME.equals(name)){
//				actualName=getResources().getString(R.string.sinaweibo);
//			}
//			holder.tv.setText(actualName);
//
//			return convertView;
//		}
//
//	}
    static class ViewHolder {
        private ImageView iv;
        private TextView tv;

        public ViewHolder(View baseView) {
            super();
            iv = (ImageView) baseView.findViewById(R.id.iv_share_menu_item);
            tv = (TextView) baseView.findViewById(R.id.tv_share_menu_item);
        }
    }
}
