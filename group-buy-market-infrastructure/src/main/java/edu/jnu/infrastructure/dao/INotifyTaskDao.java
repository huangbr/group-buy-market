package edu.jnu.infrastructure.dao;


import edu.jnu.infrastructure.dao.po.NotifyTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回调任务Dao
 */
@Mapper
public interface INotifyTaskDao {
    // 插入回调任务信息
    public void insert(NotifyTask notifyTask);


}
