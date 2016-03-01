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
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

import de.vandermeer.skb.mvn.Licenses;
import de.vandermeer.skb.mvn.ProjectFiles;
import de.vandermeer.skb.mvn.ProjectProperties;

/**
 * Managed project object for the model.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2-SNAPSHOT build 160301 (01-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class Model_ManagedProject {

	/** Model context, everything the model needs to know. */
	protected final PM_Context mc;

	/** The project base directory. */
	protected final File baseDir;

	/** The project PM directory. */
	protected final File pmDir;

	/** The project property file. */
	protected final File projectPropertyFile;

	/** The PM identifier of the project. */
	protected final String pmId;

	/** THe project properties as read from the PM configuration file. */
	protected Properties properties;

//	/** A map view of the properties. */
//	protected final Map<Object, Object> propertyMap;

	/** Set of dependencies that are defined by the project and for which we have found build version definitions. */
	protected final Set<Model_Dependency> dependencies;

	/** Project licenses. */
	protected final Set<Licenses> licenses;

	/** Files with more project configuration. */
	protected final Map<ProjectFiles, File> otherProjectFiles;

	/** Plugin definitions files of the project. */
	protected final Set<File> plugins;

	/**
	 * Creates a new Managed Project object.
	 * @param mc the model context
	 * @param baseDir project base directory
	 * @param pmDir the project PM directory
	 * @param propertyFile the project property file
	 * @throws NullPointerException if any argument was null
	 * @throws IllegalArgumentException if any sub-method experienced an illegal argument
	 * @throws FileNotFoundException if any file (property file) was not found
	 * @throws IOException if reading the property file caused an IO exception
	 */
	Model_ManagedProject(PM_Context mc, File baseDir, File pmDir, File propertyFile) throws FileNotFoundException, IOException{
		Validate.notNull(mc);
		Validate.notNull(baseDir);
		Validate.notNull(pmDir);
		Validate.notNull(propertyFile);

		this.mc = mc;
		this.baseDir = baseDir;
		this.pmDir = pmDir;
		this.projectPropertyFile = propertyFile;

//		this.propertyMap = new HashMap<>();
		this.loadProperties();
		this.pmId = this.properties.getProperty(ProjectProperties.PM_ID.getPropName());

		this.mc.getBuildVersions().put(this.getPmId(), new Ctxt_BuildVersion(this.pmId, this.getMvnGroupId() + " " + this.getMvnArtifactId() + " " + this.getMvnVersion()));

		this.dependencies = new LinkedHashSet<>();
		this.licenses = new LinkedHashSet<>();
		if(this.properties.getProperty(ProjectProperties.PM_LICENSES.getPropName())!=null){
			for(String l : StringUtils.split(this.properties.getProperty(ProjectProperties.PM_LICENSES.getPropName()))){
				try{
					this.licenses.add(Licenses.valueOf(l));
				}
				catch(Exception ex){
					throw new IllegalArgumentException("project <" + this.pmId + "> requires unknown license <" + l +">");
				}
			}
		}

		//get all other files from the pm directory
		this.otherProjectFiles = new LinkedHashMap<>();
		for(ProjectFiles pf : ProjectFiles.values()){
			if(pf!=ProjectFiles.MANAGED_PROJECT_PROPERTIES){
				File f = new File(this.pmDir + File.separator + pf.getFileName());
				if(f.exists() && f.canRead()){
					this.otherProjectFiles.put(pf, f);
				}
			}
		}

		//get all plugin files requires
		this.plugins = new LinkedHashSet<>();
		if(this.properties.getProperty(ProjectProperties.PM_PLUGINS.getPropName())!=null){
			for(String fn : StringUtils.split(this.properties.getProperty(ProjectProperties.PM_PLUGINS.getPropName()))){
				File f = new File(this.pmDir + File.separator + fn);
				if(f.exists() && f.canRead()){
					this.plugins.add(f);
				}
				else{
					throw new IllegalArgumentException("project <" + this.pmId + "> required plugin file <" + f +">");
				}
			}
		}
	}

	/**
	 * Returns the identifier of the managed project as used by the project manager.
	 * @return managed project identifier
	 */
	public String getPmId(){
		return this.pmId;
	}

	/**
	 * Checks a list of properties for containing all required properties
	 * @return true on success (exceptions on error)
	 * @throws IOException if read from property file {@link #projectPropertyFile} failed 
	 * @throws FileNotFoundException if property file property file {@link #projectPropertyFile} was not found
	 * @throws NullPointerException if required property not in list or value null
	 * @throws IllegalArgumentException if required property in list but value blank 
	 */
	protected boolean loadProperties() throws FileNotFoundException, IOException{
		this.properties = new Properties();
//		this.getModelContext().log.info(this.projectPropertyFile.toString());
		this.properties.load(new FileReader(this.projectPropertyFile));
		for(ProjectProperties pp : ProjectProperties.getRequried()){
			Validate.notBlank(this.properties.getProperty(pp.getPropName()), "<" + this.projectPropertyFile + "> -> required property does not exist or is blank: <" + pp.getPropName() + ">");
		}
//		for(Entry<Object, Object> s : this.properties.entrySet()){
//			if(s.getKey()!=null && s.getValue()!=null){
//				this.propertyMap.put(s.getKey(), s.getValue());
//			}
//		}
		return true;
	}

	/**
	 * Updates the dependencies, should be called once all projects are loaded.
	 * @return errors, empty if none occurred
	 */
	protected StrBuilder updateDependencies(){
		StrBuilder ret = new StrBuilder();
		String deps = this.properties.getProperty(ProjectProperties.PM_DEPENDENCIES.getPropName());
		if(deps!=null){
			for(String dep : StringUtils.split(deps)){
				String[] actual = StringUtils.split(dep, "/");
				String scope = (actual.length==2)?actual[1]:null;
				if(this.mc.getBuildVersions().get(actual[0])==null){
					ret.append("project <" + this.pmId + "> uses unkown dependency <" + dep + "> - check project's '" + ProjectProperties.PM_DEPENDENCIES.getPropName() + "'");
				}
				else{
					Model_Dependency md = new Model_Dependency(this.mc.getBuildVersions().get(actual[0]), scope);
					this.dependencies.add(md);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns the model context.
	 * @return model context
	 */
	public PM_Context getModelContext(){
		return this.mc;
	}

	/**
	 * Returns the MVN groupID
	 * @return MVN groupID
	 */
	public String getMvnGroupId(){
		return this.properties.getProperty(ProjectProperties.MVN_GROUP_ID.getPropName());
	}

	/**
	 * Returns the MVN artifactID
	 * @return MVN artifactID
	 */
	public String getMvnArtifactId(){
		return this.properties.getProperty(ProjectProperties.MVN_ARTIFACT_ID.getPropName());
	}

	/**
	 * Returns the MVN version
	 * @return MVN version
	 */
	public String getMvnVersion(){
		return this.properties.getProperty(ProjectProperties.MVN_VERSION.getPropName());
	}

	/**
	 * Returns the MVN packaging
	 * @return MVN packaging, default value as defined in {@link ProjectProperties#MVN_PACKAGING}
	 */
	public String getMvnPackaging(){
		return this.properties.getProperty(ProjectProperties.MVN_PACKAGING.getPropName(), ProjectProperties.MVN_PACKAGING.getDefaultValue());
	}

	/**
	 * Returns the MVN name
	 * @return MVN name
	 */
	public String getMvnName(){
		return this.properties.getProperty(ProjectProperties.MVN_NAME.getPropName());
	}

	/**
	 * Returns the MVN description
	 * @return MVN description
	 */
	public String getMvnDescription(){
		return this.properties.getProperty(ProjectProperties.MVN_DESCRIPTION.getPropName());
	}

	/**
	 * Returns the MVN url
	 * @return MVN url
	 */
	public String getMvnUrl(){
		return this.properties.getProperty(ProjectProperties.MVN_URL.getPropName());
	}

	/**
	 * Returns the MVN inceptionYear
	 * @return MVN inceptionYear
	 */
	public String getMvnInceptionYear(){
		return this.properties.getProperty(ProjectProperties.MVN_INCEPTION_YEAR.getPropName());
	}

	/**
	 * Returns the project dependencies that have been coordinated with other managed projects or build version of the master project
	 * @return project dependencies
	 */
	public Collection<Model_Dependency> getDependencies(){
		return this.dependencies;
	}

	/**
	 * Returns the MVN organization name
	 * @return MVN organization name
	 */
	public String getMvnOrgName(){
		return this.properties.getProperty(ProjectProperties.MVN_ORGANIZATION_NAME.getPropName());
	}

	/**
	 * Returns the MVN organization url
	 * @return MVN organization url
	 */
	public String getMvnOrgUrl(){
		return this.properties.getProperty(ProjectProperties.MVN_ORGANIZATION_URL.getPropName());
	}

	public boolean doesBundleDocs(){
		return (this.properties.getProperty(ProjectProperties.PM_DO_BUNDLE_DOC.getPropName())!=null)?true:false;
	}

	/**
	 * Returns the MVN property for compiler source
	 * @return compiler source property
	 */
	public String getMvnCompilerSource(){
		return this.properties.getProperty(ProjectProperties.MVN_PROPERTIES_COMPILER_SOURCE.getPropName());
	}

	/**
	 * Returns the MVN property for compiler target
	 * @return compiler target property
	 */
	public String getMvnCompilerTarget(){
		return this.properties.getProperty(ProjectProperties.MVN_PROPERTIES_COMPILER_TARGET.getPropName());
	}

	/**
	 * Returns the MVN property for encoding
	 * @return encoding property
	 */
	public String getMvnEncoding(){
		return this.properties.getProperty(ProjectProperties.MVN_PROPERTIES_ENCODING.getPropName());
	}

	/**
	 * Returns the registered licenses of the project
	 * @return project licenses
	 */
	public Set<Licenses> getLicenses(){
		return this.licenses;
	}

	/**
	 * Returns other project files found in the pm direcotry
	 * @return other project files
	 */
	public Map<ProjectFiles, File> getOtherProjectFiles(){
		return this.otherProjectFiles;
	}

	/**
	 * Returns the validated plugin files of the project.
	 * @return validated plugin files
	 */
	public Set<File> getPlugins(){
		return this.plugins;
	}

	/**
	 * Returns the MVN property for issue management -url.
	 * @return issue management -url
	 */
	public String getMvnIssueMgmtUrl(){
		return this.properties.getProperty(ProjectProperties.MVN_ISSUE_MANAGEMENT_URL.getPropName());
	}

	/**
	 * Returns the MVN property for issue management -system.
	 * @return issue management -system
	 */
	public String getMvnIssueMgmtSystem(){
		return this.properties.getProperty(ProjectProperties.MVN_ISSUE_MANAGEMENT_SYSTEM.getPropName());
	}

	/**
	 * Returns the MVN property for SCM developer connection.
	 * @return SCM developer connection
	 */
	public String getMvnScmDeveloperConnection(){
		return this.properties.getProperty(ProjectProperties.MVN_SCM_DEVELOPER_CONNECTION.getPropName());
	}

	/**
	 * Returns the MVN property for SCM connection.
	 * @return SCM connection
	 */
	public String getMvnScmConnection(){
		return this.properties.getProperty(ProjectProperties.MVN_SCM_CONNECTION.getPropName());
	}

	/**
	 * Returns the MVN property for SCM url.
	 * @return SCM url
	 */
	public String getMvnScmUrl(){
		return this.properties.getProperty(ProjectProperties.MVN_SCM_URL.getPropName());
	}

	/**
	 * Flag for requiring the jar plugin.
	 * @return true if required, false otherwise
	 */
	public boolean wantsJarPLugin(){
		return (this.properties.getProperty(ProjectProperties.PM_USE_JAR_PLUGIN.getPropName())!=null)?true:false;
	}

	/**
	 * Flag for requiring the source plugin.
	 * @return true if required, false otherwise
	 */
	public boolean wantsSourcePlugin(){
		return (this.properties.getProperty(ProjectProperties.PM_USE_SRC_PLUGIN.getPropName())!=null)?true:false;
	}

	/**
	 * Flag for requiring the compiler plugin.
	 * @return true if required, false otherwise
	 */
	public boolean wantsCompilerPlugin(){
		return (this.properties.getProperty(ProjectProperties.PM_USE_COMPILER_PLUGIN.getPropName())!=null)?true:false;
	}

	/**
	 * Flag for requiring the Javadoc plugin.
	 * @return true if required, false otherwise
	 */
	public boolean wantsJavadocPlugin(){
		return (this.properties.getProperty(ProjectProperties.PM_USE_JAVADOC_PLUGIN.getPropName())!=null)?true:false;
	}
}
