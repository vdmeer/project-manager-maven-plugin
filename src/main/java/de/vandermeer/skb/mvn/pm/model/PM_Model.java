/* Copyright 2016 Sven van der Meer <vdmeer.sven@mykolab.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vandermeer.skb.mvn.pm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Model of projects, with their settings and configurations.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 160306 (06-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class PM_Model {

	/** Model context, everything the model needs to know. */
	protected final PM_Context mc;

	/** The loader for the model. */
	protected ModelLoader loader;

	/** Mapping of identifier to managed project. */
	protected final Map<String, Model_ManagedProject> projects;

	/**
	 * Creates a new project model, programmatic access to all managed projects.
	 * @param mc model context
	 * @throws NullPointerException if argument was null
	 */
	public PM_Model(PM_Context mc){
		Validate.notNull(mc);
		this.mc = mc;

		this.projects = new TreeMap<>();
	}

	/**
	 * Loads a model from a collection of project folders.
	 * @param folders collection of project folders
	 * @throws IOException if read on a property file failed
	 * @throws FileNotFoundException if a property file was not found
	 * @throws NullPointerException if the collection or any member was null
	 * @throws IllegalArgumentException if any collection member resulted in a was a blank string or pointed to an unreadable/unwritable directory
	 */
	public void loadModel(Collection<Object> folders) throws FileNotFoundException, IOException{
		this.loader = new ModelLoader(mc, folders);
		for(Entry<File, Pair<File, File>> e : this.loader.getProjectFiles().entrySet()){
			Model_ManagedProject mp = new Model_ManagedProject(this.mc, e.getKey(), e.getValue().getKey(), e.getValue().getValue());
			this.projects.put(mp.getPmId(), mp);
		}
	}

	/**
	 * Returns the model context.
	 * @return model context
	 */
	public PM_Context getModelContext(){
		return this.mc;
	}

	/**
	 * Returns all loaded managed projects.
	 * @return all loaded managed projects
	 */
	public Collection<Model_ManagedProject> getManagedProjects(){
		return this.projects.values();
	}

	/**
	 * Update dependencies for each loaded project.
	 * Once all projects and the build version dependencies are loaded, we can update the dependencies for each individual project.
	 * This allows forward definition of dependencies.
	 */
	public void updateProjectDependencies(){
		StrBuilder error = new StrBuilder();
		for(Model_ManagedProject mp : this.projects.values()){
			error.appendSeparator('\n');
			error.append(mp.updateDependencies());
		}
		if(error.size()>0){
			throw new IllegalArgumentException("problems updating dependencies, see below\n" + error.toString());
		}
	}
}
