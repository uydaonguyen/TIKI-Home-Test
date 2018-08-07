package com.thanhuy.tiki.hometest.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * reference link : http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/
 *
 * The Class SpacesItemRecyclerViewDecoration extends from Class RecyclerView.ItemDecoration {@link RecyclerView.ItemDecoration}
 *
 *
 * author:uy.daonguyen@gmail.com
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration{

    private int mSpanCount;
    private int mSpacing;
    private boolean mincludeEdge;

    public SpacesItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mincludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column

        if (mincludeEdge) {
            if(mSpanCount > 1){
                outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * mSpacing / mSpanCount; // (column
            } else {
                outRect.left = 0;
                outRect.right = 0;
            }
            // start for spacing gridlayout
//            outRect.left = mSpacing - column * mSpacing / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
//            outRect.right = (column + 1) * mSpacing / mSpanCount; // (column + 1) * ((1f / spanCount) * spacing)
            // end for spacing gridlayout

            // start for spacing linenar layout
//            outRect.left = 0;
//            outRect.right = 0;
            // end for spacing linenar layout

            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing; // item bottom
        } else {
            outRect.left = column * mSpacing / mSpanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

            if (position >= mSpanCount) {
                outRect.top = mSpacing; // item top
            }
        }
    }
}
/**
 *
 * spacing = 4
 * 2 column
 * 0 1
 Position Left,Right

 0  4,2 => column 0

 1  2,4 => column 1

 2  4,2 => column 0

 3  2,4 => column 1

 4  4,2 => column 0

 5  2,4 => column 1

 6  4,2 => column 0

 7  2,4 => column 1

 8  4,2 => column 0

 9  2,4 => column 1

 * */