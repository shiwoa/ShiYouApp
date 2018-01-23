package com.jiaoyu.read.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.jiaoyu.read.Main2Activity;
import com.tider.android.common.ZipUtil;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.SAXParserFactory;

public class BookDownLoadTask extends AsyncTask<String, Integer, Boolean> {
    public final static int MSG_PROGRESS = 100005;
    public final static int MSG_DOWN_OVER = 100006;
    public final static int MSG_DOWN_HIDEBTN = 100007;
    public final static int MSG_BOOK_PAUSE = 100008;
    public final static int MSG_TRY_TO_DELETE = 100009;
    public final static int MSG_DOWNLOAD_FREE_BOOK = 100010;
    private static final String TAG = BookDownLoadTask.class.getSimpleName();
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    public static final Executor SERIAL_EXECUTOR = Executors.newSingleThreadExecutor(sThreadFactory);
    private Main2Activity mActivity;

    public BookDownLoadTask(Activity activity) {
        mActivity = (Main2Activity) activity;
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        FileOutputStream fout = null;
        HttpURLConnection conn = null;
        RandomAccessFile randomAccessFile = null;
        InputStream in = null;

        try {

            // 2检查文件目录是是否存在
            if (!new File(Main2Activity.localBookPath).exists()) {
                new File(Main2Activity.localBookPath).mkdirs();
            }

//            String dataFile = Main2Activity.localBookPath + "book.zip";
            String dataFile = Main2Activity.localBookPath + "book.epub";
            File newFile = new File(dataFile);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }else{
                publishProgress(MSG_DOWN_OVER);
            }
            long fileRealSize = newFile.length();
            // TODO：从测试url改为正常url
            URL u = new URL(Main2Activity.serverBookPath);
            // URL u = new
            // URL("http://192.168.1.30:8070/publish//1/fc2aa6dfe9234297b7646afbc705f204/49ef033fb7af42328ec1e8680d60ea6f/book.zip");
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.1.4322)");
            String sProperty = "bytes=" + fileRealSize + "-";
            conn.setRequestProperty("RANGE", sProperty);
            randomAccessFile = new RandomAccessFile(newFile, "rwd");
            randomAccessFile.seek(fileRealSize);
            // 文件大小
            int contentLength = conn.getContentLength();

            double rate = ((double) (newFile.length()) / (fileRealSize + contentLength));

            Main2Activity.mCurrentRate = rate;
            publishProgress(MSG_PROGRESS);

            int responseCode = conn.getResponseCode();
            // 如果网络地址上存在这个文件，直接下载，如果不存在，返回false，下载失败
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK || conn
                    .getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                in = conn.getInputStream();
                byte[] perSize = new byte[512];
                int read = 0;
                int length = 0;
                double lastRate = 0;
                while ((read = in.read(perSize, 0, 512)) != -1) {
                    if(isCancelled()){
                        return null;
                    }
                    length = read;
                    randomAccessFile.write(perSize, 0, length);
                    rate = ((double) (newFile.length()) / (fileRealSize + contentLength));
                    if (rate > (lastRate + 0.0001)) {
                        Main2Activity.mCurrentRate = rate;
                        publishProgress(MSG_PROGRESS);
                        lastRate = rate;
                    }
                }

                // 下载尚未完成，直接返回就不需要解压了
                if (newFile.length() < (fileRealSize + contentLength)) {
                    /*
					 * mBook.state=Book.DOWNLOAD_NEED_RESTART; MyLog.d(TAG,
					 * "set book state=need restart");
					 */
                    return false;
                }

                Main2Activity.mCurrentRate = 1.00;
                publishProgress(MSG_PROGRESS);
                publishProgress(MSG_DOWN_HIDEBTN);
                // 解压zip
                ZipUtil.UnZipFolder(dataFile, Main2Activity.localBookPath);
				/*
				// 解析一下,看看是否有
				String bookxmlPath = StringUtils.contactForFile(mBook.mData,
						"book", "book.xml");
				File bookXMLFile = new File(bookxmlPath);
				FileInputStream fis = new FileInputStream(bookXMLFile);
				decodeModel(fis, new BookTypeXMLHandler(mBook));
				*/

            } else {

                return false;
            }

            // 删除压缩包
            dataFile = Main2Activity.localBookPath + "book.zip";
            File zipFile = new File(dataFile);
            if (zipFile.exists()) {
                zipFile.delete();
            }

            publishProgress(MSG_DOWN_OVER);

            return true;
        } catch (Exception e) {
            Log.e("mouee", "下载失敗。", e);
            return false;
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(isCancelled()){
            return;
        }
        Integer v = values[0];
        mActivity.msgHandler.sendEmptyMessage(v);
    }

    public void decodeModel(InputStream xmlStream, ContentHandler handler) {
        if (null == xmlStream) {
            return;
        }
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader reader = factory.newSAXParser().getXMLReader();
            reader.setContentHandler(handler);// set content hander
            reader.parse(new InputSource(xmlStream));
        } catch (Exception ex) {
        } finally {
            try {
                xmlStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(11)
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }
}
