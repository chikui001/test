/*
 * Copyright (C) 2013 Square, Inc.
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
package com.example.jiao.myapplication;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class Action<T> {

	static class RequestWeakReference<M> extends WeakReference<M> {

			final Action action;

			public RequestWeakReference(Action action, M referent, ReferenceQueue<? super M> q) {
				super(referent, q);
				this.action = action;
			}
	}

	final WeakReference<T> target;

	public Action(T target, ReferenceTest referenceTest) {
		this.target = target == null ? null : new RequestWeakReference<T>(this, target, referenceTest.referenceQueue);
	}
}
