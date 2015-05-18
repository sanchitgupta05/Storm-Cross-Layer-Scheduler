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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.Math;
import java.util.Random;

import org.apache.log4j.Logger;

import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.generated.*;


import backtype.storm.scheduler.crosslayerscheduler.BuildGraph;

public class SAAlgorithm {

	private Logger logger = Logger.getLogger(SAAlgorithm.class);

	private Cluster _cluster;
	private TopologyDetails _topologyDetail;
	private final double INITIAL_TEMP = 0.8;
	private final int MAX_STEP = 10;

	public SAAlgorithm(Cluster cluster, TopologyDetails topologyDetail) {
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
		BuildGraph graph = new BuildGraph(_topologyDetail.getTopology());
		
		for(int i = 0; i < 5; ++i) {
			double temp = INITIAL_TEMP;
			double currUtil = 0;
			Map<ExecutorDetails, WorkerSlot> currState;
			currState = initApp();
			if(currState == null) continue;
			
			for(int k = 0; k < MAX_STEP; ++k) {
				Map<ExecutorDetails, WorkerSlot> newState;
				newState = genStateApp(currState, graph);	
				double newUtil = computeUtil(newState);
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

		logger.info("Topology: " + _topologyDetail.getId() + "gets Assignment" + bestState);
		return bestState;
	}

	private Map<ExecutorDetails, WorkerSlot> initApp() {
		Map<ExecutorDetails, String> execToComp = 
			_cluster.getNeedsSchedulingExecutorToComponents(_topologyDetail);
		Map<ExecutorDetails, WorkerSlot> initState =
			new HashMap<ExecutorDetails, WorkerSlot>();
		
		List<WorkerSlot> assignableSlots = _cluster.getAvailableSlots();
		for(ExecutorDetails exec : execToComp.keySet()) {
			/* Assing a random assignable slot*/
		if(assignableSlots.size() != 0) {
				double r = Math.random();
				double size = (double)1.0/((double)assignableSlots.size()); 
				WorkerSlot w = assignableSlots.get((int)Math.floor(r/size));
				initState.put(exec, w);	
			}
			else {
				logger.info("IN INITAPP: Assignable slots ran out!!");
				return null;
			}
		}
		return initState;
	}


	private Map<ExecutorDetails, WorkerSlot> genStateApp(Map<ExecutorDetails, WorkerSlot> currState, 
																			BuildGraph graph) {
		int numOutgoing = 0;
		int numIncoming = 0;
		
		Map<ExecutorDetails, WorkerSlot> newState = new HashMap<ExecutorDetails, WorkerSlot>();
		newState.putAll(currState);
		Random r = new Random();
		int randIndex = r.nextInt(currState.size());
		
		ExecutorDetails chosenExec = (ExecutorDetails)(currState.keySet().toArray())[randIndex];
		WorkerSlot chosenSlot = currState.get(chosenExec);

		Map<String, List<ExecutorDetails>> compToExec = 
			_cluster.getNeedsSchedulingComponentToExecutors(_topologyDetail);
		Map<ExecutorDetails, String> execToComp = 
			_cluster.getNeedsSchedulingExecutorToComponents(_topologyDetail);
		String comp = execToComp.get(chosenExec);

		if(graph.adjList.containsKey(comp))
			numOutgoing= graph.adjList.get(comp).size();
		if(graph.revAdjList.containsKey(comp)) 
			numIncoming = graph.revAdjList.get(comp).size();
		
		List<String> compToPortTo = null;
		if(numOutgoing > numIncoming)
			compToPortTo = graph.adjList.get(comp);
		else
			compToPortTo = graph.revAdjList.get(comp);
			
		for(String randChosenComponentToMoveTo : compToPortTo) {
			List<ExecutorDetails> e = compToExec.get(randChosenComponentToMoveTo);
			for(int i = 0; i < e.size(); i++) {	
				String node = newState.get(e.get(i)).getNodeId();

				Set<Integer> availablePorts = _cluster.getAvailablePorts(
									_cluster.getSupervisorById(node));
				if(availablePorts.isEmpty() == true)  
					continue;
				else {
					int port = (Integer)(availablePorts.toArray())[0];
					WorkerSlot w =  new WorkerSlot(node, port);
					newState.put(e.get(i),w);
					return newState;
				}
		
			}
		}
		// if there are no available ports, then maybe
		// choose an assignable port as long as there is less contention
		// between the two threads --- TODO XXX
		

		/* Calculate the new Network Paths for the given config */

		return newState;
	}
	private double computeUtil(Map<ExecutorDetails, WorkerSlot> newState) {
		//
		
		return -1;
	}

	private double transition(double oldUtil, double newUtil, double temp) {
		if(newUtil > oldUtil)
			return 1.0;
		else
			return Math.pow(Math.E, (newUtil-oldUtil)/temp);
	}

	/*
	private _____ networkSA() {

	}

	*/

	/*	
	private ____ initNetwork() {

	}

	private ___ genStateNetwork () {

	}
	*/


}

