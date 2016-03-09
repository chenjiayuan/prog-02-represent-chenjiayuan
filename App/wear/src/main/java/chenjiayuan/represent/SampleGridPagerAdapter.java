package chenjiayuan.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.View;

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
        Page page = PAGES[row][col];
        String title = page.titleRes;
        String text = page.textRes;
        final MyFragment fragment = new MyFragment();
        final int r = row;
        final int c = col;
        fragment.setTitle(page.titleRes);
        fragment.setText(page.textRes);

        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (r == 0) {
                    Intent intent = new Intent(mContext, WatchToPhoneService.class);
                    intent.putExtra("mode", "select");
                    intent.putExtra("index", Integer.toString(c));
                    mContext.startService(intent);
                }
            }
        });
        return fragment;
    }

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
