package com.newagesol.test;


import com.newagesol.docker.DockerRuleParams;
import com.newagesol.docker.KafkaRunnerRule;
import com.spotify.docker.client.messages.ContainerConfig;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DockerTestExample {

    public static ContainerConfig zooKeeperConfg = ContainerConfig.builder()
            .exposedPorts("")//2181
            .build();

    public static ContainerConfig kafkaConfg = ContainerConfig.builder()
            .env("KAFKA_ADVERTISED_HOST_NAME=" + getLocalHost(),
                    "KAFKA_ZOOKEEPER_CONNECT=",//add host
                    "KAFKA_CREATE_TOPICS=signup:1:1,firstDeposit:1:1,bonusTriggers:1:1")
            .build();


    private static DockerRuleParams zooKeeperConfig = DockerRuleParams.DockerRuleParamsBuilder.getBuilder()
            .imageName("wurstmeister/zookeeper:3.4.6")
            .ports("")//2181
            .logToWait("binding to port")
            .dockerHost("")//tcp://host:port
            .dockerCertPath("")//C:\Users\bla-bla-bla
            .config(zooKeeperConfg)
            .build();


    private static DockerRuleParams kafkaConfig = DockerRuleParams.DockerRuleParamsBuilder.getBuilder()
            .imageName("wurstmeister/kafka:0.10.0.1")
            .ports("")//9092
            .logToWait("")// kafka complite log
            .dockerHost("")//tcp://host:port
            .dockerCertPath("")//C:\Users\bla-bla-bla
            .config(kafkaConfg)
            .build();

    @ClassRule
    public static KafkaRunnerRule kafkaRunnerRule = new KafkaRunnerRule(zooKeeperConfig, kafkaConfig);

    @Test
    public void test() {
        System.out.println("test");
    }

    private static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "127.0.0.1";
    }
}
