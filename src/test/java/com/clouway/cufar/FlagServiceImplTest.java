package com.clouway.cufar;

import com.clouway.cufar.storage.FlagBase;
import com.google.common.collect.Lists;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class FlagServiceImplTest extends FlagServiceContractTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock private FlagBase flagBase;
  protected Map<String, Date> fakeAttenderSeenDates;

  @Override
  protected FlagService newFlagService() {
    return new FlagServiceImpl(flagBase);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();

    fakeAttenderSeenDates = new Hashtable<String, Date>();
  }

  @Override
  public void neverMadeAnyChanges() throws Exception {

    context.checking(new Expectations() {{
      oneOf(flagBase).findSeenDatesByAttender(with(equal(Lists.newArrayList(taskScheduleChange))), with(equal(attender)));
      will(returnValue(fakeAttenderSeenDates));
    }});

    super.neverMadeAnyChanges();
  }

  @Override
  public void neverSeenTheChanges() throws Exception {

    fakeAttenderSeenDates.put("fakeScheduleChange", new Date(0l));

    context.checking(new Expectations() {{
      oneOf(flagBase).storeOrUpdate(taskScheduleChange, dateOfUpdate);

      oneOf(flagBase).findSeenDatesByAttender(with(equal(Lists.newArrayList(taskScheduleChange))), with(equal(attender)));
      will(returnValue(fakeAttenderSeenDates));
    }});

    super.neverSeenTheChanges();
  }
}