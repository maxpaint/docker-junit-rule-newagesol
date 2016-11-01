package com.newagesol.docker;

import com.spotify.docker.client.messages.ContainerConfig;

public class DockerRuleParams {

    String imageName;
    String[] ports;
    String cmd;

    String portToWaitOn;
    int waitTimeout;
    String logToWait;

    ContainerConfig config;
    String dockerHost;
    String dockerCertPath;

    public DockerRuleParams() {
    }

    public DockerRuleParams(String imageName, String[] ports, String cmd, String portToWaitOn, int waitTimeout, String logToWait, ContainerConfig config, String dockerHost, String dockerCertPath) {
        this.imageName = imageName;
        this.ports = ports;
        this.cmd = cmd;
        this.portToWaitOn = portToWaitOn;
        this.waitTimeout = waitTimeout;
        this.logToWait = logToWait;
        this.config = config;
        this.dockerHost = dockerHost;
        this.dockerCertPath = dockerCertPath;
    }


    public static final class DockerRuleParamsBuilder {
        String imageName;
        String[] ports;
        String cmd;
        String portToWaitOn;
        int waitTimeout;
        String logToWait;
        ContainerConfig config;
        String dockerHost;
        String dockerCertPath;

        private DockerRuleParamsBuilder() {
        }

        public static DockerRuleParamsBuilder getBuilder() {
            return new DockerRuleParamsBuilder();
        }

        public DockerRuleParamsBuilder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public DockerRuleParamsBuilder ports(String... ports) {
            this.ports = ports;
            return this;
        }

        public DockerRuleParamsBuilder cmd(String cmd) {
            this.cmd = cmd;
            return this;
        }

        public DockerRuleParamsBuilder portToWaitOn(String portToWaitOn) {
            this.portToWaitOn = portToWaitOn;
            return this;
        }

        public DockerRuleParamsBuilder waitTimeout(int waitTimeout) {
            this.waitTimeout = waitTimeout;
            return this;
        }

        public DockerRuleParamsBuilder logToWait(String logToWait) {
            this.logToWait = logToWait;
            return this;
        }

        public DockerRuleParamsBuilder config(ContainerConfig config) {
            this.config = config;
            return this;
        }

        public DockerRuleParamsBuilder dockerHost(String dockerHost) {
            this.dockerHost = dockerHost;
            return this;
        }

        public DockerRuleParamsBuilder dockerCertPath(String dockerCertPath) {
            this.dockerCertPath = dockerCertPath;
            return this;
        }

        public DockerRuleParams build() {
            DockerRuleParams dockerRuleParams = new DockerRuleParams(imageName, ports, cmd, portToWaitOn, waitTimeout, logToWait, config, dockerHost, dockerCertPath);
            return dockerRuleParams;
        }
    }
}
