package thang.com.wref.Main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import thang.com.wref.Adapter.NewsAdapter;
import thang.com.wref.Adapter.StoriesAdapter;
import thang.com.wref.Models.NewsModels;
import thang.com.wref.Models.StoriesModels;
import thang.com.wref.R;

public class SocialNetworkFragment extends Fragment {
    private View view;

    private RecyclerView rcvStories, rcvNews;
    private ArrayList<NewsModels> newsArr;
    private ArrayList<StoriesModels> storiesArr;
    private NewsAdapter newsAdapter;
    private StoriesAdapter storiesAdapter;

    public SocialNetworkFragment() {
        // Required empty public constructor
    }


//    public static SocialNetworkFragment newInstance(String param1, String param2) {
//        SocialNetworkFragment fragment = new SocialNetworkFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_social_network, container, false);
        mapingView();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storiesArr = new ArrayList<>();
        newsArr = new ArrayList<>();
        addStoriesAdapter();
        addNewsAdapter();
    }

    private void mapingView(){
        rcvStories = (RecyclerView) view.findViewById(R.id.rcvStories);
        rcvNews = (RecyclerView) view.findViewById(R.id.rcvNews);
    }
    private void setupRecyclerView(){
        rcvStories.setHasFixedSize(true);
        rcvNews.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManagerS = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
                );
        LinearLayoutManager linearLayoutManagerN = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false
        );

        rcvNews.setLayoutManager(linearLayoutManagerN);
        rcvStories.setLayoutManager(linearLayoutManagerS);
        rcvStories.setNestedScrollingEnabled(false);
        rcvNews.setNestedScrollingEnabled(false);
    }
    private void addStoriesAdapter(){
        storiesArr.clear();
//        storiesAdapter.notifyDataSetChanged();
        storiesAdapter = new StoriesAdapter(storiesArr, getContext().getApplicationContext());
        rcvStories.setAdapter(storiesAdapter);
    }
    private void addNewsAdapter(){
        newsArr.clear();
//        newsAdapter.notifyDataSetChanged();
        newsAdapter = new NewsAdapter(newsArr, getContext().getApplicationContext());
        rcvNews.setAdapter(newsAdapter);
    }
}