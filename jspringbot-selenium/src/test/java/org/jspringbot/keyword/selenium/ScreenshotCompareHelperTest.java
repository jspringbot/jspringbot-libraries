package org.jspringbot.keyword.selenium;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;

@Ignore
public class ScreenshotCompareHelperTest {

    @Test
    public void testRun() throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("node");

        File cwd = new File("/BADZ/WORK/SCREENSHOT COMPARISON/icompare_v0.1/");

        if(!cwd.isDirectory()) {
            throw new IllegalStateException("Invalid current working directory.");
        }

        File newFile = new File("C:\\cygwin64\\home\\sbuitizon\\workspace\\screenshot-comparison-research\\target\\test-classes\\imgbaseline\\local-firefox\\jspringbot-home.png");

        if(!newFile.isFile()) {
            throw new IOException("File not found.");
        }

        builder.command().addAll(Arrays.asList(
                "C:\\BADZ\\WORK\\SCREENSHOT COMPARISON\\icompare_v0.1\\icompare.js",
                "C:\\cygwin64\\home\\sbuitizon\\workspace\\screenshot-comparison-research\\target\\test-classes\\imgbaseline\\local-firefox\\jspringbot-home.png",
                "C:\\cygwin64\\home\\sbuitizon\\workspace\\screenshot-comparison-research\\target\\robotframework-reports\\screenshot\\jspringbot-home.png",
                "C:\\cygwin64\\home\\sbuitizon\\workspace\\screenshot-comparison-research\\target\\robotframework-reports\\result\\jspringbot-home.png",
                "0.0001"
        ));

        builder.environment().put("PATH", "C:\\Program Files\\graphicsmagick-1.3.19-q16;C:\\Program Files\\nodejs");
        builder.directory();

        Process process = null;
        BufferedReader reader = null;
        BufferedReader errReader = null;
        StringBuilder buf = new StringBuilder();
        try {
            process = builder.start();

            errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                System.out.println("| " + line);
            }
            while((line = errReader.readLine()) != null) {
                System.err.println("| " + line);
            }

            int exitValue = process.waitFor();

            if(exitValue != 0) {
                throw new IllegalStateException("Script executed with failure. Exit Code: " + exitValue + "\n" + buf);
            }
        } finally {
            IOUtils.closeQuietly(errReader);
            IOUtils.closeQuietly(reader);
        }

    }

    @Test
    public void testName() throws Exception {
        System.out.println(new DecimalFormat("#.##########").format(0.0001));

    }
}
