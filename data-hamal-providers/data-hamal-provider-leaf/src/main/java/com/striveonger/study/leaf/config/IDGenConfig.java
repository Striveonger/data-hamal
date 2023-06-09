package com.striveonger.study.leaf.config;

import com.striveonger.study.leaf.constants.PropertiesConstants;
import com.striveonger.study.leaf.core.IDGen;
import com.striveonger.study.leaf.service.ILeafAllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-27 13:06
 */
@Configuration
public class IDGenConfig {

    private final Logger log = LoggerFactory.getLogger(IDGenConfig.class);

    @Resource
    private ILeafAllocService service;

    @Value(PropertiesConstants.LEAF_NODE_NAME)
    private String nodeName;

    @Value(PropertiesConstants.LEAF_ZOOKEEPER_ADDRESS)
    private String zookeeperAddress;

    @Value(PropertiesConstants.LEAF_PORT)
    private Integer port;

    @Bean(name = "segmentGen")
    public IDGen segmentGen() {
        return IDGen.builder().type("segment").service(service).build();
    }

    @Primary
    @Bean(name = "snowflakeGen")
    public IDGen snowflakeGen() {
        return IDGen.builder()
                    .type("snowflake")
                    .nodeName(nodeName)
                    .zookeeperAddress(zookeeperAddress)
                    .zookeeperPort(port)
                    .build();
    }

}