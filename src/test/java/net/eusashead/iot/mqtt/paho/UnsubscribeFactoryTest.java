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

import static org.hamcrest.Matchers.isA;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import net.eusashead.iot.mqtt.paho.UnsubscribeFactory.UnsubscribeActionListener;

@RunWith(JUnit4.class)
public class UnsubscribeFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenCreateIsCalledThenAnObservableIsReturned()
            throws Exception {
        // Given
        final IMqttAsyncClient client = Mockito.mock(IMqttAsyncClient.class);
        final UnsubscribeFactory factory = new UnsubscribeFactory(client);
        final String[] topics = new String[] { "topic1", "topic2" };
        final ArgumentCaptor<IMqttActionListener> actionListener = ArgumentCaptor
                .forClass(IMqttActionListener.class);

        // When
        final Completable obs = factory.create(topics);

        // Then
        Assert.assertNotNull(obs);
        obs.subscribe();
        Mockito.verify(client).unsubscribe(Matchers.same(topics),
                Matchers.isNull(), actionListener.capture());
        Assert.assertTrue(actionListener
                .getValue() instanceof UnsubscribeFactory.UnsubscribeActionListener);
    }

    @Test
    public void whenCreateIsCalledAndAnErrorOccursThenObserverOnErrorIsCalled()
            throws Throwable {
        this.expectedException.expectCause(isA(MqttException.class));
        final IMqttAsyncClient client = Mockito.mock(IMqttAsyncClient.class);
        Mockito.when(client.unsubscribe(Matchers.any(String[].class),
                Matchers.isNull(),
                Matchers.any(
                        UnsubscribeFactory.UnsubscribeActionListener.class)))
                .thenThrow(new MqttException(
                        MqttException.REASON_CODE_CLIENT_CONNECTED));
        final UnsubscribeFactory factory = new UnsubscribeFactory(client);
        final Completable obs = factory
                .create(new String[] { "topic1", "topic2" });
        obs.blockingAwait();
    }

    @Test
    public void whenOnSuccessIsCalledThenObserverOnNextAndOnCompletedAreCalled()
            throws Exception {
        final CompletableEmitter observer = Mockito
                .mock(CompletableEmitter.class);
        final UnsubscribeActionListener listener = new UnsubscribeFactory.UnsubscribeActionListener(
                observer);
        final IMqttToken asyncActionToken = Mockito.mock(IMqttToken.class);
        listener.onSuccess(asyncActionToken);
        Mockito.verify(observer).onComplete();
    }

}
