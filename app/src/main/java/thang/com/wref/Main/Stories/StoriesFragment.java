package thang.com.wref.Main.Stories;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import thang.com.wref.Models.StoriesModels;
import thang.com.wref.R;

public class StoriesFragment extends Fragment implements StoriesProgressView.StoriesListener {
    private static final String TAG = "StoriesFragment";
    private View view, reverse, skip;
    private Context context;
    private CircleImageView userAvata;
    private TextView txtusername, txtTimeStory;
    private ImageView btnClose;
    private RelativeLayout RelativeStoriesInfor;
    private LinearLayout imgStoryhear, linearStoriesBottom;
    private StoriesProgressView stories;
    private ViewPager2 viewPager2story;

    private ArrayList<StoriesModels> arrayList;

    long pressTime = 0L;
    long limit = 500L;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    stories.pause();
                    RelativeStoriesInfor.animate().alpha(0.0f).setDuration(500);
                    imgStoryhear.animate().alpha(0.0f).setDuration(500);
                    Log.d(TAG,"ACTION_DOWN");
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    stories.resume();
                    Log.d(TAG,"ACTION_UP");
                    RelativeStoriesInfor.animate().alpha(1.0f).setDuration(500);
                    linearStoriesBottom.animate().alpha(1.0f).setDuration(500);
                    return limit < now - pressTime;
                case MotionEvent.ACTION_CANCEL:
                    long now1 = System.currentTimeMillis();
                    stories.resume();
                    Log.d(TAG,"ACTION_CANCEL");
                    RelativeStoriesInfor.animate().alpha(1.0f).setDuration(500);
                    linearStoriesBottom.animate().alpha(1.0f).setDuration(500);
                    return limit < now1 - pressTime;
            }
            return true;
        }
    };
    public StoriesFragment(ArrayList<StoriesModels> arrayList, Context context, ViewPager2 viewPager2story) {
        this.arrayList = arrayList;
        this.context = context;
        this.viewPager2story = viewPager2story;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stories, container, false);
        mapingView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupStories();
    }
    private void mapingView(){
        RelativeStoriesInfor = (RelativeLayout) view.findViewById(R.id.RelativeStoriesInfor);
        stories = (StoriesProgressView) view.findViewById(R.id.stories);
        imgStoryhear = (LinearLayout) view.findViewById(R.id.imgStoryhear);
        linearStoriesBottom = (LinearLayout) view.findViewById(R.id.linearStoriesBottom);
    }
    private void setupStories(){
        stories.setStoriesCount(3); // <- set stories
        stories.setStoryDuration(1200L); // <- set a story duration
        stories.setStoriesListener(this); // <- set listener
        stories.startStories(); // <- start progress
    }
    @Override
    public void onNext() {

    }

    @Override
    public void onPrev() {

    }

    @Override
    public void onComplete() {

    }
}