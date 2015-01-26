package com.clouway.cufar;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public interface FlagApplyFunction<T> {
  void apply(T object, String flagName, Boolean seen);
}
