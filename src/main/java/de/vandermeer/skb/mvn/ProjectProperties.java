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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection of all project properties.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.3 build 170404 (04-Apr-17) for Java 1.8
 * @since      v0.0.1
 */
public enum ProjectProperties {

	//
	// PM PROPERTIES
	//

	/** The identifier of the project, must be unique for all loaded projects. */
	PM_ID ("pm.id", true, ""),

	/** Dependencies (of project managed by the pm) as a list of - name of the project with an optional scope separated by a slash. */
	PM_DEPENDENCIES ("pm.dependencies", false, "dependencies (of project managed by the pm) as a list of - name of the project with an optional scope separated by a slash"),

	/** Flag for adding an ASCIIDOC profile to compile bundle documentation. */
	PM_DO_BUNDLE_DOC ("pm.do.bundle-doc", false, "flag for adding an ASCIIDOC profile to compile bundle documentation"),

	/** Licenses for the project, keys (separated by whitespace) will be translated to license definitions in the project manager. */
	PM_LICENSES ("pm.licenses", true, "licenses for the project, keys will be translated to license definitions in the project manager"),

	/** List of files with plugin definitions, path relative from the main project property file. */
	PM_PLUGINS ("pm.plugins", false, "list of files with plugin definitions, path relative from the main project property file"),

	/** List of files with profile definitions, path relative from the main project property file. */
	PM_PROFILES ("pm.profiles", false, "list of files with profiles definitions, path relative from the main project property file"),

	/** Flag for adding a jar plugin. */
	PM_USE_JAR_PLUGIN ("pm.use.jar-plugin", false, "flag for adding a jar plugin"),

	/** Flag for adding a source profile. */
	PM_USE_SRC_PROFILE ("pm.use.src-profile", false, "flag for adding a source profile"),

	/** Flag for adding a compiler plugin. */
	PM_USE_COMPILER_PLUGIN ("pm.use.compiler-plugin", false, "flag for adding a compiler plugin"),

	/** Flag for adding a Javadoc profile. */
	PM_USE_JAVADOC_PROFILE ("pm.use.javadoc-profile", false, "flag for adding a Javadoc profile"),

	/** Flag for adding a Javadoc profile with AsciiDoc doclet. */
	PM_USE_JAVADOC_ADOC_PROFILE ("pm.use.javadoc-adoc-profile", false, "flag for adding a Javadoc profile with AsciiDoc doclet"),

	/** Flag for adding a maven site plugin. */
	PM_USE_MAVEN_SITE_PLUGIN ("pm.use.maven-site-plugin", false, "flag for adding a maven site plugin"),

	//
	// MAVEN BASIC INFORMATION
	//
	/** Maven group identifier - groupId. */
	MVN_GROUP_ID ("mvn.group.id", true, ""),
	/** Maven group artifact - artifactId. */
	MVN_ARTIFACT_ID ("mvn.artifact.id", true, ""),
	/** Maven version - version. */
	MVN_VERSION ("mvn.version", true, ""),

	/** Packaging of the Maven project: jar, maven-plugin, etc. */
	MVN_PACKAGING ("mvn.packaging", false, "jar", "packaging of the maven project: jar, maven-plugin, ..."),

	/** Maven property for compiler source version. */
	MVN_PROPERTIES_COMPILER_SOURCE ("mvn.properties.compiler.source", true, ""),
	/** Maven property for compiler target version. */
	MVN_PROPERTIES_COMPILER_TARGET ("mvn.properties.compiler.target", true, ""),
	/** Maven property for file encodings. */
	MVN_PROPERTIES_ENCODING ("mvn.properties.encoding", true, ""),

	//
	// MORE PROJECT INFORMATION
	//
	/** Maven project name. */
	MVN_NAME ("mvn.name", true, ""),
	/** Maven project description. */
	MVN_DESCRIPTION ("mvn.description", true, ""),
	/** Maven project URL. */
	MVN_URL ("mvn.url", true, ""),
	/** Maven project inception year. */
	MVN_INCEPTION_YEAR ("mvn.inception.year", true, ""),
	/** Maven organization name. */
	MVN_ORGANIZATION_NAME ("mvn.organization.name", false, ""),
	/** Maven organization URL. */
	MVN_ORGANIZATION_URL ("mvn.organization.url", false, ""),


	//
	// MAVEN ISSUE MANAGEMENT
	//
	/** Maven issue management URL. */
	MVN_ISSUE_MANAGEMENT_URL ("mvn.issue.management.url", true, ""),
	/** Maven issue management system. */
	MVN_ISSUE_MANAGEMENT_SYSTEM ("mvn.issue.management.system", true, ""),


	//
	// MAVEN SCM
	//
	/** Maven SCM developer connection. */
	MVN_SCM_DEVELOPER_CONNECTION ("mvn.scm.developer.connection", true, ""),
	/** Maven SCM connection. */
	MVN_SCM_CONNECTION ("mvn.scm.connection", true, ""),
	/** Maven SCM URL. */
	MVN_SCM_URL ("mvn.scm.url", true, ""),


	;

	/** Property name. */
	String propName;

	/** Flag for required, mandatory properties. */
	boolean required;

	/** Default value for the property. */
	String defaultValue;

	/** Property description. */
	String description;

	/**
	 * Creates a new project property.
	 * @param propName the name of the property (as used in a property file)
	 * @param required flag for property beging required
	 * @param defaultValue a default value
	 * @param description a description of the property
	 */
	ProjectProperties(String propName, boolean required, String defaultValue, String description){
		this.propName = propName;
		this.required = required;
		this.defaultValue = defaultValue;
		this.description = description;
	}

	/**
	 * Creates a new project property.
	 * @param propName the name of the property (as used in a property file)
	 * @param required flag for property beging required
	 * @param defaultValue a default value
	 * @param description a description of the property
	 */
	ProjectProperties(String propName, boolean required, String description){
		this(propName, required, null, description);
	}

	/**
	 * Returns the actual name of the property.
	 * @return property name
	 */
	public String getPropName(){
		return this.propName;
	}

	/**
	 * Returns the required flag of the property
	 * @return true if required, false otherwise
	 */
	public boolean isRequired(){
		return this.required;
	}

	/**
	 * Returns the default value of the property.
	 * @return property default value
	 */
	public String getDefaultValue(){
		return this.defaultValue;
	}

	/**
	 * Returns the property description.
	 * @return property description
	 */
	public String description(){
		return this.description;
	}

	/**
	 * Returns all required properties.
	 * @return required properties
	 */
	public static Collection<ProjectProperties> getRequried(){
		Set<ProjectProperties> ret = new HashSet<>();
		for(ProjectProperties p : ProjectProperties.values()){
			if(p.isRequired()){
				ret.add(p);
			}
		}
		return ret;
	}
}
