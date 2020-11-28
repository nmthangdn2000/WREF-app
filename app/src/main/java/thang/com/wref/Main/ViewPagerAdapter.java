package thang.com.wref.Main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import thang.com.wref.Main.AboutFragment;
import thang.com.wref.Main.DetailWeatherFragment;
import thang.com.wref.Main.MapFragment;
import thang.com.wref.Main.SocialNetworkFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MapFragment();
            case 1:
                return new SocialNetworkFragment();
            case 2:
                return new DetailWeatherFragment();
            case 3:
                return new AboutFragment();
            default:
                return new MapFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
