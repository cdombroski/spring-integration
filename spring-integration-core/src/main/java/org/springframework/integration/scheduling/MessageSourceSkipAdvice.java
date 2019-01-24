/*
 * Copyright 2014-2019 the original author or authors.
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

package org.springframework.integration.scheduling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.aop.AbstractMessageSourceAdvice;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.scheduling.PollSkipAdvice.DefaultPollSkipStrategy;
import org.springframework.messaging.Message;

/**
 * An advice that can be added to a message source's advice chain that determines
 * whether a message should be received. May be used to temporarily suspend
 * message receipt when some downstream condition exists in the flow.
 *
 * @author Gary Russell
 * @author Chris Dombroski
 * @since 5.2
 *
 */
public class MessageSourceSkipAdvice extends AbstractMessageSourceAdvice {

	private static final Log logger = LogFactory.getLog(MessageSourceSkipAdvice.class);

	private final PollSkipStrategy pollSkipStrategy;

	public MessageSourceSkipAdvice() {
		this(new DefaultPollSkipStrategy());
	}


	public MessageSourceSkipAdvice(PollSkipStrategy strategy) {
		this.pollSkipStrategy = strategy;
	}


	@Override
	public boolean beforeReceive(MessageSource<?> messageSource) {
		boolean skipPoll = this.pollSkipStrategy.skipPoll();
		if (skipPoll && logger.isDebugEnabled()) {
			logger.debug("Skipping poll because "
					+ this.pollSkipStrategy.getClass().getName()
					+ ".skipPoll() returned true");
		}
		return !skipPoll;
	}

	@Override
	public Message<?> afterReceive(Message<?> result, MessageSource<?> source) {
		return result;
	}

}
