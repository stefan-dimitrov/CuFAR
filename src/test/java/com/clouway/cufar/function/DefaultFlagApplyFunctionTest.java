package com.clouway.cufar.function;


public class DefaultFlagApplyFunctionTest extends FlagApplyFunctionContractTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    flagApplyFunction = new DefaultFlagApplyFunction<ChatRoom>();
  }
}