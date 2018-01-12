package com.bionic.mui.adapter.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bionic.mui.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultifunctionalAdapter extends RecyclerView.Adapter<MultifunctionalAdapter.ViewHolder> implements Filterable{

    private final String TAG = MultifunctionalAdapter.class.getSimpleName();

    protected List<Map<String, Object>> mDataset;
    protected List<Map<String, Object>> mOriginDataset;
    protected List<Map<String, Object>> mFilterDataset;
    protected int mLayout;
    protected String[] mFrom;
    protected int[] mTo;
    protected boolean mHasHead;
    protected boolean mHasTail;
    protected OnViewActionBindListener mBindListener;

    protected  KeyWordFilter mFilter;
    class KeyWordFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            LogUtil.d(TAG, "performFiltering() constraint = "+ constraint
                    + ", mOriginDataset.size() = "+ mOriginDataset.size());
            FilterResults res = new FilterResults();

            mFilterDataset = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < mOriginDataset.size(); i++) {
                if ((mHasHead && i == 0)
                        || (mHasTail && i == mOriginDataset.size() - 1)) {
                    continue;
                }
                Map<String, Object> map = mOriginDataset.get(i);
                for (Object key : map.keySet()) {
                    Object value = map.get(key);
                    if (value instanceof String) {
                        String text = (String)value;
                        if (text.contains(constraint)) {
                            mFilterDataset.add(map);
                            break;
                        }
                    }
                }
            }

            res.count = mFilterDataset.size();
            res.values = mFilterDataset;

            return res;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            LogUtil.d(TAG, "publishResults() results.size() = "+ results.count);
            mDataset = mFilterDataset;
            notifyDataSetChanged();
        }
    }

    public void restoreOriginDataSet() {
        mDataset = mOriginDataset;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new KeyWordFilter();
        }
        return mFilter;
    }

    public void setHasHeadTail(boolean hasHead, boolean hasTail) {
        mHasHead = hasHead;
        mHasTail = hasTail;
    }

    public interface OnViewActionBindListener {
        void onBind(RecyclerView.ViewHolder holder);
    }

    public MultifunctionalAdapter(List<Map<String, Object>> data, int resource, String[] from,
                                  int[] to, OnViewActionBindListener listener) {
        mOriginDataset = mDataset = data;
        mLayout = resource;
        mFrom = from;
        mTo = to;
        mBindListener = listener;
    }

    public static class ItemSpaceDecoration extends RecyclerView.ItemDecoration {

        private int spaceInPixel;
        private final Drawable divider;
        private final int orientation;
        private int padding;

        public ItemSpaceDecoration(int orientation, Drawable divier,
                                        int spaceInPixel, int padding) {
            this.divider = divier;
            this.spaceInPixel = spaceInPixel;
            this.orientation = orientation;
            this.padding = padding;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            c.drawColor(0xffffffff);

            int left;
            int right;
            int top;
            int bottom;

            if (orientation == LinearLayoutManager.HORIZONTAL) {
                top = parent.getPaddingTop();
                bottom = parent.getHeight() - parent.getPaddingBottom();
                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount - 1; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) child.getLayoutParams();
                    left = child.getRight() + params.rightMargin;
                    right = left + spaceInPixel;
                    divider.setBounds(left, top, right, bottom);
                    divider.draw(c);
                }
            } else {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();

                final int childCount = parent.getChildCount();
                for (int i = 0; i < childCount - 1; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) child.getLayoutParams();
                    top = child.getBottom() + params.bottomMargin;
                    bottom = top + spaceInPixel;
                    divider.setBounds(left + padding, top, right - padding, bottom);
                    divider.draw(c);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.set(spaceInPixel/2, 0, spaceInPixel/2, 0);
            } else {
                outRect.set(0, spaceInPixel/2, 0, spaceInPixel/2);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View layout) {
            super(layout);
        }
    }

    public List<Map<String, Object>> getDataset() {
        return mDataset;
    }

    public List<Map<String, Object>> getOriginDataset() {
        return mOriginDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MultifunctionalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        mBindListener.onBind(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> m = mDataset.get(position);
        for (int i = 0; i < mTo.length; i++) {
            View v = holder.itemView.findViewById(mTo[i]);
            Object value = m.get(mFrom[i]);
            if (v instanceof ImageView) {
                if (value == null) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }
                if (value instanceof Integer) {
                    ((ImageView) v).setImageResource((int) value);
                } else if (value instanceof Drawable) {
                    ((ImageView) v).setImageDrawable((Drawable) value);
                }
            } else if (v instanceof AppCompatRadioButton) {
                if (value == null) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }
                if (value instanceof Boolean) {
                    ((AppCompatRadioButton) v).setChecked((boolean) value);
                }
            } else if (v instanceof RadioButton) {
                if (value == null) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }
                if (value instanceof Boolean) {
                    ((RadioButton) v).setChecked((boolean) value);
                }
            } else if (v instanceof AppCompatTextView) {
                if (value == null) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }

                String text = null;
                if (value instanceof Integer) {
                    text = v.getContext().getString((int) value);
                } else if (value instanceof String){
                    text = (String)value;
                }

                if (text == null) {
                    text = "";
                }

                AppCompatTextView tv = (AppCompatTextView) v;
                Paint paint = tv.getPaint();
                if (text.matches("<s>(.|\n)*</s>")) {
                    paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    text = text.replace("<s>","").replace("</s>", "");
                } else {
                    paint.setFlags(Paint.LINEAR_TEXT_FLAG);
                }

                paint.setAntiAlias(true);
                tv.setText(text);
            } else if (v instanceof TextView) {
                if (value == null) {
                    v.setVisibility(View.GONE);
                } else {
                    v.setVisibility(View.VISIBLE);
                }

                String text = null;
                if (value instanceof Integer) {
                    text = v.getContext().getString((int) value);
                } else if (value instanceof String){
                    text = (String)value;
                }

                TextView tv = (TextView) v;
                Paint paint = tv.getPaint();
                if (text.matches("<s>(.|\n)*</s>")) {
                    paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    text = text.replace("<s>","").replace("</s>", "");
                } else {
                    paint.setFlags(Paint.LINEAR_TEXT_FLAG);
                }

                paint.setAntiAlias(true);
                tv.setText(text);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
