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

import org.apache.log4j.Logger;

import backtype.storm.scheduler.Topologies;
import backtype.storm.scheduler.TopologyDetails;
import backtype.storm.scheduler.WorkerSlot;
import backtype.storm.scheduler.ExecutorDetails;
import backtype.storm.scheduler.Cluster;
import backtype.storm.scheduler.SupervisorDetails;
import backtype.storm.generated.*;

/* 
* Graph Builder for the passed in Storm Topology
* It build the DAG of the topology
* It consists of two public variables 
*		- Map<String, List<String>> adjList;
*		- <ap<String, List<String>> revAdjList; 
*/ 

public class BuildGraph {
	public Map<String, List<String>> adjList;
	public Map<String, List<String>> revAdjList;

	public BuildGraph(StormTopology topology) {
		adjList = new HashMap<String, List<String>>();
		revAdjList = new HashMap<String, List<String>>();
		Map<String, Bolt> bolts = topology.get_bolts();
		Map<String, SpoutSpec> spouts = topology.get_spouts();

		for(String boltId: bolts.keySet()) {
			Map<GlobalStreamId,Grouping> inputs =
				bolts.get(boltId).get_common().get_inputs();
			for(GlobalStreamId from: inputs.keySet()) {
				String inputId = from.get_componentId();
				
				if(adjList.containsKey(inputId) == false) {
					List<String> tmp = new ArrayList<String>();
					tmp.add(boltId);
					adjList.put(inputId, tmp);
				}
				else 
					adjList.get(inputId).add(boltId);
				
				if(revAdjList.containsKey(boltId) == false) {
					List<String> tmp2 = new ArrayList<String>();
					tmp2.add(inputId);
					revAdjList.put(boltId,tmp2);
				}
				else
					revAdjList.get(boltId).add(inputId);
			}
		}
		for(String spoutId: spouts.keySet()) {
			Map<GlobalStreamId,Grouping> inputs =
				spouts.get(spoutId).get_common().get_inputs();

			for(GlobalStreamId from: inputs.keySet()) {
				String inputId = from.get_componentId();
				if(adjList.containsKey(inputId) == false) {
					List<String> tmp = new ArrayList<String>();
					tmp.add(spoutId);
					adjList.put(inputId, tmp);
				}
				else 
					adjList.get(inputId).add(spoutId);
				

				if(revAdjList.containsKey(spoutId) == false) {
					List<String> tmp2 = new ArrayList<String>();
					tmp2.add(inputId);
					revAdjList.put(spoutId,tmp2);
				}
				else
					revAdjList.get(spoutId).add(inputId);
			}
		}
	}
}

