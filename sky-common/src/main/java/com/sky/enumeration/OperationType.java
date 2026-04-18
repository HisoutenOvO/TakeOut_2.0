package com.sky.enumeration;

/**
 * 数据库操作类型，用于配合AOP使用-辅助自动填充
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT

}
