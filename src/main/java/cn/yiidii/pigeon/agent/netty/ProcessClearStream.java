package cn.yiidii.pigeon.agent.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 线程处理process的流
 */
@Data
@AllArgsConstructor
@Slf4j
public class ProcessClearStream implements Runnable {

    private InputStream inputStream;
    private List<String> output;
    private String type;
    private boolean isFinish;

    public ProcessClearStream(InputStream inputStream, List<String> output, String type) {
        this.inputStream = inputStream;
        this.output = output;
        this.type = type;
        this.isFinish = false;
    }

    @Override
    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                output.add(line.trim());
            }
            isFinish = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            isFinish = false;
        }
        isFinish = false;
    }
}
