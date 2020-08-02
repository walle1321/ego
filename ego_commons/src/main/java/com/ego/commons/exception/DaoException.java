package com.ego.commons.exception;

/**
 * 数据库删改查异常，要继承RuntimeException
 */
public class DaoException extends RuntimeException{
    public DaoException(String message) {
        super(message);
    }
}
