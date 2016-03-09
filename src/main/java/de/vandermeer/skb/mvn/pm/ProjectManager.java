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

package de.vandermeer.skb.mvn.pm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import de.vandermeer.skb.mvn.PmConstants;
import de.vandermeer.skb.mvn.pm.model.PM_Context;
import de.vandermeer.skb.mvn.pm.model.PM_Model;
import de.vandermeer.skb.mvn.pm.model.PomWriter;

/**
 * Manages MVN projects.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 160306 (06-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class ProjectManager {

	/** The configuration directory, home of all files with properties. */
	private final File configDir;

	/** The directory where the projects store their properties and configurations. */
	private final String projectPmDir;

	/** Model context, everything the model needs to know. */
	protected final PM_Context mc;

	/** The projects found in the main projects file. */
	Properties projects;

	/** The PM model. */
	protected PM_Model model;

	/**
	 * Creates a new project manager.
	 * @param configDir the project manager configuration directory
	 * @param projectPmDir the standard directory for project configurations
	 * @throws NullPointerException if any argument was null or if any load operation did run into null pointers
	 * @throws IllegalArgumentException if any argument was blank or if any load operation did run into illegal arguments
	 */
	public ProjectManager(File configDir, String projectPmDir){
		Validate.notNull(configDir);
		Validate.notBlank(projectPmDir);

		this.configDir = configDir;
		this.projectPmDir = projectPmDir;

		this.testConfigDir();
		this.projects = this.loadProjectDirectories();
		this.mc = new PM_Context(this.projectPmDir);
		this.loadDependencyVersions();
		this.loadPluginVersions();
	}

	/**
	 * Tests the configuration directory.
	 * @throws IllegalArgumentException if any test operation failed
	 */
	protected void testConfigDir(){
		//check if configuration directory exists
		if(!this.configDir.isDirectory()){
			throw new IllegalArgumentException("configuration directory not a directory - <" + this.configDir + ">");
		}
		if(!this.configDir.canRead()){
			throw new IllegalArgumentException("cannot read from configuration directory <" + this.configDir + ">");
		}
	}

	/**
	 * Loads project properties from their project directory.
	 * @return the loaded properties
	 * @throws IllegalArgumentException if any load operation failed (for instance file not found, IO error)
	 */
	protected Properties loadProjectDirectories(){
		//check for the main configure file
		File projectsFile = new File(this.configDir.toString() + File.separator + PmConstants.PROJECTS_FILE);
		this.projects = new Properties();
		try {
			projects.load(new FileReader(projectsFile));
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("did not find projects file, tried <" + projectsFile + ">");
		}
		catch (IOException ioex) {
			throw new IllegalArgumentException("could not read projects file, got IOException <" + ioex.getMessage() + ">");
		}
		if(projects.isEmpty()){
			throw new IllegalArgumentException("empty projects file <" + projectsFile + ">");
		}

		return projects;
	}

	/**
	 * Loads the dependency version file of the main project.
	 * @throws IllegalArgumentException if any load operation failed (for instance file not found, IO error)
	 */
	protected void loadDependencyVersions(){
		//if dependency versions are defined, load them
		File dependencyVersionFile = new File(this.configDir.toString() + File.separator + PmConstants.DEPENDENCY_VERSIONS_FILE);
		try {
			Properties dependencyVersions = new Properties();
//			getLog().info("dependency versions file: " + dependencyVersionFile);
			dependencyVersions.load(new FileReader(dependencyVersionFile));
			if(dependencyVersions.size()>0){
				this.mc.setDependencyVersions(dependencyVersions);
			}
		}
		catch (FileNotFoundException e) {
//			getLog().warn("- cannot do dependency coordination, dependency-versions file does not exist: <" + dependencyVersionFile + ">");
		}
		catch (IOException ioex) {
			throw new IllegalArgumentException("could not read existing dependency-versions file, got IOException <" + ioex.getMessage() + ">");
		}
		catch(Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * Loads the plugin version file of the main project.
	 * @throws IllegalArgumentException if any load operation failed (for instance file not found, IO error)
	 */
	protected void loadPluginVersions(){
		//if plugin versions are defined, load them
		File pluginVersionFile = new File(this.configDir.toString() + File.separator + PmConstants.PLUGIN_VERSIONS_FILE);
		try {
			Properties pluginVersions = new Properties();
//			getLog().info("plugin versions file: " + pluginVersionFile);
			pluginVersions.load(new FileReader(pluginVersionFile));
			if(pluginVersions.size()>0){
				this.mc.setPluginVersions(pluginVersions);
			}
		}
		catch (FileNotFoundException e) {
//			getLog().warn("- cannot do plugin coordination, plugin-versions file does not exist: <" + pluginVersionFile + ">");
		}
		catch (IOException ioex) {
			throw new IllegalArgumentException("could not read existing plugin-versions file, got IOException <" + ioex.getMessage() + ">");
		}
		catch(Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * Loads the actual model with all projects and all their properties.
	 * @throws NullPointerException if any load operation did run into a null pointer problem
	 * @throws IllegalArgumentException if any load operation failed (for instance file not found, IO error)
	 */
	public void loadModel(){
		this.model = new PM_Model(this.mc);

		try{
			this.model.loadModel(projects.values());
			this.model.updateProjectDependencies();
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * Writes the model to generated POM files, then checks if the actual POM file needs an update.
	 * @return information about what POM files where written, empty if none where written (in that case, w/o exceptions, the new generated POM files did not differ from the existing POM files)
	 * @throws NullPointerException if any null pointer happened
	 * @throws IllegalArgumentException if any problems happened with creating, writing, copying POM files
	 */
	public String writeModel(){
		try{
			PomWriter pw = new PomWriter();
			return pw.writePoms(this.model.getManagedProjects());
		}
		catch(Exception ex){
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
