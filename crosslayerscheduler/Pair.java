/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package backtype.storm.scheduler.crosslayerscheduler;


public class Pair<L,R> {

	private L _key;
	private R _value;

	public Pair(L key,R value) {
		_key = key;
		_value = value;
	}

	public L getKey() {
		return _key;
	}
	public R getValue() {
		return _value;
	}

	public void update(L key, R value) {
		_key = key;
		_value = value;
	}
	public void updateKey(L key) { _key = key;}
	public void updateValue(R value) {_value = value;}
}


