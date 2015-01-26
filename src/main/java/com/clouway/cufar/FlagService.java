package com.clouway.cufar;

import java.util.Date;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public interface FlagService {
  void addChange(ChangeFlag changeFlag, Date changedOn);
  void seeChange(ChangeFlag changeFlag, String attender, Date seenOn);

  <T> void applyFlags(String attender, T object, FlagApplyFunction<?> flagApplyFunction, ChangeFlag<?> ...changeFlags);
}
