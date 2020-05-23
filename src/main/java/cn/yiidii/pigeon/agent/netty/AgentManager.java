package cn.yiidii.pigeon.agent.netty;

import cn.yiidii.pigeon.agent.netty.dto.AgentResponse;
import cn.yiidii.pigeon.agent.netty.handler.AgentHandler;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class AgentManager {

    /**
     * 根据Server的cmdType分发不同的任务
     */
    public static void dispatchTask(AgentResponse response) {
        String cmdType = response.getCmdType();
        switch (cmdType) {
            case CMDType.ISSUED_FILE:
                CMDExecutor.issuedScript(response);
                break;
            case CMDType.DO_WIN_SCRIPT:
                CMDExecutor.doWinScript(response);
                break;
            case CMDType.DO_UNIX_SCRIPT:
                CMDExecutor.doUnixScript(response);
                break;
            default:
                log.warn("{} received a invalid cmdType: {}", AgentHandler.getHost(), response.getCmdType());
                break;
        }
    }

}
