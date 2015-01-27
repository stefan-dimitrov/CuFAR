package com.clouway.cufar;

import com.clouway.cufar.flag.ChangeFlag;
import com.clouway.cufar.function.FlagApplyFunction;
import com.clouway.cufar.storage.FlagBase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public class FlagServiceImpl implements FlagService {

  private FlagBase flagBase;

  public FlagServiceImpl(FlagBase flagBase) {

    this.flagBase = flagBase;
  }

  @Override
  public void addChange(ChangeFlag changeFlag, Date changedOn) {
    flagBase.storeOrUpdate(changeFlag, changedOn);
  }

  @Override
  public void seeChange(ChangeFlag changeFlag, String attender, Date seenOn) {

  }

  @Override
  public <T> void applyFlags(String attender, T object, FlagApplyFunction<T> flagApplyFunction, List<? extends ChangeFlag> changeFlags) {

    Map<String, Date> attenderSeenDates = flagBase.findSeenDatesByAttender(changeFlags, attender);

    for(ChangeFlag changeFlag: changeFlags) {
      String flagName = flagNameFromChangeFlag(object, changeFlag);

      boolean seenByAttender = wasFlagSeen(attenderSeenDates.get(changeFlag.getId()), changeFlag.getLastUpdateDate());

      flagApplyFunction.apply(object, flagName, seenByAttender);
    }

  }

  private <T> String flagNameFromChangeFlag(T object, ChangeFlag changeFlag) {
    String flagName;
    String objectClassName = object.getClass().getSimpleName();
    flagName = changeFlag.getClass().getSimpleName().substring(objectClassName.length());
    return flagName;
  }

  private boolean wasFlagSeen(Date attendanceDate, Date updateDate) {
    if (updateDate == null) {
      return true;
    }

    if (attendanceDate.before(updateDate)) {
      return false;
    }
    return true;
  }
}
