package net.eusashead.iot.mqtt;

import java.util.Arrays;

/*
 * #[license]
 * rxmqtt
 * %%
 * Copyright (C) 2013 - 2018 Eusa's Head
 * %%
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
 * %[license]
 */

public interface PublishMessage extends MqttMessage {
	
	static PublishMessage create(final byte[] payload, final int qos, final boolean retained) {
        return new PublishMessageImpl(payload, qos, retained);
    }
	
	class PublishMessageImpl extends AbstractMqttMessage implements PublishMessage {

        private PublishMessageImpl(byte[] payload, int qos, boolean retained) {
            super(payload, qos, retained);
        }

        @Override
        public String toString() {
            return "PublishMessageImpl [payload=" + Arrays.toString(payload)
                    + ", qos=" + qos + ", retained=" + retained + "]";
        }

	}

}
