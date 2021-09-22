/*
 * Copyright (c) 2021 , Inc. All Rights Reserved.
 */

package xyz.xiamang.holiday.exception;

/**
 *
 * @author ysq
 */
public class UnSupportException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * @param message 异常说明
     */
    public UnSupportException(String message) {
        super(message);
    }

}
