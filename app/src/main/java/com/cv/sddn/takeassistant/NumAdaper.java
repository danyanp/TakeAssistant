package com.cv.sddn.takeassistant;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Created by sddn on 2018/11/30.
 */

public abstract class NumAdaper extends BaseAdapter {

    private Context mContext;
    private String[] mArr;

    public NumAdaper(Context context, String[] arr)
    {
        mContext = context;
        mArr = arr;
    }

    @Override
    public int getCount()
    {
        return mArr.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

}