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

import java.lang.Math;

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

	private Logger logger = Logger.getLogger(SAAlgorithm.class);

	private Cluster _cluster;
	private TopologyDetails _topologyDetail;
	private final double INITIAL_TEMP = 0.8;
	private final int MAX_STEP = 10;


	public void SAAlgorithm(Cluster cluster, TopologyDetails topologyDetail) {
		_cluster = new Cluster();
		_topologyDetail = new TopologyDetails();

		_cluster = cluster;
		_topologyDetail = topologyDetail;

	} 

	public void update(Cluster cluster, TopologyDetails topologyDetail) {
		_cluster = cluster;
		_topologyDetail = topologyDetail;
	}

	public Map<ExecutorDetails, WorkerSlot> run() {
		Map<ExecutorDetails, WorkerSlot> assignment = _run();
		if(assignment != null) 
			return assignment;
		else 
			logger.info("SAAlgortithm wasn't able to compute an assignemt\n");
		return null;
	}

	private Map<ExecutorDetails, WorkerSlot> _run() {
		double bestUtil = 0;
		Map<ExecutorDetails, WorkerSlot> bestState = null;

		for(int i = 0; i < 5; ++i) {
			double temp = INITIAL_TEMP;
			double currUtil = 0;
			Map<ExecutorDetails, WorkerSlot> currState;
			currState = initApp();
			if(currState == null) continue;
			
			for(int k = 0; k < MAX_STEP; ++k) {
				Map<ExecutorDetails, WorkerSlot> newState;
				newState = genStateApp();	
				double newUtil = computeUtil();
				double r = Math.random(); // TODO can be changed to threshold

				if(transition(currUtil, newUtil, temp) > r) {
					currState = newState;
					currUtil = newUtil;
				}

				if(currUtil > bestUtil) {
					bestUtil = currUtil;
					bestState = currState;
				}

				temp = Math.pow(temp,0.95);
			}
		}

		return bestState;
	}

	private Map<ExecutorDetails, WorkerSlot> initApp() {
		Map<List<ExecutorDetails, String> execToComp = _cluster.getNeedsSchedulingExecutorToComponents(_topologyDetail);
		Map<ExecutorDetails, WorkerSlot> initState = new HashMap<ExecutorDetails, WorkerSlot>();
		List<WorkerSlot> assignableSlots = _cluster.getAssignableSlots();
		for( List<ExecutorDetails> listExec : execToComp.keySet()) {
			for(ExecutorDetails exec : listExec) {
				/* Assing a random assignable slot*/
				if(assignableSlots.size() != 0) {
					WorkerSlot w = assignableSlots.remove(0);
					initState.put(exec, w);		// check if we need 'new' call
				}
				else {
					logger.info("IN InitApp(): Assignable slots ran out!!");
					return null;
				}
			}
		}
		return initState;
	}


	private Map<ExecutorDetails, WorkerSlot> genStateApp() {
		// this is where the DeAlloc Heuristic comes in 

	}
	
	private ____ initNetwork() {

	}

	private ___ genStateNetwork () {

	}

	private double computeUtil() {
				

	}

	private double transition(double oldUtil, double newUtil, double temp) {
		if(newUtil > oldUtil)
			return 1.0;
		else
			return Math.pow(Math.E, (double)(oldUtil - newUtil)/temp);
	}

	private _____ networkSA() {

	}
}



