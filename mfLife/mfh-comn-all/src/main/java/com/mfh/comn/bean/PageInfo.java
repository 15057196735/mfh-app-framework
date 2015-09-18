/*
 * 文件名称: PageInfo.java
 * 版权信息: Copyright 2001-2011 ZheJiang chunchen Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-12-14
 * 修改内容: 
 */
package com.mfh.comn.bean;
/**
 * 分页信息,注意第一页用1表示，不是0，这与服务器端一致。
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2011-12-14
 */
public class PageInfo {
    public static final int FIRST_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public final static int TOTALCOUNT_NOTINIT = -1;
    public final static int PAGENO_NOTINIT = 0;
    public final static int PAGENO_LASTNO = 999999999;//代表最后一页，具体页数需要待定。
    private boolean fromLast = false;
    
    /** 当前页号，1代表第一页；0代表未初始化 */
    private int pageNo = PAGENO_NOTINIT;
    
    /** 每页记录条数 */
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    /** 总页数*/
    private int totalPage = 0;    

    /** 总记录数  */
    private int totalCount = TOTALCOUNT_NOTINIT;
    
    /** 默认构造函数 */
    public PageInfo() {
        pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * 翻页次序，是否从后往前
     * @return
     */
    public boolean isFromLast() {
        return fromLast;
    }

    /**
     * 构造函数
     * @param pageSize
     */
    public PageInfo(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /** 构造函数 */
    public PageInfo(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        if (pageNo == PAGENO_LASTNO)
            fromLast = true;
        else
            fromLast = false;
        this.pageSize = pageSize;
    }

    /**
     * 构造函数
     * @param fromLast 是否反向
     * @param pageSize 页数
     */
    public PageInfo(boolean fromLast, int pageSize) {
        this.pageSize = pageSize;
        this.fromLast = fromLast;
        if (fromLast)
            this.pageNo = PAGENO_LASTNO;
        else
            this.pageNo = PAGENO_NOTINIT;
    }

    /**
     * 设置总数
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        reCalcTotalCount(totalCount, pageSize);
    }
    
    /**
     * 重置
     * @author zhangyz created on 2013-4-11
     */
    public void reset() {
        totalPage = 0;
        this.totalCount = TOTALCOUNT_NOTINIT;
        if (fromLast)
            this.pageNo = PAGENO_LASTNO;
        else
            this.pageNo = PAGENO_NOTINIT;
    }

    /**
     * @param totalCount
     * @author huangwb created on 2012-2-23 
     * @since 
     */
    private void reCalcTotalCount(int totalCount, int pageSize) {
        if (pageSize != 0) {
            totalPage = totalCount / pageSize;
            if (totalCount % pageSize > 0) {
                totalPage ++;
            }
            //如果指示从后向开始且未初始化，则直接跳到开始处（即最后一页）
            if (pageNo == PAGENO_LASTNO)
                this.moveToFirst();
            else if (pageNo == PAGENO_LASTNO - 1) {//可能已经moveNext过一次了。
                this.moveToFirst();
                this.moveToNext();//补充move一下
            }
        }
    }
        
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return
     * @author zhangyz created on 2013-4-11
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置页号，1代表第一页。
     * 建议用prePageNo和nextPageNo代替
     * @param pageNo
     * @author zhangyz created on 2013-4-11
     */
    public void setPageNo(int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
            if (pageNo == PAGENO_LASTNO)
                fromLast = true;
            else
                fromLast = false;
        }
    }

    public int getPageIndex() {
        return pageNo - 1;
    }
    
    /**
     * 是否还未初始化过，即没有读过总记录数
     * @return
     * @author zhangyz created on 2013-4-11
     */
    public boolean isNotInit() {
        if (this.totalCount == TOTALCOUNT_NOTINIT)
            return true;
        else
            return false;
    }

    /**
     * 是否还有下一页
     * @return
     * @author zhangyz created on 2013-4-11
     */
    public boolean hasNextPage() {
        if (fromLast) {
            if (pageNo >= FIRST_PAGE_NO || isNotInit())
                return true;
            else
                return false;
        }
        else {
            if (pageNo < totalPage || isNotInit())
                return true;
            else
                return false;
        }
    }

    /**
     * 翻到下一页
     * @return 返回页号
     * @author zhangyz created on 2013-4-11
     */
    public int moveToNext() {
        if (fromLast) {
            if (hasNextPage())
                pageNo--;
            return pageNo - 1;
        }
        else {
            if (hasNextPage())
                pageNo++;
            else
                throw new RuntimeException("已经是最后一页!");
            return pageNo - 1;
        }
    }

    /**
     * 翻到最后一页
     * @return 返回上页
     * @author zhangyz created on 2013-4-15
     */
    public void moveToLast () {
        if (fromLast)
            pageNo = FIRST_PAGE_NO;
        else
            pageNo = this.totalPage;
    }
    
    /**
     * 移到第一页
     * 
     * @author zhangyz created on 2013-4-15
     */
    public void moveToFirst() {
        if (fromLast)
            pageNo = this.totalPage;
        else
            pageNo = FIRST_PAGE_NO;
    }

    /**
     * 是否还有上一页
     * @return
     * @author zhangyz created on 2013-4-13
     */
    public boolean hasPrevPage() {
        if (fromLast) {
            if (pageNo < this.totalPage)
                return true;
            else
                return false;
        }
        else {
            if (pageNo <= FIRST_PAGE_NO)
                return false;
            else
                return true;
        }
    }

    /**
     * 移到上一页
     * @return 返回页号
     * @author zhangyz created on 2013-4-11
     */
    public int moveToPrev() {
        if (fromLast) {
            pageNo++;
            return pageNo - 1;
        }
        else {
            pageNo--;
            return pageNo - 1;
        }
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
            reCalcTotalCount(totalCount, pageSize);
        }
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    /**
     * 获取已经读取的个数。不是很精确。因为最后一页可能没有读满。
     * @return 0:代表还未读取；>0,代表读取至的位置
     * @author zhangyz created on 2013-5-7
     */
    public int getHavedCount() {
        int haveCount;
        if (fromLast)
            haveCount = (totalPage - pageNo) * pageSize;
        else
            haveCount = (pageNo) * pageSize;
        if (haveCount < 0)
            haveCount = 0;
        if (haveCount > totalCount)
            haveCount = totalCount;
        return haveCount;
    }

    /**
     * 获取上一次读取结束的位置，用于分页
     * @return 0:代表还未读取；>0,代表读取至的位置
     * @author zhangyz created on 2013-5-7
     */
    public int getStartCount() {
        int haveCount;
        if (fromLast)
            haveCount = pageNo * pageSize;
        else
            haveCount = (pageNo -1) * pageSize;
        if (haveCount < 0)
            haveCount = 0;
        return haveCount;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return totalPage;
    }
    
    /**
     * 直接设置成一页
     * @param totalCount
     * @author zhangyz created on 2013-6-6
     */
    public void setToOnePage(int totalCount) {
        if (isNotInit()) {//一次性查出，无须分页。
            setPageSize(totalCount + 1);
            setTotalCount(totalCount);
        }
    }
}
