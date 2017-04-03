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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Dependency version object for context, collects all artifacts and their information from the dependency version file.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 170331 (31-Mar-17) for Java 1.8
 * @since      v0.0.1
 */
public class Ctxt_DependencyVersion {

	/** An identifier for the dependency, as given in the configuration file. */
	final String id;

	/** Dependency group identifier. */
	final String groupId;

	/** Dependency artifact identifier. */
	final String artifactId;

	/** Dependency version. */
	final String version;

	/**
	 * Creates a new dependency version object for the context.
	 * @param id the identifier of the artifact
	 * @param value the value (group, artifact, version information)
	 * @throws NullPointerException if any argument was null or resulted in null strings
	 * @throws IllegalArgumentException if any argument was blank, resulted in blank strings, or was otherwise problematic
	 */
	public Ctxt_DependencyVersion(String id, String value){
		Validate.notBlank(id);
		Validate.notBlank(value);

		this.id = id;

		String[] _val = StringUtils.split(value);
		if(_val.length!=3){
			throw new IllegalArgumentException("dependency definition has not all required elements: <" + id + "=" + value +">");
		}

		this.groupId = _val[0];
		this.artifactId = _val[1];
		this.version = _val[2];

		Validate.notBlank(this.groupId);
		Validate.notBlank(this.artifactId);
		Validate.notBlank(this.version);
	}

	/**
	 * Returns the object identifier.
	 * @return object identifier
	 */
	public String getId(){
		return this.id;
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
}
