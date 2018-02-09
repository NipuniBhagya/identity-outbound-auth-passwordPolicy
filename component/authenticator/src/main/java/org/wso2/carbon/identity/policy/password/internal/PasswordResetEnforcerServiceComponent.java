/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.identity.policy.password.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.event.stream.core.EventStreamService;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.policy.password.PasswordChangeEnforcerOnExpiration;
import org.wso2.carbon.identity.policy.password.PasswordChangeHandler;

/**
 * @scr.component name="org.wso2.carbon.identity.policy.password.component" immediate="true"
 * @scr.reference name="eventStreamManager.service"
 * interface="org.wso2.carbon.event.stream.core.EventStreamService" cardinality="1..1"
 * policy="dynamic" bind="setEventStreamService" unbind="unsetEventStreamService"
 */
public class PasswordResetEnforcerServiceComponent {
    private static Log log = LogFactory.getLog(PasswordResetEnforcerServiceComponent.class);

    /**
     * Activate the application authenticator and user operation event listener event components
     *
     * @param ctxt the component context.
     */
    protected void activate(ComponentContext ctxt) {
        try {
            // register the connector to enforce password change upon expiration.
            ctxt.getBundleContext()
                    .registerService(ApplicationAuthenticator.class.getName(),
                            new PasswordChangeEnforcerOnExpiration(), null);

            // register the listener to capture password change events.
            ctxt.getBundleContext()
                    .registerService(AbstractEventHandler.class.getName(),
                            new PasswordChangeHandler(), null);

            log.debug("PasswordChangeEnforcerOnExpiration handler is activated");
        } catch (Throwable e) {
            log.fatal("Error while activating the PasswordChangeEnforcerOnExpiration handler ", e);
        }
    }

    /**
     * Deactivate the component
     *
     * @param ctxt the component context.
     */
    protected void deactivate(ComponentContext ctxt) {
        log.debug("PasswordChangeEnforcerOnExpiration is deactivated");
    }

    protected void setEventStreamService(EventStreamService eventStreamService) {
        PasswordResetEnforcerDataHolder.getInstance().setEventStreamService(eventStreamService);
    }

    protected void unsetEventStreamService(EventStreamService eventStreamService) {
        PasswordResetEnforcerDataHolder.getInstance().setEventStreamService(eventStreamService);
    }
}
