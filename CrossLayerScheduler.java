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

import org.apache.log4j.Logger;

import backtype.storm.scheduler.Pair;
import backtype.storm.scheduler.IScheduler;
import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.scheduler.EvenScheduler;
import backtype.storm.scheduler.SAAlgorithm;

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
			if(_scheduleTopology(currTopology,cluster) == false)
				logger.info("Cannot Schedule topology" + currTopology.getId());
		}
	}

	private boolean _scheduleTopology(TopologyDetails topologyDetail, Cluster cluster) {
		Map<String, List<ExecutorDetails>> compToExec = cluster.getNeedsSchedulingComponentsToExecutors(topologyDetail);
		if(CompToExec == null) return true;

		

	}
	
	@Override
	public void schedule(Topologies topologies, Cluster cluster) {
		doSchedule(topologies, cluster);
	}

	@Override
	public void prepare(Map conf){}

}



