package com.hedvig.backoffice;

import java.util.HashMap;

public class ContainerBuilder {

  private final ContainerParameters params = new ContainerParameters();

  public ContainerBuilder name(String containerName) {
    params.containerName = containerName;
    return this;
  }

  public ContainerBuilder image(String imageName) {
    params.imageName = imageName;
    return this;
  }

  public ContainerBuilder ports(HashMap<Integer, Integer> ports) {
    params.ports = ports;
    return this;
  }

  public ContainerBuilder cmd(String cmd) {
    params.cmd = cmd;
    return this;
  }

  public ContainerBuilder env(String[] envs) {
    params.envs = envs;
    return this;
  }

  public ContainerBuilder link(String name) {
    params.containerToLink = name;
    return this;
  }

  public ContainerBuilder volume(String name) {
    params.volume = name;
    return this;
  }

  public ContainerBuilder waitForPort(String portToWaitOn) {
    return waitForPort(portToWaitOn, 10000);
  }

  public ContainerBuilder waitForPort(String portToWaitOn, int timeoutInMillis) {
    params.portToWaitOn = portToWaitOn;
    params.waitTimeout = timeoutInMillis;
    return this;
  }

  public ContainerBuilder waitForLog(String logToWait) {
    params.logToWait = logToWait;
    return this;
  }

  public ContainerBuilder config(String path) {
    params.pathToConfig = path;
    return this;
  }

  public ContainerBuilder resource(String pathToResource, String targetName) {
    params.pathToResource = pathToResource;
    params.resourceName = targetName;
    return this;
  }

  public DockerService build() {
    return new DockerService(params);
  }
}
