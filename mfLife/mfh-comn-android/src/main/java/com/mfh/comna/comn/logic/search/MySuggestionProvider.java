package com.mfh.comna.comn.logic.search;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * 搜索建议提供实现的基类
 * 
 * @author zhangyz created on 2013-5-4
 * @since Framework 1.0
 */
public abstract class MySuggestionProvider extends CursorAdapter{

    public MySuggestionProvider(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public MySuggestionProvider(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    /**
     * 根据选择的建议位置得到建议字符串
     * @param position
     * @return
     * @author zhangyz created on 2013-5-4
     */
    public abstract String getSuggestionToken(int position);
    
    /**
     * 根据输入的字符串查询搜索建议数据
     * @param inputStr
     * @return
     */
    public abstract Cursor querySuggestData(String inputStr);
}
