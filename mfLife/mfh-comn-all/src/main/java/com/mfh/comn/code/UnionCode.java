package com.mfh.comn.code;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 统一编码串，通过字符分割体现层次；统一编码串各段可以位于同一张表或跨表。
 * 跨表时通过_T_分割，表内通过_DOWN_分割。
 * 
 * @author zhangyz created on 2013-1-25
 * @since Framework 1.0
 */
public class UnionCode {
    public static String CODE_T_DEVIDE = "_T_";    
    public static String DOWN = "_DOWN_";
    
    private List<String[]> segs = null;
    
    /**
     * 是否存在多个表
     * @param code
     * @return
     * @author zhangyz created on 2012-5-8
     */
    public static boolean haveMutiTable(String code){
        if (code.indexOf(CODE_T_DEVIDE) > 0)
            return true;
        else
            return false;
    }
    
    /**
     * 是否存在多个层
     * @param code
     * @return
     * @author zhangyz created on 2012-5-9
     */
    public static boolean haveMutiDeep(String code){
        if (code.indexOf(CODE_T_DEVIDE) > 0 || code.indexOf(DOWN) > 0)
            return true;
        else
            return false;
    }
    
    /**
     * 直接按原始编码串进行初始化
     * @param code 格式如： 11_T_01_DOWN_0001_DONW_000002,其中_T_分隔的可能不存在。
     */
    public UnionCode(String code) {
        super();
        if (code == null || code.length() == 0)
            return;
        if (code.indexOf(CODE_T_DEVIDE) >= 0){
            String[] lts = StringUtils.splitByWholeSeparator(code, CODE_T_DEVIDE);
            segs = new ArrayList<String[]>(lts.length);
            for (int ii = 0; ii < lts.length; ii++){
                String [] vars = StringUtils.splitByWholeSeparator(lts[ii], DOWN);
                segs.add(vars);
            }
        }
        else{
            segs = new ArrayList<String[]>(1);        
            String[] vars = StringUtils.splitByWholeSeparator(code, DOWN);//temp.split(Constants.DOWN);
            segs.add(vars);
        }
    }
    
    /**
     * 无参构造函数，后面通过调用addCodeSegment和newCodeDivide进行编码初始化工作。
     */
    public UnionCode() {
        segs = new ArrayList<String[]>();
    }
    
    /**
     * 在调用无参构造函数后，可以连续调用该方法，所以返回类本身。
     * @param segment 编码段，按从左到右顺序可以有多段，每段都需要调用本方法添加。
     * @return
     * @author zhangyz created on 2014-3-19
     */
    public UnionCode addCodeSegment(String segment) {
        if (segs.size() == 0)
            segs.add(new String[] {});
        int dividePos = segs.size() - 1;
        String[] segArray = segs.get(dividePos);
        int oldSize = segArray.length;
        String[] newArray = new String[oldSize + 1];
        System.arraycopy(segArray, 0, newArray, 0, oldSize);
        newArray[oldSize] = segment;
        segs.set(dividePos, newArray);
        return this;
    }
    
    /**
     * 在调用无参构造函数后，可以调用该方法。启用一个新的表分隔，意味着该编码会跨表。后面调用addCodeSegment()添加的编码段都位于该新的表分隔下。
     * @return
     * @author zhangyz created on 2014-3-19
     */
    public UnionCode newCodeDivide() {
        if (segs.size() == 0) {
            segs.add(new String[] {});
            return this;
        }
        else {
            String[] segArray = segs.get(segs.size() - 1);
            if (segArray.length == 0)
                return this;//无须添加，说明可能是误调用。
            segs.add(new String[] {});
        }        
        return this;
    }
    
    /**
     * 该编码串涉及到几张表
     * @return
     * @author zhangyz created on 2012-4-15
     */
    public int getCodePartSize(){
        if (segs == null)
            return 0;
        return segs.size();
    }
    
    /**
     * 获取指定位置表的编码串
     * @param index 属于哪个表的；默认为0，代表只有一个表
     * @return
     * @author zhangyz created on 2012-4-10
     */
    public String[] getCodeInOneTable(int index){
        if (segs == null)
            return null;
        if (index >= segs.size())
            return null;
        return segs.get(index);
    }
    
    /**
     * 获取指定位置表的编码串
     * @param index 属于哪个表的；默认为0，代表只有一个表
     * @param index
     * @param assertLength 断定返回串有几个
     * @return
     * @author zhangyz created on 2012-5-29
     */
    public String[] getCodeInOneTable(int index, int assertLength){
        if (segs == null)
            return null;
        if (index >= segs.size())
            return null;
        String[] ret =  segs.get(index);
        if (ret == null || ret.length != assertLength)
            throw new RuntimeException("不正确的codeId查询参数,应有" + assertLength + "段!");
        
        return ret;
    }
    
    /**
     * 获取最后一部分的统一编码串；各部分是可以独立存在的
     * 
     * @author zhangyz created on 2012-5-30
     */
    public String getUnCodeInLastTable(){
        if (segs == null)
            return null;
        String[] rets = segs.get(segs.size() - 1);
        if (rets.length == 1)
            return rets[0];
        StringBuilder builder = new StringBuilder(rets[0]);
        for (int ii = 1; ii < rets.length; ii++){
            builder.append(DOWN).append(rets[ii]);
        }
        return builder.toString();
    }
    
    /**
     * 获取最后一部分的编码串数组
     * @return
     * @author zhangyz created on 2012-4-10
     */
    public String[] getCodeInLastTable(){
        if (segs == null)
            return null;
        return segs.get(segs.size() - 1);
    }
    
    /**
     * 获取最后一部分的编码串
     * @param assertLength 断定返回串有几个
     * @return
     * @author zhangyz created on 2012-5-29
     */
    public String[] getCodeInLastTable(int assertLength){
        if (segs == null)
            return null;
        String[] ret = segs.get(segs.size() - 1);
        if (ret == null || ret.length != assertLength)
            throw new RuntimeException("不正确的codeId查询参数,应有" + assertLength + "段!");
        return ret;
    }
    
    
    /**
     * 针对某父子关系的编码串，取其最后一层值作为编码值
     * @param index 属于哪个表的，默认为0，代表只有一个表
     * @return
     * @author zhangyz created on 2012-4-11
     */
    public String getLastCodeInOneTable(int index){
        if (segs == null)
            return null;
        if (index >= segs.size())
            return null;
        String[] strs = segs.get(index);
        return strs[strs.length - 1];
    }
    
    @Override
    public String toString() {
        if (segs == null)
            return null;
        StringBuilder ret = new StringBuilder();
        for (String[] item : segs) {
            if (ret.length() > 0)
                ret.append(CODE_T_DEVIDE);
            for (int ii = 0; ii < item.length; ii++) {
                if (ii > 0)
                    ret.append(DOWN);
                ret.append(item[ii]);
            }
        }
        return ret.toString();
    }
    
    /**
     * 直接获取统一编码串的最后一小段.
     * @param code
     * @return
     * @author zhangyz created on 2012-6-25
     */
    public static String getLastCodeInLastTable(String code){
        if (code == null || code.length() == 0)
            return null;
        
        int tn = code.lastIndexOf(CODE_T_DEVIDE);
        int dn = code.lastIndexOf(DOWN);
        int inLength = 0;
        if(tn > dn) {
            inLength = tn;
            inLength +=  CODE_T_DEVIDE.length();
        }
        else if(dn > tn) {
            inLength = dn;
            inLength += DOWN.length();
        }
        else if (tn == -1)
            return code;
        
        code = code.substring(inLength);
        return code;
    }
    
    /**
     * 针对统一编码串中最后部分的父子关系的编码串，取其最后一层值作为编码值
     * @return
     * @author zhangyz created on 2012-4-11
     */
    public String getLastCodeInLastTable(){
        if (segs == null)
            return null;
        String[] strs = segs.get(segs.size() - 1);
        return strs[strs.length - 1];
    }
    
    /**
     * 断言传入的当前编码值参数串应该有几段
     * 
     * @param segs 已解析的编码串
     * @param length
     * @return 返回传入的参数,便于后续级联操作
     * @author zhangyz created on 2012-4-12
     */
    public static String[] assertParam(String[] segs, int length) {
        if(segs == null || segs.length != length)
            throw new RuntimeException((new StringBuilder("传入的字符串数组应该是")).append(length).append("段!").toString());
        else
            return segs;

    }

    public static List<String> assertParam(List<String> segs, int length) {
        if(segs == null || segs.size() != length)
            throw new RuntimeException((new StringBuilder("传入的字符串列表应该是")).append(length).append("段!").toString());
        else
            return segs;

    }
}
