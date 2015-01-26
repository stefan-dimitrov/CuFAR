package com.clouway.cufar;

import com.clouway.cufar.flag.ChangeFlag;
import com.clouway.cufar.flag.ChangeFlagOf;
import com.clouway.cufar.function.DefaultFlagApplyFunction;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public class FlagServiceContractTest {

  class Task {
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

  @ChangeFlagOf(Task.class)
  class TaskScheduleChange implements ChangeFlag<Long> {

    private Long taskId;

    public TaskScheduleChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public Long getReferenceId() {
      return this.taskId;
    }
  }

  @ChangeFlagOf(Task.class)
  class TaskCommentsChange implements ChangeFlag<Long> {

    private Long taskId;

    TaskCommentsChange(Long taskId) {
      this.taskId = taskId;
    }

    @Override
    public Long getReferenceId() {
      return taskId;
    }
  }

  private FlagService flagService;
  private Task task;
  private String attender = "fake_attender";

  private Date january12;
  private Date january15;
  private Date january19;
  private Date january20;
  private Date january21;
  private Date january22;

  @Test
  public void neverMadeAnyChanges() throws Exception {

    ChangeFlag taskScheduleChange = new TaskScheduleChange(task.getId());
    flagService.applyFlags(attender, task, new DefaultFlagApplyFunction<Task>(), taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(true));
  }

  @Test
  public void neverSeenTheChanges() throws Exception {

    TaskScheduleChange taskScheduleChange = new TaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january12);

    flagService.applyFlags(attender, task, new DefaultFlagApplyFunction<Task>(), taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesNotSeen() throws Exception {
    TaskScheduleChange taskScheduleChange = new TaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january15);

    flagService.applyFlags(attender, task, new DefaultFlagApplyFunction<Task>(), taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void changesSeen() throws Exception {
    TaskScheduleChange taskScheduleChange = new TaskScheduleChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january22);

    flagService.applyFlags(attender, task, new DefaultFlagApplyFunction<Task>(), taskScheduleChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
  }

  @Test
  public void multipleChanges() throws Exception {
    TaskScheduleChange taskScheduleChange = new TaskScheduleChange(task.getId());
    TaskCommentsChange taskCommentsChange = new TaskCommentsChange(task.getId());

    flagService.addChange(taskScheduleChange, january20);
    flagService.addChange(taskCommentsChange, january20);

    flagService.seeChange(taskScheduleChange, attender, january19);
    flagService.seeChange(taskCommentsChange, attender, january21);

    flagService.applyFlags(attender, task, new DefaultFlagApplyFunction<Task>(), taskScheduleChange, taskCommentsChange);

    assertThat(task.isScheduleChangeSeen(), is(false));
    assertThat(task.isCommentsChangeSeen(), is(true));
  }
}
