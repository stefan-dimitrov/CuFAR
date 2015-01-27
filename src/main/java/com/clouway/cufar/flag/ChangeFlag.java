package com.clouway.cufar.flag;

import java.util.Date;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public interface ChangeFlag<T> {
  String getId();
  T getReferenceId();
  Date getLastUpdateDate();
}
