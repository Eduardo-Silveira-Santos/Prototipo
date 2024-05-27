package com.eduardosantos.prototipo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final String userName;
    private final String userEmail;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String userName, String userEmail) {
        super(fragmentActivity);
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentHome();
            case 1:
                return new FragmentOptions();
            case 2:
                return new FragmentAccount(userName, userEmail);
            default:
                return new FragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
