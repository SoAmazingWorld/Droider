package com.awave.apps.droider.Feed.Presentor;


import com.awave.apps.droider.DroiderBaseActivity;
import com.awave.apps.droider.Feed.Interactors.FeedParser;
import com.awave.apps.droider.Feed.Interactors.OnLoadingInteractorFinishedListener;
import com.awave.apps.droider.Feed.OnTaskCompleted;
import com.awave.apps.droider.Feed.View.FeedFragment;
import com.awave.apps.droider.Feed.View.FeedView;
import com.awave.apps.droider.Model.FeedModel;

import java.util.ArrayList;

public class FeedPresenterImpl implements FeedPresenter, OnLoadingInteractorFinishedListener {

    FeedView view;
    FeedFragment feedFragment;
    OnTaskCompleted taskCompleted;
    boolean isPodCast;
    ArrayList<FeedModel> list = new ArrayList<>();

    public void attachView(FeedFragment fragment, FeedView view, OnTaskCompleted onTaskCompleted) {
        this.view = view;
        this.taskCompleted = onTaskCompleted;
        this.feedFragment = fragment;
    }

    @Override
    public void loadMore(String url) {
        new FeedParser(list, this, isPodCast).execute(url);
    }

    @Override
    public void getDataWithClearing(String url) {
        list.clear();
        new FeedParser(list, this, isPodCast).execute(url + 1);
    }

    @Override
    public void OnCompleted(ArrayList<FeedModel> list) {
        view.onLoadComplete(list);
    }

    @Override
    public void onNetworkFailure() {
        ((DroiderBaseActivity) feedFragment.getActivity()).initInternetConnectionDialog(feedFragment.getActivity());
    }
}
