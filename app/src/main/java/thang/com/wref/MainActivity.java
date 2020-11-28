package thang.com.wref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;


import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import thang.com.wref.Main.ViewPagerAdapter;



public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private MeowBottomNavigation bottomNavigation;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        setBottomNavigation();
    }
    private void mapping(){
        bottomNavigation = findViewById(R.id.BottomNavigation);
        viewPager2 = findViewById(R.id.ViewPager2);
        viewPager2.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
    }
    private void setBottomNavigation(){
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_map_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_newspaper_folded));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_cloud_queue_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_menu_24));
        // default button

        //event
        // Set Menu Click Listener
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        Log.d(TAG, " a1 "+ item.getId());
                        viewPager2.setCurrentItem(0);
                        break;
                    case 2:
                        Log.d(TAG, " a2 "+ item.getId());
                        bottomNavigation.clearCount(2);
                        viewPager2.setCurrentItem(1);
                        break;
                    case 3:
                        Log.d(TAG, " a3 "+ item.getId());
                        bottomNavigation.clearCount(3);
                        viewPager2.setCurrentItem(2);
                        break;
                    case 4:
                        Log.d(TAG, " a4 "+ item.getId());
                        viewPager2.setCurrentItem(3);
                        break;
                    default:
                        viewPager2.setCurrentItem(0);
                        break;
                }
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });
        bottomNavigation.show(1, true);
        bottomNavigation.setCount(2, "15");
        bottomNavigation.setCount(3, "");
    }
}