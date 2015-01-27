package com.clouway.cufar;

import com.clouway.cufar.flag.ChangeFlag;
import com.clouway.cufar.flag.ChangeFlagOf;
import com.clouway.cufar.function.FlagApplyFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public abstract class FlagServiceContractTest {

  class FakeTask {
    private Long id;
    private boolean commentsChangeSeen;
    private boolean scheduleChangeSeen;

    public Long getId() {
      return id;
    }

    public void setCommentsChangeSeen(boolean commentsChangeSeen) {
      this.commentsChangeSeen = commentsChangeSeen;
    }

    public boolean isCommentsChangeSeen() {
      return commentsChangeSeen;
    }

    public void setScheduleChangeSeen(boolean scheduleChangeSeen) {
      this.scheduleChangeSeen = scheduleChangeSeen;
    }

    public boolean isScheduleChangeSeen() {
      return scheduleChangeSeen;
    }
  }

  @ChangeFlagOf(FakeTask.class)
  class FakeTaskScheduleChange implements ChangeFlag<Long> {

    private Long taskId;

    public FakeTaskScheduleChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public Long getReferenceId() {
      return this.taskId;
    }
  }

  @ChangeFlagOf(FakeTask.class)
  class FakeTaskCommentsChange implements ChangeFlag<Long> {

    private Long taskId;

    FakeTaskCommentsChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public Long getReferenceId() {
      return taskId;
    }
  }

  private FlagService flagService;
  private FakeTask task;
  private final String attender = "fake_attender";

  private final Date january15 = newDate(2015, 1, 15);
  private final Date january20 = newDate(2015, 1, 20);
  private final Date january22 = newDate(2015, 1, 22);

  private FlagApplyFunction<FakeTask> fakeFlagApplyFunction = new FlagApplyFunction<FakeTask>() {
    @Override
    public void apply(FakeTask object, String flagName, Boolean seen) {
      if ("ScheduleChange".equals(flagName)) {
        object.setScheduleChangeSeen(seen);
      }

      else if ("CommentsChange".equals(flagName)) {
        object.setCommentsChangeSeen(seen);
      }
    }
  };


  protected abstract FlagService newFlagService();

  @Before
  public final void setUp() throws Exception {
    task = new FakeTask();
    flagService = newFlagService();
  }

  @Test
  public void neverMadeAnyChanges() throws Exception {

    ChangeFlag taskScheduleChange = new FakeTaskScheduleChange(task.getId());
    flagService.applyFlags(attender, task, fakeFlagApplyFunction, taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(true));
  }

  @Test
  public void neverSeenTheChanges() throws Exception {

    FakeTaskScheduleChange taskScheduleChange = new FakeTaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january15);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesNotSeen() throws Exception {
    FakeTaskScheduleChange taskScheduleChange = new FakeTaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january15);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesSeen() throws Exception {
    FakeTaskScheduleChange taskScheduleChange = new FakeTaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january22);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void multipleChanges() throws Exception {
    FakeTaskScheduleChange taskScheduleChange = new FakeTaskScheduleChange(task.getId());
    FakeTaskCommentsChange taskCommentsChange = new FakeTaskCommentsChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);
    flagService.addChange(taskCommentsChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january15);
    flagService.seeChange(taskCommentsChange, attender, january22);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, taskScheduleChange, taskCommentsChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
    assertThat(task.isCommentsChangeSeen(), is(true));
  }

  private Date newDate(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 1);

    return calendar.getTime();
  }
}
