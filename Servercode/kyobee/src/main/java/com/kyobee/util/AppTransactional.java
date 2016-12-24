package com.kyobee.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

import com.kyobee.exception.RsntException;

/**
 * This new annotation is same as spring @Transactional annotation along with
 * one extra capability, that the transaction will rollback whenever we throw
 * ApplicationException from inner class.
 * 
 * @author rohit
 *
 */
@Transactional(rollbackFor = RsntException.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
public @interface AppTransactional {

}
