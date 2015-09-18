package com.mfh.comna.comn.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mfh.comna.comn.bean.ILetterIndexAble;
import com.mfh.comna.comn.bean.KvBean;

public class LetterPageHelper <T extends ILetterIndexAble>{
    private Map<String, Integer> selector = new HashMap<String, Integer>();// 键值是索引表的字母，值为对应在listview中的位置
    private List<String> blankIndexs = new ArrayList<String>();//还未有关联条目的字母索引
    private MyPageListAdapter<T> adapter;
    public static String[] indexLetters;
    
    public final static String[] letters = new String[] {
        "A", "B", "C", "D", "E", "F", "G", 
        "H", "I", "J", "K", "L", "M", "N", 
        "O", "P", "Q", "R", "S", "T", 
        "U", "V", "W", "X", "Y", "Z"
    };
    
    /**
     * 构造函数
     * @param adapter
     * @param params
     */
    public LetterPageHelper(MyPageListAdapter<T> adapter, List<KvBean<T>> params, String[] paramLetters) {
        if (paramLetters == null)
            indexLetters = letters;
        else
            indexLetters = paramLetters;
        initBlankIndexs(indexLetters);
        this.adapter = adapter;
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (blankIndexs.size() == 0)
                    break;
                fillItemIndex(params.get(i));
            }
        }
    }
    
    /**
     * 初始化
     * @author zhangyz created on 2013-5-11
     */
    private void initBlankIndexs(String[] indexLetters) {
        blankIndexs.clear();
        for (String item : indexLetters)
            blankIndexs.add(item);
    }

    public Map<String, Integer> getSelector() {
        return selector;
    }
    
    /**
     * 对传入的值取其索引字母并填充到索引中
     * @param param
     * @author zhangyz created on 2013-5-11
     */
    private void fillItemIndex(KvBean<T> param){
        String letter;
        for (int j = blankIndexs.size() - 1; j >= 0; j--) {//循环字母表，找出list中对应字母的位置
            letter = blankIndexs.get(j);
            if (letter.equals(param.getBean().getLetterIndex())) {
                selector.put(letter, adapter.dataItems.indexOf(param));
                blankIndexs.remove(j);//每个字母只要有一个对应的
                break;
            }                    
        }
    }
    
    public void addDataItems(List<KvBean<T>> params) {
        for (int i = 0; i < params.size(); i++) {
            if (blankIndexs.size() == 0)
                break;
            fillItemIndex(params.get(i));
        }
    }

    public void addDataItems(KvBean<T>[] params) {
        for (int i = 0; i < params.length; i++) {
            if (blankIndexs.size() == 0)
                break;
            fillItemIndex(params[i]);
        }
    }

    public void addDataItem(KvBean<T> param) {
        fillItemIndex(param);
    }

    public void clearData() {
        selector.clear();
        initBlankIndexs(indexLetters);
    }
}
