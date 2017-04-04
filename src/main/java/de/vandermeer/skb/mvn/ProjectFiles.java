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

package de.vandermeer.skb.mvn;

/**
 * Collection of all project property and configuration files.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3-SNAPSHOT build 170404 (04-Apr-17) for Java 1.8
 * @since      v0.0.1
 */
public enum ProjectFiles {

	/** The main configuration/property file for a managed project. */
	MANAGED_PROJECT_PROPERTIES ("project.properties", null, "the main configuration/property file for a managed project"),

	/** Additional dependencies of the project. */
	DEPENDENCIES("dependencies.pm", "dependencies", "additional dependencies of the project"),

	/** Distribution management information. */
	DISTRIBUTION_MANAGEMENT("distributionManagement.pm", "distMgmt", "distribution management information"),

	/** Project developers. */
	DEVELOPERS("developers.pm", "developers", "project developers"),

	/** Project contributors. */
	CONTRIBUTORS("contributors.pm", "contributors", "project contributors"),

	/** Project modules. */
	MODULES("modules.pm", "modules", "project modules"),

	/** Project reporting. */
	REPORTING("reporting.pm", "reporting", "project reporting"),

	/** Project profiles. */
	PROFILES("profiles.pm", "profiles", "project profiles"),

	/** Project properties. */
	PROPERTIES("properties.pm", "properties", "project properties"),

	;

	/** File name. */
	String fileName;

	/** The associated attribute in an ST template. */
	String stAttribute;

	/** File description. */
	String description;

	/**
	 * Creates a new file name enumerate.
	 * @param fileName the file name
	 * @param stAttribute the ST attribute where the file contents should be added to
	 * @param description a description of the enumerate
	 */
	ProjectFiles(String fileName, String stAttribute, String description){
		this.fileName = fileName;
		this.stAttribute = stAttribute;
		this.description = description;
	}

	/**
	 * Returns the actual file name.
	 * @return actual file name
	 */
	public String getFileName(){
		return this.fileName;
	}

	/**
	 * Returns the file description.
	 * @return file description
	 */
	public String description(){
		return this.description;
	}

	/**
	 * Returns the associated ST attribute of the file.
	 * @return ST attribute
	 */
	public String getStAttribute(){
		return this.stAttribute;
	}
}
