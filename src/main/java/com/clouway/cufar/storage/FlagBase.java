package com.clouway.cufar.storage;

import com.clouway.cufar.flag.ChangeFlag;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public interface FlagBase {

  String storeOrUpdate(ChangeFlag<?> changeFlag, Date lastUpdateDate);

  Map<String,Date> findSeenDatesByAttender(List<? extends ChangeFlag> changeFlagList, String attender);

  Map<String, Date> findUpdateDates(List<? extends ChangeFlag> changeFlagList);
}
