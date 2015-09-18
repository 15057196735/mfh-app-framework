/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mfh.comna.actionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mfh.comna.R;

/**
 * 普通的actionbar，中间有title
 * @author Cyril Mottier
 */
public class ActionBar extends BaseActionBar {
    private TextView mTitleView;
    private CharSequence mTitle;

    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId(TypedArray a) {
        int type = a.getInteger(R.styleable.ActionBar_type, -1);
        int layoutID;
        switch (type) {
            /*case 2:
                mType = Type.Empty;
                layoutID = R.layout.gd_action_bar_empty;
                break;
            case 1:
                mType = Type.Dashboard;
                layoutID = R.layout.gd_action_bar_dashboard;
                break;*/
            case 0:
            default:
                mType = Type.Normal;
                layoutID = R.layout.gd_action_bar_normal;
                break;
        }
        return layoutID;
    }
    
    @Override
    protected void otherInit (TypedArray a) {
        mTitle = a.getString(R.styleable.ActionBar_actionBarTitle);//其实无用，因为先构造父类，然后再构造子类，会把子类成员变量清除。
    }
    
    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTitle(mTitle);
    }
    
    /**
     * @param title The title to set to this {@link com.mfh.comna.actionbar.ActionBar}
     */
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView == null) {
            mTitleView = (TextView) findViewById(R.id.gd_action_bar_title);            
        }
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }
}
