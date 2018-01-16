/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.namesrv;

import org.apache.rocketmq.common.MQVersion;
import org.apache.rocketmq.common.namesrv.NamesrvConfig;
import org.apache.rocketmq.remoting.netty.NettyServerConfig;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;
import org.apache.rocketmq.srvutil.ShutdownHookThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * 注册中心
 */
public class NamesrvStartup {
    private static final Logger log = LoggerFactory.getLogger(NamesrvStartup.class);


    public static Properties properties = null;

    public static void main(String[] args) {
        main0(args);
    }

    public static NamesrvController main0(String[] args) {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, Integer.toString(MQVersion.CURRENT_VERSION));
        try {
            final NamesrvConfig namesrvConfig = new NamesrvConfig();
            final NettyServerConfig nettyServerConfig = new NettyServerConfig();
            nettyServerConfig.setListenPort(9876);


            final NamesrvController controller = new NamesrvController(namesrvConfig, nettyServerConfig);

            controller.getConfiguration().registerConfig(properties);

            boolean initResult = controller.initialize();
            if (!initResult) {
                controller.shutdown();
                System.exit(-3);
            }

            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(log, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    controller.shutdown();
                    return null;
                }
            }));

            controller.start();

            System.out.println("The Name Server boot success. serializeType=" + RemotingCommand.getSerializeTypeConfigInThisServer());

            return controller;
        } catch (Throwable e) {
        }

        return null;
    }
}
