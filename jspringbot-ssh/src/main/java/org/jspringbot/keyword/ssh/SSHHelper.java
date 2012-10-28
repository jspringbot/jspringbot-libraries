/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.ssh;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jspringbot.JSpringBotLogger;
import org.jspringbot.syntax.HighlighterUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ssh helper
 */
public class SSHHelper {

    public static final JSpringBotLogger LOG = JSpringBotLogger.getLogger(SSHHelper.class);

    protected Connection conn;

    protected SSHConnectionBean sshConnectionBean;

    protected SCPClient scpClient;

    public SSHHelper(Connection conn, SSHConnectionBean sshConnectionBean) {
        this.conn = conn;
        this.sshConnectionBean = sshConnectionBean;
    }

    public void createScp() {
        try {
            scpClient = conn.createSCPClient();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create an ssh client protocol");
        }
    }

    public void sshConnect() throws IOException {
        sshConnectionBean.connect();
    }

    public void sshDisconnect() {
        sshConnectionBean.disconnect();
    }

    public void sysOutRemoteFile(String remoteFile) throws IOException {
        scpClient.get(remoteFile, System.out);
    }

    public void executeWhileOutputIsNot(String command, String compare, long sleep, long timeout) throws InterruptedException {
        Validate.notNull(compare, "Compare should not be null");

        String result = null;
        long totalSleep = 0;

        do {
            if (result != null) {
                LOG.info("Sleeping for %s ms.", sleep);
                totalSleep += sleep;
                Thread.sleep(sleep);
            }

            result = executeOutputContent(command);
        } while(!compare.equals(result) && totalSleep < timeout);
    }

    public void executeOutputContentShouldContain(String command, String value) {
        String actualValue = executeOutputContent(command);

        if (!actualValue.contains(value)) {
            throw new IllegalArgumentException(String.format("Actual value '%s' does not contain '%s'", actualValue, value));
        }
    }


    public void executeOutputContentShouldBe(String command, String expectedValue) {
        String actualValue = executeOutputContent(command);

        if (!actualValue.equals(expectedValue)) {
            throw new IllegalArgumentException(String.format("Expected value '%s' is not equal to actual value '%s'", expectedValue, actualValue));
        }
    }

    public String executeOutputContent(String command) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            long start = System.currentTimeMillis();
            conn.exec(command,out);
            String output = StringUtils.trim(out.toString());

            if(StringUtils.isNotBlank(output)) {
                LOG.pureHtml(String.format("<b>&gt; %s</b> (%d ms)", command, System.currentTimeMillis() - start) +
                        HighlighterUtils.INSTANCE.highlightNormal(output));
            }

            return output;
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error executing command: '%s'", command), e);
        }
    }

    public void executeCommand(String command) {
        try {
            executeOutputContent(command);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error executing command: '%s'", command), e);
        }
    }
}
