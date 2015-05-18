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
//package backtype.storm.scheduler.crosslayerscheduler;
package backtype.storm.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

import org.apache.log4j.Logger;

import backtype.storm.scheduler.crosslayerscheduler.Pair;
import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.scheduler.crosslayerscheduler.SAAlgorithm;
import backtype.storm.generated.*;

public class CrossLayerScheduler implements IScheduler {

	private Logger logger = Logger.getLogger(CrossLayerScheduler.class);

	/* Map to store the */
	Map<Pair<String, String>, Double> pairNodesToBandwidth;

	private void init() {
		
		// need to craete a Topology Map of 
		// Key -> Pair of NodeIds
		// 'k' shortest paths between them and corresponding Available bandwidth

		// Use modified Floyd-Warshall algorithm to get the sK shortest paths between

	}

	private void doSchedule(Topologies topologies, Cluster cluster) {
		List<TopologyDetails> needSched = cluster.needsSchedulingTopologies(topologies);
		for(TopologyDetails currTopology : needSched) {
			System.out.println("************************************************************************************************************************************************************************************************************************************************************************************************ TRYNING TO SCHEDULE "+currTopology.getId());
			if(_scheduleTopology(currTopology,cluster) == false)
				logger.warn("Cannot Schedule topology" + currTopology.getId());
		}
	}

	private boolean _scheduleTopology(TopologyDetails topologyDetail, Cluster cluster) {
		Map<String, List<ExecutorDetails>> compToExec = 
			cluster.getNeedsSchedulingComponentToExecutors(topologyDetail);
		if(compToExec == null) return true;
	

		SAAlgorithm algorithm = new SAAlgorithm(cluster, topologyDetail);
		Map<ExecutorDetails, WorkerSlot> assignments = algorithm.run();
		if(assignments == null) 
			return false;
		
		try {
			for(ExecutorDetails e : assignments.keySet()) {	
				Collection<ExecutorDetails> k = new ArrayList<ExecutorDetails>();
				k.add(e);
				cluster.assign(assignments.get(e), 
						topologyDetail.getId(), k);
			}
		} catch(Exception f) {
			logger.warn("Exception: "+f+" caught while assigning job to cluster");
		}
		return true;
	}
	
	@Override
	public void schedule(Topologies topologies, Cluster cluster) {
		System.out.println("******************888 CROSS LAYER SCHEDULER 888******************");
		doSchedule(topologies, cluster);
	}

	@Override
	public void prepare(Map conf){}

}


