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

import org.apache.commons.lang3.Validate;

/**
 * Dependency object for the model.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 160306 (06-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class Model_Dependency {

	/** Dependency group identifier. */
	final String groupId;

	/** Dependency artifact identifier. */
	final String artifactId;

	/** Dependency version. */
	final String version;

	/** Dependency scope. */
	final String scope;

	/**
	 * Creates a new dependency object for the model.
	 * @param bv the build version object to use as source
	 * @param scope the scope for which the dependency is defined
	 * @throws NullPointerException if any argument was null or if getters in the argument returned null
	 * @throws IllegalArgumentException if any argument was blank (except scope, which defaults to (compile) if getters in the argument returned blank
	 */
	public Model_Dependency(Ctxt_DependencyVersion bv, String scope){
		this(bv.getGroupId(), bv.getArtifactId(), bv.getVersion(), scope);
	}

	/**
	 * Creates a new dependency object for the model.
	 * @param groupId the dependecy's group identifier
	 * @param artifactId the dependecy's artifact identifier
	 * @param version the dependecy's version
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any argument was blank
	 */
	public Model_Dependency(String groupId, String artifactId, String version){
		this(groupId, artifactId, version, null);
	}

	/**
	 * Creates a new dependency object for the model.
	 * @param groupId the dependecy's group identifier
	 * @param artifactId the dependecy's artifact identifier
	 * @param version the dependecy's version
	 * @param scope scope for the dependency
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any argument was blank (except scope, which defaults to (compile)
	 */
	public Model_Dependency(String groupId, String artifactId, String version, String scope){
		Validate.notBlank(groupId);
		Validate.notBlank(artifactId);
		Validate.notNull(version);

		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.scope = (scope==null)?"compile":scope;
	}

	/**
	 * Returns the group identifier.
	 * @return return the group identifier.
	 */
	public String getGroupId(){
		return this.groupId;
	}

	/**
	 * Returns the artifact identifier.
	 * @return artifact identifier
	 */
	public String getArtifactId(){
		return this.artifactId;
	}

	/**
	 * Returns the version.
	 * @return version
	 */
	public String getVersion(){
		return this.version;
	}

	/**
	 * Returns the scope for which the dependency is defined.
	 * @return dependency scope
	 */
	public String getScope(){
		return this.scope;
	}

}
