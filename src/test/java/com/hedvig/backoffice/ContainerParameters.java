package com.hedvig.backoffice;

import java.util.HashMap;

public class ContainerParameters {

  String containerName;
  String containerToLink;
  String imageName;
  HashMap<Integer, Integer> ports;
  String cmd;
  String[] envs;
  boolean isLocalImage;
  String portToWaitOn;
  String logToWait;
  int waitTimeout;
  String volume;
  String pathToConfig;
  String pathToResource;
  String resourceName;
}
