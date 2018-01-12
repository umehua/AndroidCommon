package com.bionic.mui.adapter.listview;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

public class MultifunctionalAdapter extends SimpleAdapter {

	private static final String TAG = MultifunctionalAdapter.class.getSimpleName();
	
	public static final int TO_VIEW_TAG = -9999;
	
	protected Context mContext;
	protected List<? extends Map<String, ?>> mData;
	protected int mResource;
	protected LayoutInflater mInflater;
	protected String[] mFrom;
	protected int[] mTo;
	
	public MultifunctionalAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mContext = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public List<? extends Map<String, ?>> getData() {
		return mData;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}

		bindView(position, v);

		return v;
	}

	protected void bindView(int position, View view) {
		final Map dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}

		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			if (to[i] == TO_VIEW_TAG) {
				view.setTag(dataSet.get(from[i]));
				continue;
			}
			
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				boolean bound = false;
				if (!bound) {
					if (v instanceof Switch) {
						if (data != null) {
							v.setVisibility(View.VISIBLE);
							((Switch)v).setChecked((Boolean)data);
						} else {
							v.setVisibility(View.INVISIBLE);
						}
					} else if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at
							// the bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(
									v.getClass().getName() + " should be bound to a Boolean, not a "
											+ (data == null ? "<unknown type>" : data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						v.setVisibility(View.VISIBLE);
                        if (data instanceof Spanned) {
                            ((TextView)v).setText((Spanned)data);
                        } else {
                            setViewText((TextView) v, text);
                        }
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else if (data == null || data instanceof Drawable) {
							((ImageView) v).setImageDrawable((Drawable) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else if (v instanceof RadioButton) {
						if (data instanceof Boolean) {
							((RadioButton) v).setChecked((Boolean)data);
						}
					} else {
						throw new IllegalStateException(v.getClass().getName() + " is not a "
								+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}
}