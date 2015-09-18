package com.mfh.comna.comn.database.dao;

import com.mfh.comn.bean.IObject;
import com.mfh.comn.bean.Pair;
import com.mfh.comn.logic.SeqInit;
import com.mfh.comn.logic.SeqInit.SeqArea;
import com.mfh.comna.comn.logic.ServiceFactory;
import com.mfh.comna.comn.seq.SequenceService;

import net.tsz.afinal.db.table.TableInfo;

/**
 * 客户端dao基类,带有序列服务的，可以实现主键自增的，并且跨数据库唯一的。
 * 一般都需要从本类继承。
 * @param <T>
 * @author zhangyz created on 2013-6-7
 * @since Framework 1.0
 */
public abstract class BaseSeqAbleDao<T extends IObject, PK>  extends BaseDbDao<T, PK>{
    private String sequeceName = null;
        
    //提供生成自增主键的服务类
    protected SequenceService seqService = ServiceFactory.getSequenceService();
    
    /**
     * 获取本数据库自增序列的范围.一万~千万
     * one:一万； ten：十万 ....
     * @return 若返回str或null,代表是使用字符串
     * @author zhangyz created on 2013-6-7
     */
    protected SeqArea getSequenceArea() {
        return SeqArea.ten;
    }
    
    /**
     * 构造函数
     */
    public BaseSeqAbleDao() {
        super();        
        sequeceName = this.getTableName();        
        SeqArea seqArea = getSequenceArea();
        Pair<Long, Long> ret = SeqInit.getSeqLong(seqArea);
        if (ret != null && ret.getV1() != null && ret.getV2() != null)
            seqService.checkSequence(sequeceName, ret.getV1(), ret.getV2());
    }
    
    /**
     * 产生自增主键
     * @return
     * @author zhangyz created on 2013-6-15
     */
    protected Object genAutoId() {
        SeqArea seqArea = getSequenceArea();
        if (seqArea == null)
            return this.seqService.getUUID();
        else {
            if (seqArea.equals(SeqArea.thousand))
                return this.seqService.getNextSeqLongValue(sequeceName);
            else if (seqArea.equals(SeqArea.str))
                return this.seqService.getUUID();
            else
                return this.seqService.getNextSeqIntValue(sequeceName);
        }
    }
    
    /**
     * 保存一个对象。若主键为空,则由内部序列机制自动生成，可以确保跨库唯一。
     * @param bean
     * @author zhangyz created on 2013-6-7
     */
    @SuppressWarnings("unchecked")
    @Override
    public PK save(T bean) {
        if (bean.getId() == null){
            TableInfo tf = TableInfo.get(this.pojoClass);
            Object id = genAutoId();
            tf.getId().setValue(bean, id);
        }
        getDb().save(bean);
        return (PK)bean.getId();
    }
    
    public String getSequeceName() {
        return sequeceName;
    }
}
