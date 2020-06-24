package me.sheasmith.weatherstation;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    int paddingTop;
    int paddingBottom;

    private ArrayList<ForecastFragment> arrayList = new ArrayList<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    public void addFragment(ForecastFragment fragment) {
        arrayList.add(fragment);
        fragment.setPadding(paddingTop, paddingBottom);
    }

    public void clearFragments() {
        arrayList.clear();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // return your fragment that corresponds to this 'position'
        return arrayList.get(position);
    }

    public void setPadding(int top, int bottom) {
        this.paddingTop = top;
        this.paddingBottom = bottom;
        for (ForecastFragment fragment : arrayList) {
            fragment.setPadding(top, bottom);
        }
    }

    public ForecastFragment getFragment(int index) {
        return arrayList.get(index);
    }

}