package com.eduardosantos.prototipo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UserViewPagerAdapter extends FragmentStateAdapter {
    private final String userName;
    private final String userEmail;

    public UserViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String userName, String userEmail) {
        super(fragmentActivity);
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserFragmentHome();
            case 1:
                return new UserFragmentOptions();
            case 2:
                return new UserFragmentAccount(userName, userEmail);
            default:
                return new UserFragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
