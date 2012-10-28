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

import java.io.File;
import java.io.IOException;

public class SSHConnectionBean {

    private Connection conn;

    private String user;

    private File privateKey;

    private String password;

    public SSHConnectionBean(Connection conn) {
        this.conn = conn;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPrivateKey(File privateKey) {
        this.privateKey = privateKey;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void connect() throws IOException {
        conn.connect();

        if (privateKey == null) {
            conn.authenticateWithPassword(user, password);
        } else {
            conn.authenticateWithPublicKey(user, privateKey, password);
        }
    }

    public void disconnect() {
        conn.close();
    }
}
