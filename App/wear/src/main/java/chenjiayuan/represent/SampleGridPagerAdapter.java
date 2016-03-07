package chenjiayuan.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
    import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.Gravity;

import java.util.List;

/**
 * Created by chenjiayuan on 2/28/16.
 */
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;
    int m = 0;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[] BG_IMAGES = new int[] {
            R.drawable.curry
    };

    private final Page[][] PAGES = PageData.p;

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        //TODO: fix for demo purpose
        Page page = PAGES[row][col];
        String title = page.titleRes;
        String text = page.textRes;
        CardFragment fragment = CardFragment.create(title, text, page.iconRes);

        // Advanced settings
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(true);
        fragment.setExpansionDirection(CardFragment.EXPAND_DOWN);
        fragment.setExpansionFactor(2.0f);
        return fragment;
    }

    // Obtain the background image for the row
//    @Override
//    public Drawable getBackgroundForRow(int row) {
//        return mContext.getResources().getDrawable(
//                (BG_IMAGES[row % BG_IMAGES.length]), null);
//    }

    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        //TODO: fix demo purpose
//        if (m == -1) {
//            return GridPagerAdapter.BACKGROUND_NONE;
//        } else if (m == 0) {
//            if( row == 0 && column == 0) {
//                return mContext.getResources().getDrawable(R.drawable.curry, null);
//            } else if (row == 0 && column == 1) {
//                return mContext.getResources().getDrawable(R.drawable.tompson, null);
//            } else if (row == 0 && column == 2) {
//                return mContext.getResources().getDrawable(R.drawable.green, null);
//            } else if (row == 1 && column == 0) {
//                return mContext.getResources().getDrawable(R.drawable.obama, null);
//            } else {
//                return GridPagerAdapter.BACKGROUND_NONE;
//            }
//        } else if (m == 1) {
//            if( row == 0 && column == 0) {
//                return mContext.getResources().getDrawable(R.drawable.james, null);
//            } else if (row == 0 && column == 1) {
//                return mContext.getResources().getDrawable(R.drawable.irving, null);
//            } else if (row == 0 && column == 2) {
//                return mContext.getResources().getDrawable(R.drawable.love, null);
//            } else if (row == 1 && column == 0) {
//                return mContext.getResources().getDrawable(R.drawable.obama, null);
//            } else {
//                return GridPagerAdapter.BACKGROUND_NONE;
//            }
//        } else {
            return GridPagerAdapter.BACKGROUND_NONE;
//        }
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}
