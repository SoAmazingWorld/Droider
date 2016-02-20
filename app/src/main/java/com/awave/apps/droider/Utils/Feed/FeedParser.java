package com.awave.apps.droider.Utils.Feed;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.awave.apps.droider.Utils.Helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class FeedParser extends AsyncTask<String, Void, Void> {
    private static final String TAG = "FeedParser";

    private int count = 0;

    private List<FeedItem> mFeedItems = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private List<String> mUrlList = new ArrayList<>();
    private List<String> mDescrList = new ArrayList<>();
    private List<String> mImgUrlList = new ArrayList<>();
    private List<String> mYoutubeUrlList = new ArrayList<>();

    private Handler mRefreshHandler = new Handler();
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OnTaskCompleted mOnTaskCompleted;

    public FeedParser(ArrayList<FeedItem> data, SwipeRefreshLayout refreshLayout, Context context, OnTaskCompleted onTaskCompleted){
        this.mFeedItems = data;
        this.mSwipeRefreshLayout = refreshLayout;
        this.mContext = context;
        this.mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected Void doInBackground(String... params) {
        final Document[] document = new Document[1];
        final String [] parameters = params;
        synchronized (this) {
            try {
                try {
                    document[0] = Jsoup.connect(params[0]).get();
                }
                catch (SocketTimeoutException e) {
                    Log.e(TAG, "doInBackground: failed to connect/SocketTimeoutException. Running new Thread", e.getCause());
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                document[0] = Jsoup.connect(parameters[0]).get();
                            } catch (IOException e) {
                                Log.e(TAG, "run: failed to connect to the given url", e.getCause());
                            }
                        }
                    }.run();

                }
                Elements elements = document[0].select("div[id^=post]");
                Elements entry = document[0].select("div.entry");

                for (Element element : elements) {
                    mTitleList.add(element.getElementsByTag("a").attr("title"));
                    mUrlList.add(element.getElementsByTag("a").attr("href"));
                    mDescrList.add(element.getElementsByTag("p").text().substring(0, element.getElementsByTag("p").text().lastIndexOf(" ")));
                    mImgUrlList.add(element.getElementsByTag("img").attr("src"));

                    count++;
                }

                for (Element youtube : entry) {
                    mYoutubeUrlList.add(youtube.getElementsByTag("iframe").attr("src"));
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Failed to load the feed", e.getCause());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        for (int i = 0; i < count; i++) {
            FeedItem feedItem = new FeedItem();

            feedItem.setTitle(mTitleList.get(i));
            feedItem.setDescription(mDescrList.get(i));
            feedItem.setUrl(mUrlList.get(i));

            if (mYoutubeUrlList.get(i).contains("youtube")){
                Log.d(TAG, "onPostExecute: set image from youtube");
                feedItem.setImgUrl(Helper.getYoutubeImg(mYoutubeUrlList.get(i)));
            }
            else if (mImgUrlList.get(i).contains("\u0060")){
                feedItem.setImgUrl(mImgUrlList.get(i).replace("\u0060", "%60"));
            }
            else {
                feedItem.setImgUrl(mImgUrlList.get(i));
            }

            mFeedItems.add(feedItem);
        }
        mRefreshHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mOnTaskCompleted.onTaskCompleted();
    }
}