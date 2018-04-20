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

import io.reactivex.CompletableEmitter;

public abstract class CompletableEmitterMqttActionListener
        extends BaseEmitterMqttActionListener {

    protected final CompletableEmitter emitter;

    public CompletableEmitterMqttActionListener(
            final CompletableEmitter emitter) {
        this.emitter = Objects.requireNonNull(emitter);
    }

    @Override
    public OnError getOnError() {
        return new OnError() {

            @Override
            public void onError(final Throwable t) {
                CompletableEmitterMqttActionListener.this.emitter.onError(t);
            }
        };
    }

}
