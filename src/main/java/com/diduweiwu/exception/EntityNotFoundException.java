package com.diduweiwu.exception;


/**
 * @author diduweiwu
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("对象未找到");
    }
}
