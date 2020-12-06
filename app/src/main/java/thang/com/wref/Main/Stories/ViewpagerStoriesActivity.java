package thang.com.wref.Main.Stories;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;

public class ViewpagerStoriesActivity extends Fragment {
    private View view;

    private ViewPager2 viewPager2story;
    private StoriesViewpaerAdapter storiesViewpaerAdapter;

//    private StoryRetrofit storyRetrofit;
    private NetworkUtil networkUtil;
    private Retrofit retrofit;

    private SharedPreferences sessionManagement;
    private String id ="", token = "";
    private int numberStory;
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
    private void mapingView(){
        viewPager2story = (ViewPager2) view.findViewById(R.id.storiesViewpager);
//        Intent intent = getContext().getApplicationContext().getIntent();
//        numberStory  = (int) intent.getSerializableExtra("numberClickStory");
        Log.d("storhjkl", " "+numberStory);
        networkUtil = new NetworkUtil();
        getStory();
        viewPager2story.setPageTransformer(new CubeTransformerViewpager());
        viewPager2story.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
    }
    private void getStory() {
        sessionManagement = getContext().getApplicationContext().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        id = sessionManagement.getString("id","");
        token = "Bearer "+sessionManagement.getString("token","");
        List<Fragment> fragments = new ArrayList<>();
        retrofit = networkUtil.getRetrofit();
//        storyRetrofit = retrofit.create(StoryRetrofit.class);
//        Call<List<Story>> callstory = storyRetrofit.getStory(token);
//        callstory.enqueue(new Callback<List<Story>>() {
//            @Override
//            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
//                if(!response.isSuccessful()){
//                    Toast.makeText(thang.com.uptimum.Main.other.Stories.ViewpagerStoriesActivity.this, "lỗi rác story", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                List<Story> storys = response.body();
//                for(Story story : storys){
//                    if(story.getUsers().getId().equals(id)){
//                        ArrayList<Story> stories = new ArrayList<>();
//                        stories.add(story);
//                        fragments.add(new StoriesFragment(stories, viewPager2story));
//                        break;
//                    }
//                }
//                for(Story story : storys){
//                    if(!story.getUsers().getId().equals(id)){
//                        ArrayList<Story> stories = new ArrayList<>();
//                        stories.add(story);
//                        fragments.add(new StoriesFragment(stories, viewPager2story));
//                    }
//                }
////                Collections.reverse(arrayStory);
                storiesViewpaerAdapter = new StoriesViewpaerAdapter(getFragmentManager(),getLifecycle(),fragments);
                viewPager2story.setAdapter(storiesViewpaerAdapter);
                viewPager2story.setCurrentItem(numberStory,false);
//            }
//
//            @Override
//            public void onFailure(Call<List<Story>> call, Throwable t) {
//                Log.d("loaddataa","Load không được lỗi : "+t.getMessage());
//            }
//        });
    }
}
