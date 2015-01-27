package com.clouway.cufar;

import com.clouway.cufar.flag.ChangeFlag;
import com.clouway.cufar.function.FlagApplyFunction;

import java.util.Date;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public class FlagServiceImpl implements FlagService {

  @Override
  public void addChange(ChangeFlag changeFlag, Date changedOn) {
  }

  @Override
  public void seeChange(ChangeFlag changeFlag, String attender, Date seenOn) {

  }

  @Override
  public <T> void applyFlags(String attender, T object, FlagApplyFunction<T> flagApplyFunction, ChangeFlag<?>... changeFlags) {
    String flagName;
    Boolean seenByAttender = true;

    for(ChangeFlag changeFlag: changeFlags) {
      flagName = flagNameFromChangeFlag(object, changeFlag);

      flagApplyFunction.apply(object, flagName, seenByAttender);
    }

  }

  private <T> String flagNameFromChangeFlag(T object, ChangeFlag changeFlag) {
    String flagName;
    String objectClassName = object.getClass().getSimpleName();
    flagName = changeFlag.getClass().getSimpleName().substring(objectClassName.length());
    return flagName;
  }
}
