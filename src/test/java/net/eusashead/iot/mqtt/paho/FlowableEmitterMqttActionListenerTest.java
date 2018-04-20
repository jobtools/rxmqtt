package net.eusashead.iot.mqtt.paho;

/*
 * #[license]
 * rxmqtt
 * %%
 * Copyright (C) 2013 - 2017 Eusa's Head
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

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import io.reactivex.FlowableEmitter;

@RunWith(JUnit4.class)
public class FlowableEmitterMqttActionListenerTest {

    @Test(expected = NullPointerException.class)
    public void whenTheConstructorIsCalledWithANullEmitterANullPointerExceptionOccurs() {
        new FlowableEmitterMqttActionListener<Object>(null) {

            @Override
            public void onSuccess(final IMqttToken asyncActionToken) {
                // Not invoked
            }

        };
    }

    @Test
    public void whenTheConstructorIsCalledWithAValidEmitterThenGetOnErrorReturnsTheEmitter() {

        // Given
        @SuppressWarnings("unchecked")
        final FlowableEmitter<Object> emitter = Mockito
                .mock(FlowableEmitter.class);
        final Throwable ex = Mockito.mock(Throwable.class);
        final FlowableEmitterMqttActionListener<Object> listener = new FlowableEmitterMqttActionListener<Object>(
                emitter) {

            @Override
            public void onSuccess(final IMqttToken asyncActionToken) {
                // Not invoked
            }
        };

        // When
        final OnError onError = listener.getOnError();
        onError.onError(ex);

        // Then
        Mockito.verify(emitter).onError(ex);
    }

}
