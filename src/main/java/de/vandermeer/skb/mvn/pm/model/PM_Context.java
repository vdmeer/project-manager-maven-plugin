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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * Context for the project model, as in everything it needs to generate and process a model.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 170331 (31-Mar-17) for Java 1.8
 * @since      v0.0.1
 */
public class PM_Context {

	/** The directory where the projects store their properties and configurations. */
	protected final String projectPmDir;

	/** Mapping of identifiers to dependency versions. */
	protected final Map<String, Ctxt_DependencyVersion> dependencyVersions;

	/** Mapping of property key to version for plugin versions. */
	protected final List<Map<String, String>> pluginVersions;

	/**
	 * Creates a new project model context.
	 * @param projectPmDir the standard project configuration directory
	 * @throws NullPointerException if argument was null
	 * @throws IllegalArgumentException if argument was blank
	 */
	public PM_Context(String projectPmDir){
		Validate.notBlank(projectPmDir, "mc: standard project PM directory names as null");
		this.projectPmDir = projectPmDir;

		this.dependencyVersions = new TreeMap<>();
		this.pluginVersions = new ArrayList<>();
	}

	/**
	 * Returns the standard project PM directory.
	 * @return standard project PM directory
	 */
	public String getProjectPmDir(){
		return this.projectPmDir;
	}

	/**
	 * Sets the dependency version information on the context (creates {@link Ctxt_DependencyVersion} objects).
	 * @param dependencyVersions the dependency version properties
	 * @throws IllegalArgumentException if any argument was problematic
	 */
	public void setDependencyVersions(Properties dependencyVersions){
		StrBuilder errors = new StrBuilder();
		for(Entry<Object, Object> p : dependencyVersions.entrySet()){
			try{
				Ctxt_DependencyVersion bv = new Ctxt_DependencyVersion(p.getKey().toString(), p.getValue().toString());
				this.dependencyVersions.put(bv.getId(), bv);
			}
			catch(Exception ex){
				errors.append(ex.getMessage()).appendNewLine();
			}
		}
		if(errors.size()>0){
			throw new IllegalArgumentException("PM Context, problems loading dependency versions, see below\n" + errors.toString());
		}
	}

	/**
	 * Returns the dependency version map of the context.
	 * @return dependency version map, empty if none added successfully
	 */
	public Map<String, Ctxt_DependencyVersion> getDependencyVersions(){
		return this.dependencyVersions;
	}

	/**
	 * Sets the plugin version information on the context.
	 * @param pluginVersions the plugin version properties
	 * @throws IllegalArgumentException if any argument was problematic
	 */
	public void setPluginVersions(Properties pluginVersions){
		StrBuilder errors = new StrBuilder();
		for(Entry<Object, Object> p : pluginVersions.entrySet()){
			try{
				Map<String, String> version = new HashMap<>();
				version.put("key", p.getKey().toString());
				version.put("value", p.getValue().toString());
				this.pluginVersions.add(version);
			}
			catch(Exception ex){
				errors.append(ex.getMessage()).appendNewLine();
			}
		}
		if(errors.size()>0){
			throw new IllegalArgumentException("PM Context, problems loading plugin versions, see below\n" + errors.toString());
		}
	}

	/**
	 * Returns the plugin version map of the context.
	 * @return plugin version map, empty if none added successfully
	 */
	public List<Map<String, String>> getPluginVersions(){
		return this.pluginVersions;
	}

}
