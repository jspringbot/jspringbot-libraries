package org.jspringbot.keyword.selenium;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class ScreenshotCompareHelper implements InitializingBean {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ScreenshotCompareHelper.class);

    protected WebDriver driver;

    protected File baseImageDir;

    protected File baseOutputImageDir;

    protected File outputImageDir;

    protected File collectImageDir;

    protected File resultImageDir;

    protected File screenshotImageDir;

    protected String environmentPath;

    protected Double threshold = 0.0001;

    protected File icompareFile;

    public ScreenshotCompareHelper(WebDriver driver) {
        this.driver = driver;
    }

    @Required
    public void setCollectImageDir(File collectImageDir) {
        this.collectImageDir = collectImageDir;
    }

    @Required
    public void setIcompareFile(File icompareFile) {
        this.icompareFile = icompareFile;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    @Required
    public void setBaseImageDir(File baseImageDir) {
        this.baseImageDir = baseImageDir;
    }

    @Required
    public void setOutputImageDir(File outputImageDir) {
        this.outputImageDir = outputImageDir;
    }

    @Required
    public void setEnvironmentPath(String path) {
        environmentPath = path;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!outputImageDir.isDirectory()) {
            if(!outputImageDir.mkdirs()) {
                throw new IllegalStateException("unable to create directory " + outputImageDir.getAbsolutePath());
            }
        }
        if(!collectImageDir.isDirectory()) {
            if(!collectImageDir.mkdirs()) {
                throw new IllegalStateException("unable to create directory " + collectImageDir.getAbsolutePath());
            }
        }

        baseOutputImageDir = new File(outputImageDir, "base");
        resultImageDir = new File(outputImageDir, "result");
        screenshotImageDir = new File(outputImageDir, "screenshot");

        if(!baseOutputImageDir.isDirectory()) {
            baseOutputImageDir.mkdirs();
        }
        if(!resultImageDir.isDirectory()) {
            resultImageDir.mkdirs();
        }
        if(!screenshotImageDir.isDirectory()) {
            screenshotImageDir.mkdirs();
        }
    }

    public void compare(String filename) throws IOException, InterruptedException {
        compare(filename, threshold);
    }

    public void collect(String filename) throws IOException {
        createScreenShotFile(collectImageDir, filename);
    }

    public void compare(String filename, double threshold) throws IOException, InterruptedException {

        File baselineFile = new File(baseImageDir, filename);
        File screenShotFile = createScreenShotFile(screenshotImageDir, filename);
        File resultFile = new File(resultImageDir, filename);

        if(!baselineFile.isFile()) {
            throw new IllegalArgumentException(String.format("Filename '%s' not found in directory %s.", filename, baseImageDir.getAbsolutePath()));
        }

        DecimalFormat formatter = new DecimalFormat("#.############");

        List<String> arguments = Arrays.asList(
                icompareFile.getAbsolutePath(),
                baselineFile.getAbsolutePath(),
                screenShotFile.getAbsolutePath(),
                resultFile.getAbsolutePath(),
                formatter.format(threshold)
        );

        ProcessBuilder builder = new ProcessBuilder("node");
        builder.command().addAll(arguments);

        LOG.info("Arguments: " + arguments);

        if(StringUtils.isNotBlank(environmentPath)) {
            builder.environment().put("PATH", environmentPath);
        }

        builder.directory(icompareFile.getParentFile());

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
                buf.append("| ").append(line).append("\n");
            }
            while((line = errReader.readLine()) != null) {
                buf.append("| ").append(line).append("\n");
            }

            int exitValue = process.waitFor();

            if(exitValue != 0) {
                LOG.warn("Console: \n" + buf);
                throw new IllegalStateException("Script executed with failure. Exit Code: " + exitValue);
            }
        } finally {
            IOUtils.closeQuietly(errReader);
            IOUtils.closeQuietly(reader);
        }

        String resultOutput = buf.toString();

        LOG.info("Console: \n" + buf);

        if(!resultOutput.contains("PASSED!")) {
            IOUtils.copy(new FileInputStream(baselineFile), new FileOutputStream(new File(baseOutputImageDir, filename)));

            LOG.html("Comparison: <br/> <table><tr><td><b>Baseline:</b></td><td><b>Screenshot:</b></td></tr><tr><td><img src='base/%s'/></td><td><img src='screenshot/%s'/></td></tr><tr><td colspan='2'><b>Difference:</b></td></tr><tr><td colspan='2'><img src='result/%s'/></td></tr></table>", filename, filename, filename);
            throw new IllegalStateException("Image Compare did not pass.");
        }
    }

    private File createScreenShotFile(File dir, String filename) throws IOException {
        File outputFile;
        FileOutputStream out = null;

        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            outputFile = new File(dir, filename);

            LOG.info("Screenshot File: " + outputFile.getAbsolutePath());

            out = new FileOutputStream(outputFile);
            IOUtils.write(bytes, out);
        } finally {
            IOUtils.closeQuietly(out);
        }

        return outputFile;
    }

}
