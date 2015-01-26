package com.clouway.cufar.function;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public interface FlagApplyFunction<T> {
  void apply(T object, String flagName, Boolean seen);
}
