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
 * @version    v0.0.2 build 160304 (04-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class PM_Context {

	/** The directory where the projects store their properties and configurations. */
	protected final String projectPmDir;

	/** Mapping of identifiers to build versions. */
	protected final Map<String, Ctxt_BuildVersion> buildVersions;

	/**
	 * Creates a new project model context.
	 * @param projectPmDir the standard project configuration directory
	 * @throws NullPointerException if argument was null
	 * @throws IllegalArgumentException if argument was blank
	 */
	public PM_Context(String projectPmDir){
		Validate.notBlank(projectPmDir, "mc: standard project PM directory names as null");
		this.projectPmDir = projectPmDir;

		this.buildVersions = new TreeMap<>();
	}

	/**
	 * Returns the standard project PM directory.
	 * @return standard project PM directory
	 */
	public String getProjectPmDir(){
		return this.projectPmDir;
	}

	/**
	 * Sets the build version information on the context (creates {@link Ctxt_BuildVersion} objects).
	 * @param buildVersions the build version properties
	 * @throws IllegalArgumentException if any argument was problematic
	 */
	public void setBuildVersions(Properties buildVersions){
		StrBuilder errors = new StrBuilder();
		for(Entry<Object, Object> p : buildVersions.entrySet()){
			try{
				Ctxt_BuildVersion bv = new Ctxt_BuildVersion(p.getKey().toString(), p.getValue().toString());
				this.buildVersions.put(bv.getId(), bv);
			}
			catch(Exception ex){
				errors.append(ex.getMessage()).appendNewLine();
			}
		}
		if(errors.size()>0){
			throw new IllegalArgumentException("PM Context, problems loading build versions, see below\n" + errors.toString());
		}
	}

	/**
	 * Returns the build version map of the context.
	 * @return build version map, empty if none added successfully
	 */
	public Map<String, Ctxt_BuildVersion> getBuildVersions(){
		return this.buildVersions;
	}
}
