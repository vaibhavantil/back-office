package com.hedvig.backoffice;

import static com.spotify.docker.client.DockerClient.RemoveContainerParam.removeVolumes;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.HostConfig.Bind;
import com.spotify.docker.client.messages.PortBinding;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;

public class DockerService {

  private DockerClient dockerClient;
  private ContainerCreation container;
  private Map<String, List<PortBinding>> ports;
  private LogStream logs;

  DockerService(ContainerParameters params) {
    dockerClient = createDockerClient();

    ContainerConfig containerConfig =
      createContainerConfig(
        params.imageName,
        params.ports,
        params.cmd,
        params.envs,
        params.containerToLink,
        params.pathToConfig,
        params.pathToResource,
        params.resourceName);

    try {
      if (!params.isLocalImage) {
        dockerClient.pull(params.imageName);
      }
      container = dockerClient.createContainer(containerConfig, params.containerName);
    } catch (DockerException | InterruptedException e) {
      disposeContainer();
      throw new IllegalStateException(e);
    } catch (Throwable throwable) {
      disposeContainer();
      throw throwable;
    }
  }

  public static ContainerBuilder builder() {
    return new ContainerBuilder();
  }

  public void startContainer() throws Throwable {
    dockerClient.startContainer(container.id());
  }

  public void disposeContainer() {

    try {
      val stream = dockerClient.logs(container.id(), LogsParam.stdout(), LogsParam.stderr());
      System.out.println(stream.readFully());
    } catch (DockerException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String exception = "";

    try {
      if (container != null) {
        dockerClient.killContainer(container.id());
      }
    } catch (DockerException | InterruptedException e) {
      exception += "Unable to stop docker container " + container.id() + e;
    }

    try {
      if (container != null) {
        dockerClient.removeContainer(container.id(), removeVolumes());
      }
    } catch (DockerException | InterruptedException e) {
      exception += "Unable to remove docker container " + container.id() + e;
    }
    dockerClient.close();
    if (!exception.isEmpty()) {
      throw new RuntimeException(exception);
    }
  }

  public String getDockerHost() {
    return dockerClient.getHost();
  }

  public int getHostPort(String containerPort) {
    List<PortBinding> portBindings = ports.get(containerPort);
    if (portBindings.isEmpty()) {
      return -1;
    }
    return Integer.parseInt(portBindings.get(0).hostPort());
  }

  protected DockerClient createDockerClient() {
    try {
      return DefaultDockerClient.fromEnv().build();
    } catch (DockerCertificateException e) {
      throw new IllegalStateException("Could not create docker client from environement", e);
    }
  }

  protected ContainerConfig createContainerConfig(
    String imageName,
    Map<Integer, Integer> ports,
    String cmd,
    String[] envs,
    String containerToLink,
    String pathToConfig,
    String pathToResource,
    String resourceName) {
    Map<String, List<PortBinding>> portBindings = new HashMap<>();

    Set<String> exposedPorts =
      ports.values().stream().map(Object::toString).collect(Collectors.toSet());

    for (Map.Entry<Integer, Integer> port : ports.entrySet()) {
      List<PortBinding> hostPorts =
        Collections.singletonList(PortBinding.of("0.0.0.0", port.getKey().toString()));
      portBindings.put(port.getValue().toString(), hostPorts);
    }

    HostConfig hostConfig;

    if (containerToLink != null && pathToConfig != null && resourceName != null) {
      hostConfig =
        HostConfig.builder()
          .portBindings(portBindings)
          .links(containerToLink)
          .appendBinds(Bind.from(pathToConfig).to("/config").readOnly(true).build())
          .appendBinds(Bind.from(pathToResource).to(resourceName).build())
          .build();
    } else if (containerToLink != null && pathToConfig != null) {
      hostConfig =
        HostConfig.builder()
          .portBindings(portBindings)
          .links(containerToLink)
          .appendBinds(Bind.from(pathToConfig).to("/config").readOnly(true).build())
          .build();
    } else {
      hostConfig = HostConfig.builder().portBindings(portBindings).build();
    }

    ContainerConfig.Builder configBuilder =
      ContainerConfig.builder()
        .hostConfig(hostConfig)
        .image(imageName)
        .networkDisabled(false)
        .exposedPorts(exposedPorts)
        .attachStdout(true);

    if (cmd != null) {
      configBuilder = configBuilder.cmd(cmd);
    }
    if (envs != null) {
      configBuilder = configBuilder.env(envs);
    }

    return configBuilder.build();
  }
}
