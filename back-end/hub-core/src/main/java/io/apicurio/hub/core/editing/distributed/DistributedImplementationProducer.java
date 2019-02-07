/*
 * Copyright 2018 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apicurio.hub.core.editing.distributed;

import io.apicurio.hub.core.config.HubConfiguration;
import io.apicurio.hub.core.editing.OperationHandler;
import io.apicurio.hub.core.editing.ISharedApicurioSession;
import io.apicurio.hub.core.storage.IRollupExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Returns user-configured (or default) distributed session factory according to user configuration.
 *
 * Discovers all visible implementations of {@link IDistributedSessionFactory}, and registers
 * them by type/name {@link IDistributedSessionFactory#getSessionType()}.
 *
 * @see IDistributedSessionFactory
 * @see HubConfiguration
 *
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@ApplicationScoped
public class DistributedImplementationProducer {
    private static Logger logger = LoggerFactory.getLogger(DistributedImplementationProducer.class);

    @Inject
    private HubConfiguration config;

    @Inject
    private JMSSessionFactory jms;

    @Inject
    private NoOpSessionFactory noop;

    @Produces
    public IDistributedSessionFactory create() {
        if ("jms".equalsIgnoreCase(config.getDistributedSessionType())) {
            logger.debug("Selecting JMS distributed session");
            return new IDistributedSessionFactory() {
                @Override
                public ISharedApicurioSession joinSession(String designId, OperationHandler handler) {
                    return jms.joinSession(designId, handler);
                }

                @Override
                public String getSessionType() {
                    return jms.getSessionType();
                }

                @Override
                public void setRollupExecutor(IRollupExecutor rollupExecutor) {
                    jms.setRollupExecutor(rollupExecutor);
                }
            };
        } else {
            logger.debug("Selecting NoOp distributed session");
            return new IDistributedSessionFactory() {
                public ISharedApicurioSession joinSession(String designId, OperationHandler handler) {
                    return noop.joinSession(designId, handler);
                }

                @Override
                public String getSessionType() {
                    return noop.getSessionType();
                }

                @Override
                public void setRollupExecutor(IRollupExecutor rollupExecutor) {
                    noop.setRollupExecutor(rollupExecutor);
                }
            };
        }
    }
}
