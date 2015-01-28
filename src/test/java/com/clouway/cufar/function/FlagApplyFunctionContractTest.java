package com.clouway.cufar.function;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Stefan Dimitrov (stefan.dimitrov@clouway.com).
 */
public abstract class FlagApplyFunctionContractTest {

  protected class ChatRoom {
    private boolean commentsSeen;

    public boolean isCommentsSeen() {
      return commentsSeen;
    }

    public void setCommentsSeen(boolean commentsSeen) {
      this.commentsSeen = commentsSeen;
    }
  }

  protected ChatRoom chatRoom;
  protected FlagApplyFunction<ChatRoom> flagApplyFunction;

  @Before
  public void setUp() throws Exception {
    chatRoom = new ChatRoom();
  }

  @Test
  public void appliesSeenToInstanceProperty() throws Exception {
    chatRoom.setCommentsSeen(false);
    flagApplyFunction.apply(chatRoom, "Comments", true);

    assertThat(chatRoom.isCommentsSeen(), is(true));
  }

  @Test
  public void appliesNotSeenToInstanceProperty() throws Exception {
    chatRoom.setCommentsSeen(true);
    flagApplyFunction.apply(chatRoom, "Comments", false);

    assertThat(chatRoom.isCommentsSeen(), is(false));
  }

  @Test
  public void doesNotApplyStatusToNotExistingProperty() throws Exception {
    chatRoom.setCommentsSeen(false);
    flagApplyFunction.apply(chatRoom, "Stars", true);

    assertThat(chatRoom.isCommentsSeen(), is(false));
  }

}
