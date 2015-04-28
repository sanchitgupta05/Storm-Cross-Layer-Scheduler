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
package backtype.storm.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.math;

import org.apache.log4j.Logger;

import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.scheduler.EvenScheduler;



public class SAAlgorithm {

	private Cluster _cluster;
	private TopologyDetails _topologyDetail;

	public void SAAlgorithm(Cluster cluster, TopologyDetails topologyDetail) {
		_cluster = new Cluster();
		_topologyDetail = new TopologyDetails();

		_cluster = cluster;
		_topologyDetail = topologyDetail;

	} 


	private void run() {

		float bestUtil = 0;
		

	}

	private ____  initApp() {

	}

	private ____ genStateApp() {

	}
	
	private ____ initNetwork() {

	}

	private ___ genStateNetwork (){

	}


}







