package com.clouway.cufar;

public class FlagServiceImplTest extends FlagServiceContractTest {

  @Override
  protected FlagService newFlagService() {
    return new FlagServiceImpl();
  }
}