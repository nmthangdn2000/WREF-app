package thang.com.wref.Main;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Adapter.NewsAdapter;
import thang.com.wref.Adapter.StoriesAdapter;
import thang.com.wref.Main.Stories.CubeTransformerViewpager;
import thang.com.wref.Main.Stories.StoriesFragment;
import thang.com.wref.Main.Stories.StoriesViewpaerAdapter;
import thang.com.wref.Models.NewsModels;
import thang.com.wref.Models.StoriesModels;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.NewsRetrofit;
import thang.com.wref.fragment.CommentFragment;

public class SocialNetworkFragment extends Fragment implements View.OnClickListener, View.OnTouchListener{
    private static final String TAG = "SocialNetworkFragment";
    private View view;
    private MeowBottomNavigation meowBottomNavigation;

    private RecyclerView rcvStories, rcvNews;
    private ArrayList<NewsModels> newsArr;
    private ArrayList<StoriesModels> storiesArr;
    private NewsAdapter newsAdapter;
    private StoriesAdapter storiesAdapter;
    private LinearLayout btnPostNewNews;
    private SearchView searchView;

    private ViewPager2 viewPager2story;
    private StoriesViewpaerAdapter storiesViewpaerAdapter;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private NewsAdapter.onClickRecyclerNews mListenerNews;
    private FrameLayout fragmentCommnet;
    private boolean check = false;

    private Retrofit retrofit;
    private NetworkUtil networkUtil;
    private NewsRetrofit newsRetrofit;

    public SocialNetworkFragment(MeowBottomNavigation meowBottomNavigation) {
        this.meowBottomNavigation = meowBottomNavigation;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getContext().getResources().getColor(R.color.colorStatusBar_Weather));
        }
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
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
        setOnClickRecyclerNews();
        addStoriesAdapter();
        addNewsAdapter();
        clospaneSlidingUpPanel();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgStories:
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
                meowBottomNavigation.startAnimation(animation);
                meowBottomNavigation.setVisibility(View.GONE);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                setUpStories();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.SlidingUpPanelLayout:

                break;
            case R.id.btnPostNewNews:
                clickPostNews();
                break;
            default:
                break;
        }
        return false;
    }
    private void mapingView(){
        rcvStories = (RecyclerView) view.findViewById(R.id.rcvStories);
        rcvNews = (RecyclerView) view.findViewById(R.id.rcvNews);
        viewPager2story = (ViewPager2) view.findViewById(R.id.storiesViewpager);
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.SlidingUpPanelLayout);
        fragmentCommnet = (FrameLayout) view.findViewById(R.id.fragmentCommnet);
        btnPostNewNews = (LinearLayout) view.findViewById(R.id.btnPostNewNews);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        btnPostNewNews.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void clickPostNews(){

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
        newsRetrofit = retrofit.create(NewsRetrofit.class);
        Call<List<NewsModels>> listCall = newsRetrofit.getNews();
        listCall.enqueue(new Callback<List<NewsModels>>() {
            @Override
            public void onResponse(Call<List<NewsModels>> call, Response<List<NewsModels>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Không có mạng", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "onResponse: "+response.body());
                    List<NewsModels> news = response.body();
                    for(NewsModels post : news){
                        newsArr.add(post);
                    }
//                    Collections.reverse(arrayPosts);
                    newsAdapter.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<NewsModels>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                call.cancel();
            }
        });
        newsAdapter = new NewsAdapter(newsArr, getContext().getApplicationContext(), mListenerNews);
        rcvNews.setAdapter(newsAdapter);
    }
    private void setUpStories(){
        viewPager2story.setVisibility(View.VISIBLE);
        fragmentCommnet.setVisibility(View.INVISIBLE);
        viewPager2story.setPageTransformer(new CubeTransformerViewpager());
        viewPager2story.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        ArrayList<StoriesModels> stories = new ArrayList<>();
        stories.add(new StoriesModels("aa",null, "ádsad", "ádsda", "ádasd"));
        stories.add(new StoriesModels("aa",null, "ádsad", "ádsda", "ádasd"));
        stories.add(new StoriesModels("aa",null, "ádsad", "ádsda", "ádasd"));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new StoriesFragment(stories, getContext(), viewPager2story));
        fragments.add(new StoriesFragment(stories, getContext(), viewPager2story));
        storiesViewpaerAdapter = new StoriesViewpaerAdapter(getFragmentManager(),getLifecycle(),fragments);
        viewPager2story.setAdapter(storiesViewpaerAdapter);
        viewPager2story.setCurrentItem(0,false);
    }
    private void clospaneSlidingUpPanel(){
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "onPanelSlide: mở");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                    animation.setDuration(500);
                    meowBottomNavigation.setVisibility(View.VISIBLE);
                    meowBottomNavigation.startAnimation(animation);
                    clearFragment();
                }
            }
        });
    }
    private void setOnClickRecyclerNews(){
        mListenerNews = new NewsAdapter.onClickRecyclerNews() {
            @Override
            public void onClickComment(int position, LinearLayout btnComment) {
                Log.d(TAG, "onClickComment: aaaaaaaaaaaaa"+ getFragmentManager().getFragments().size());
                viewPager2story.setVisibility(View.INVISIBLE);
                fragmentCommnet.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClickComment: "+position);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
                meowBottomNavigation.startAnimation(animation);
                meowBottomNavigation.setVisibility(View.GONE);
                CommentFragment commentFragment = new CommentFragment();
                getFragmentManager().beginTransaction().add(R.id.fragmentCommnet, commentFragment, "commentFragment").commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        };
    }
    private void clearFragment(){
        for (Fragment fragment : getFragmentManager().getFragments()) {
            if (fragment.getTag().equals("commentFragment")) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
            else if (fragment.getTag().equals("postNewsFragment")) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }
}