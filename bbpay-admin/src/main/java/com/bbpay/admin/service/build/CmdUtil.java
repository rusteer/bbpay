package com.bbpay.admin.service.build;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdUtil {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CmdUtil.class);
    public static int exeCmd(String cmd) {
        LOGGER.info("start cmd:" + cmd);
        int result = 0;
        BufferedReader in = null;
        BufferedReader error = null;
        try {
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = in.readLine();
            while (line != null) {
                LOGGER.info(line);
                line = in.readLine();
            }
            while ((line = error.readLine()) != null) {
                System.err.println(line);
            }
            if (p.waitFor() != 0) {
                result = p.exitValue();
                if (result != 0) {
                    System.err.println("Error occurs while executing cmd {" + cmd + "} and the return value is " + result);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
            try {
                error.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
        }
        LOGGER.info("end cmd:" + cmd);
        return result;
    }
}
