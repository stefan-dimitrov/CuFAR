package com.clouway.cufar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChangeFlagOf {
  Class value();
}
