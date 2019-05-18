package com.software5000.base;

/**
 * 更新操作时的更新策略枚举类型，这里只针对没有值的栏位
 *
 * 这里的<code>EMPTY</code>只会针对String类型的 <b>""</b> 值。
 *
 * @author matuobasyouca@gmail.com
 */
public enum ValueUpdatePolicy {
    /**
     * 不更新空值,不更新NULL
     */
    NOT_EMPTY_NOT_NULL,

    /**
     * 更新空值,不更新NULL
     */
    WITH_EMPTY_NOT_NULL,

    /**
     * 不更新空值,更新NULL
     */
    NOT_EMPTY_WITH_NULL,

    /**
     * 更新空值,更新NULL
     */
    WITH_EMPTY_WITH_NULL,
}
