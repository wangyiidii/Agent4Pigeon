package cn.yiidii.pigeon.agent.netty.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端的请求，其实是客户端向Server返回的采集数据.
 * 需序列化并与Server的包结构一致，否则会序列化异常
 */
@Data
public class AgentRequest implements Serializable {

    private static final long serialVersionUID = 1663173194410665331L;
    private String id;
    private boolean success;
    private List<String> output;

    public static AgentRequest success(List<String> output) {
        AgentRequest request = new AgentRequest();
        request.success = true;
        request.setOutput(output);
        return request;
    }

    public static AgentRequest error() {
        AgentRequest request = new AgentRequest();
        request.success = false;
        List<String> err = new ArrayList<>();
        err.add("errDesc = 采集失败");
        request.setOutput(err);
        return request;
    }

    public static AgentRequest error(List<String> err) {
        AgentRequest request = new AgentRequest();
        request.success = false;
        request.setOutput(err);
        return request;
    }

}
