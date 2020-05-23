package cn.yiidii.pigeon.agent.netty.apptask;

import cn.yiidii.pigeon.agent.netty.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动Agent
 */
@Slf4j
@Component
@Order(value = 1)
public class launchAgentClient implements ApplicationRunner {

    @Autowired
    private Client client;

    @Override
    public void run(ApplicationArguments args) {
        try {
            client.launchClient();
            log.info("Agent start success.");
        } catch (Exception e) {
            log.error("Agent start failed. e:{}", e.toString());
        }
    }
}
