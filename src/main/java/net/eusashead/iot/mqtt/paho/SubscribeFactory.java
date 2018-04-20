package net.eusashead.iot.mqtt.paho;

/*
 * #[license]
 * rxmqtt
 * %%
 * Copyright (C) 2013 - 2016 Eusa's Head
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

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import net.eusashead.iot.mqtt.SubscribeMessage;

public class SubscribeFactory extends BaseMqttActionFactory {

    static final class SubscribeActionListener extends FlowableEmitterMqttActionListener<SubscribeMessage> {

        public SubscribeActionListener(final FlowableEmitter<? super SubscribeMessage> observer) {
            super(observer);
        }

        @Override
        public void onSuccess(final IMqttToken asyncActionToken) {
            // Does nothing
        }
    }

    private final static Logger LOGGER = Logger.getLogger(SubscribeActionListener.class.getName());

    public SubscribeFactory(final IMqttAsyncClient client) {
        super(client);
    }

    public Flowable<SubscribeMessage> create(final String[] topics, final int[] qos,
            final BackpressureStrategy backpressureStrategy) {

        Objects.requireNonNull(topics);
        Objects.requireNonNull(qos);
        Objects.requireNonNull(backpressureStrategy);

        return Flowable.create(emitter -> {

            // Message listeners
            final SubscriberMqttMessageListener[] listeners = new SubscriberMqttMessageListener[topics.length];
            for (int i = 0; i < topics.length; i++) {
                listeners[i] = new SubscriberMqttMessageListener(emitter);
            }

            try {
                this.client.subscribe(topics, qos, null, new SubscribeActionListener(emitter), listeners);
            } catch (final MqttException exception) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
                }
                emitter.onError(exception);
            }
        }, backpressureStrategy);
    }

}

class SubscriberMqttMessageListener implements IMqttMessageListener {

    private final static Logger LOGGER = Logger.getLogger(SubscriberMqttMessageListener.class.getName());

    private final FlowableEmitter<? super SubscribeMessage> observer;

    SubscriberMqttMessageListener(final FlowableEmitter<? super SubscribeMessage> emitter) {
        this.observer = Objects.requireNonNull(emitter);
    }

    @Override
    public void messageArrived(final String topic, final org.eclipse.paho.client.mqttv3.MqttMessage message) {
        LOGGER.log(Level.FINE, String.format("Message %s received on topic %s", message.getId(), topic));
        this.observer.onNext(
                SubscribeMessage.create(message.getId(), topic, message.getPayload(), message.getQos(), message.isRetained()));
    }
}
