package cn.yiidii.pigeon.agent.netty;


import cn.yiidii.pigeon.agent.netty.dto.AgentRequest;
import cn.yiidii.pigeon.agent.netty.dto.AgentResponse;
import cn.yiidii.pigeon.agent.netty.handler.AgentHandler;
import cn.yiidii.pigeon.agent.netty.util.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Slf4j
public class CMDExecutor {

    /**
     * 下发脚本
     */
    public static void issuedScript(AgentResponse response) {
        String id = response.getId();
        String fileName = response.getFileName();
        byte[] attachment = response.getAttachment();
        String filePath = System.getProperty("user.dir") + File.separator
                + AgentConstant.SCRIPT_DIR + File.separator + response.getFileName();
        try {
            if (StringUtils.isNotBlank(fileName) && !Objects.isNull(attachment) && attachment.length > 0) {
                FileUtils.writeByteArrayToFile(new File(filePath), GzipUtil.ungzip(attachment));
            }
        } catch (Exception e) {
            log.error("Exception occured when issuedScript.e: {}", e.toString());
            log.error("{}", e);
        }
        List<String> list = new ArrayList<>();
        list.add("success invoke method issuedScript");
        AgentRequest request = AgentRequest.success(list);
        request.setId(id);
        AgentHandler.getAgentClientCTX().writeAndFlush(request);
    }

    /**
     * 执行Windows的vbs脚本
     */
    public static void doWinScript(AgentResponse response) {
        String id = response.getId();
        // 拼接参数
        String filename = response.getFileName();
        String[] invokeParam = response.getInvokeParam();
        String[] command = new String[]{"cscript", "//nologo", "//T:120", System.getProperty("user.dir") + File.separator + AgentConstant.SCRIPT_DIR + File.separator + filename};
        command = ArrayUtils.addAll(command, invokeParam);

        //执行命令
        List<String> stdOutput = new ArrayList<>();
        List<String> errOutput = new ArrayList<>();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            ProcessClearStream stdProcessStream = new ProcessClearStream(process.getInputStream(), stdOutput, "STD");
            ProcessClearStream errProcessStream = new ProcessClearStream(process.getErrorStream(), errOutput, "ERROR");
            stdProcessStream.run();
            errProcessStream.run();
            int exitCode = process.waitFor();

            //给两个线程2s时间处理
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (stdProcessStream.isFinish() && errProcessStream.isFinish()) {
                    break;
                }
            }

            //返回
            AgentRequest request = new AgentRequest();
            if (exitCode != 0) {
                request = AgentRequest.error();
            }
            if (errOutput.size() > 0) {
                request = AgentRequest.error();
            } else if (stdOutput.size() > 0) {
                request = AgentRequest.success(stdOutput);
            }
            request.setId(id);
            AgentHandler.getAgentClientCTX().writeAndFlush(request);
        } catch (Exception e) {
            log.error("doWinScript: {}", e);
            AgentHandler.getAgentClientCTX().writeAndFlush(AgentRequest.error());
        } finally {
            //释放process资源
            freeProcessResource(process);
        }
    }

    /**
     * 执行Lunix的ssh脚本
     */
    public static void doUnixScript(AgentResponse response) {

    }

    private static void freeProcessResource(Process p) {
        if (Objects.isNull(p)) {
            return;
        }
        InputStream error = p.getErrorStream();
        InputStream in = p.getInputStream();
        OutputStream out = p.getOutputStream();
        try {
            if (error != null) {
                error.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            log.info("Exception occured when freeProcessResource: {}", e.toString());
        }
    }
}
