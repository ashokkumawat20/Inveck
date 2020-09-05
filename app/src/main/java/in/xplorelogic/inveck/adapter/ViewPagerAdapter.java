package in.xplorelogic.inveck.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

/* renamed from: com.app.xplorelogic.inveck.UI.auth.ViewPagerAdapter */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> mFragTitleList = new ArrayList<>();
    ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int position) {
        return (Fragment) this.mFragmentList.get(position);
    }

    public int getCount() {
        return this.mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        this.mFragmentList.add(fragment);
        this.mFragTitleList.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return (CharSequence) this.mFragTitleList.get(position);
    }
}
