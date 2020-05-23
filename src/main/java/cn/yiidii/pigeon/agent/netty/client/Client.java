package cn.yiidii.pigeon.agent.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Agent 客户端
 */
@Component
@Data
@Slf4j
public class Client {

    @Value("${pigeon.agent.serverHost}")
    private String host;
    @Value("${pigeon.agent.port}")
    private int port;

    public void launchClient() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());

            // 连接服务端
            Channel ch = b.connect(host, port).sync().channel();

        } finally {
            //group.shutdownGracefully();
        }
    }
}
