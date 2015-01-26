package com.clouway.cufar.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public class DefaultFlagApplyFunction<T> implements FlagApplyFunction<T> {
  @Override
  public void apply(T object, String flagName, Boolean seen) {
    String setterName = String.format("set%sSeen", flagName);
    try {
      Method setterMethod = object.getClass().getMethod(setterName, Boolean.class);
      setterMethod.invoke(object, seen);

    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

  }
}
