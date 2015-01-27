package com.clouway.cufar;

import com.clouway.cufar.flag.ChangeFlag;
import com.clouway.cufar.flag.ChangeFlagOf;
import com.clouway.cufar.function.FlagApplyFunction;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public abstract class FlagServiceContractTest {

  protected FakeTaskScheduleChange taskScheduleChange;
  protected FakeTaskCommentsChange taskCommentsChange;

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

    private String id = "fakeScheduleChange";
    private Long taskId;

    public FakeTaskScheduleChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public Long getReferenceId() {
      return this.taskId;
    }

  }

  @ChangeFlagOf(FakeTask.class)
  class FakeTaskCommentsChange implements ChangeFlag<Long> {

    private String id = "fakeCommentsChange";
    private Long taskId;

    FakeTaskCommentsChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public Long getReferenceId() {
      return taskId;
    }

  }

  protected FlagService flagService;
  protected FakeTask task;
  protected final String attender = "fake_attender";

  protected final Date dateBeforeUpdate = new Date(123456155555l);
  protected final Date dateOfUpdate = new Date(123456177777l);
  protected final Date dateAfterUpdate = new Date(123456199999l);

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
  public void setUp() throws Exception {
    task = new FakeTask();
    flagService = newFlagService();

    taskScheduleChange = new FakeTaskScheduleChange(task.getId());
    taskCommentsChange = new FakeTaskCommentsChange(task.getId());
  }

  @Test
  public void neverMadeAnyChanges() throws Exception {

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, Lists.newArrayList(taskScheduleChange));

    assertThat(task.isScheduleChangeSeen(), is(true));
  }

  @Test
  public void neverSeenTheChanges() throws Exception {

    flagService.addChange(taskScheduleChange, dateOfUpdate);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, Lists.newArrayList(taskScheduleChange));

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesNotSeen() throws Exception {

    flagService.addChange(taskScheduleChange, dateOfUpdate);

    flagService.seeChange(taskScheduleChange, attender, dateBeforeUpdate);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, Lists.newArrayList(taskScheduleChange));

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesSeen() throws Exception {

    flagService.addChange(taskScheduleChange, dateOfUpdate);

    flagService.seeChange(taskScheduleChange, attender, dateAfterUpdate);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, Lists.newArrayList(taskScheduleChange));

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void multipleChanges() throws Exception {

    flagService.addChange(taskScheduleChange, dateOfUpdate);
    flagService.addChange(taskCommentsChange, dateOfUpdate);

    flagService.seeChange(taskScheduleChange, attender, dateBeforeUpdate);
    flagService.seeChange(taskCommentsChange, attender, dateAfterUpdate);

    flagService.applyFlags(attender, task, fakeFlagApplyFunction, Lists.newArrayList(taskScheduleChange, taskCommentsChange));

    assertThat(task.isScheduleChangeSeen(), is(false));
    assertThat(task.isCommentsChangeSeen(), is(true));
  }
}
