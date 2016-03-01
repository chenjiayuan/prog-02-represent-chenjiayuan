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
    int m = -1;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm, String mode) {
        super(fm);
        mContext = ctx;
        if (mode != null) {
            if (mode.equals("zipcode")) m = 0;
            else if (mode.equals("currentLocation")) m = 1;
        }
    }

    static final int[] BG_IMAGES = new int[] {
            R.drawable.curry
    };

    // A simple container for static data in each page
    private static class Page {
        int titleRes;
        int textRes;
        int iconRes;

        public Page(int titleRes, int textRes, int iconRes) {
            this.titleRes = titleRes;
            this.textRes = textRes;
            this.iconRes = iconRes;
        }
    }
    // Create a static set of pages in a 2D array
    private final Page[][] PAGES1 = {
            {
                    new Page(R.string.name1, R.string.party1, 0),
                    new Page(R.string.name2, R.string.party2, 0),
                    new Page(R.string.name3, R.string.party3, 0),
            },
            {
                    new Page(R.string.title2012, R.string.stat, 0)
            }
    };
    private final Page[][] PAGES2 = {
            {
                    new Page(R.string.name11, R.string.party11, 0),
                    new Page(R.string.name22, R.string.party22, 0),
                    new Page(R.string.name33, R.string.party33, 0),
            },
            {
                    new Page(R.string.title20122, R.string.stat2, 0)
            }
    };
    private final Page[][] PAGES_EMPTY = {
            {
                    new Page(R.string.name0, R.string.party0, 0),
                    new Page(R.string.name0, R.string.party0, 0),
                    new Page(R.string.name0, R.string.party0, 0),
            },
            {
                    new Page(R.string.name0, R.string.name0, 0)
            }
    };

    // Override methods in FragmentGridPagerAdapter
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        //TODO: fix for demo purpose
        Page page;
        if (m == 0) {
            page = PAGES1[row][col];
        } else if (m == 1) {
            page = PAGES2[row][col];
        } else {
            page = PAGES_EMPTY[row][col];
        }
        String title =
                page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
        String text =
                page.textRes != 0 ? mContext.getString(page.textRes) : null;
        CardFragment fragment = CardFragment.create(title, text, page.iconRes);

        // Advanced settings (card gravity, card expansion/scrolling)
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
        if (m == -1) {
            return GridPagerAdapter.BACKGROUND_NONE;
        } else if (m == 0) {
            if( row == 0 && column == 0) {
                return mContext.getResources().getDrawable(R.drawable.curry, null);
            } else if (row == 0 && column == 1) {
                return mContext.getResources().getDrawable(R.drawable.tompson, null);
            } else if (row == 0 && column == 2) {
                return mContext.getResources().getDrawable(R.drawable.green, null);
            } else if (row == 1 && column == 0) {
                return mContext.getResources().getDrawable(R.drawable.obama, null);
            } else {
                return GridPagerAdapter.BACKGROUND_NONE;
            }
        } else if (m == 1) {
            if( row == 0 && column == 0) {
                return mContext.getResources().getDrawable(R.drawable.james, null);
            } else if (row == 0 && column == 1) {
                return mContext.getResources().getDrawable(R.drawable.irving, null);
            } else if (row == 0 && column == 2) {
                return mContext.getResources().getDrawable(R.drawable.love, null);
            } else if (row == 1 && column == 0) {
                return mContext.getResources().getDrawable(R.drawable.obama, null);
            } else {
                return GridPagerAdapter.BACKGROUND_NONE;
            }
        } else {
            return GridPagerAdapter.BACKGROUND_NONE;
        }
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return PAGES1.length;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES1[rowNum].length;
    }
}
