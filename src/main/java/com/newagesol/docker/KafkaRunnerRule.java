package com.newagesol.docker;

import com.spotify.docker.client.messages.ContainerConfig;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;

public class KafkaRunnerRule extends ExternalResource {

    private DockerRuleParams zooKeeperRuleConfig;
    private DockerRuleParams kafkaRuleConfig;

    private DockerRule zooKeeperRule;
    private DockerRule kafkaRule;

    public KafkaRunnerRule(DockerRuleParams zooKeeperRuleConfig, DockerRuleParams kafkaRuleConfig) {
        this.zooKeeperRuleConfig = zooKeeperRuleConfig;
        this.kafkaRuleConfig = kafkaRuleConfig;
    }

    private DockerRule buildZooKeeper(DockerRuleParams zooKeeperRuleConfig) {
        return DockerRule.builder()
                .image(zooKeeperRuleConfig.imageName)
                .ports(zooKeeperRuleConfig.ports)
                .waitForLog(zooKeeperRuleConfig.logToWait)
                .dockerHost(zooKeeperRuleConfig.dockerHost)
                .dockerCertPath(zooKeeperRuleConfig.dockerCertPath)
                .config(zooKeeperRuleConfig.config)
                .build();
    }

    private DockerRule buildKafka(DockerRuleParams kafkaRuleConfig) {
        return DockerRule.builder()
                .image(kafkaRuleConfig.imageName)
                .ports(kafkaRuleConfig.ports)
                .waitForLog(kafkaRuleConfig.logToWait)
                .dockerHost(kafkaRuleConfig.dockerHost)
                .dockerCertPath(kafkaRuleConfig.dockerCertPath)
                .config(kafkaRuleConfig.config)
                .build();
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        zooKeeperRule = buildZooKeeper(zooKeeperRuleConfig);
        zooKeeperRule.before();
        while (zooKeeperRule.getHostPort(String.format("%s/tcp", zooKeeperRuleConfig.ports[0])) < 0) {
            Thread.sleep(2000);
        }

        Integer kafkaPort = zooKeeperRule.getHostPort(String.format("%s/tcp", zooKeeperRuleConfig.ports[0]));
        kafkaRuleConfig.config = modifyPortForKafkainConfig(kafkaRuleConfig.config, kafkaPort);

        kafkaRule = buildKafka(kafkaRuleConfig);
        kafkaRule.before();
    }

    private ContainerConfig modifyPortForKafkainConfig(ContainerConfig config, Integer kafkaPort) {
        return ContainerConfig.builder()
                .hostname(config.hostname())
                .domainname(config.domainname())
                .user(config.user())
                .attachStdin(config.attachStdin())
                .attachStdout(config.attachStdout())
                .attachStderr(config.attachStderr())
                .portSpecs(config.portSpecs())
                .exposedPorts(config.exposedPorts())
                .tty(config.tty())
                .openStdin(config.openStdin())
                .stdinOnce(config.stdinOnce())
                .env(modifyPortForKafka(config.env(), kafkaPort))// replace env
                .cmd(config.cmd())
                .image(config.image())
                .volumes(config.volumes())
                .workingDir(config.workingDir())
                .entrypoint(config.entrypoint())
                .networkDisabled(config.networkDisabled())
                .onBuild(config.onBuild())
                .labels(config.labels())
                .macAddress(config.macAddress())
                .hostConfig(config.hostConfig()).build();

    }

    private List<String> modifyPortForKafka(List<String> env, Integer kafkaPort) {
        List<String> newEnv = new ArrayList<>();
        env.stream().forEach(s -> {
            if(s.contains("KAFKA_ZOOKEEPER_CONNECT")){
                s = String.format("%s:%s", s.split(":")[0], kafkaPort);
            }
            newEnv.add(s);
        });

        return newEnv;
    }

    @Override
    protected void after() {
        super.after();
        kafkaRule.after();
        zooKeeperRule.after();
    }
}
