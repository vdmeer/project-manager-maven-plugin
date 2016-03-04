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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import de.vandermeer.skb.mvn.PmConstants;

/**
 * Maven wrapper for the {@link ProjectManager} creating a maven plugin.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2-SNAPSHOT build 160304 (04-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
@Mojo(
		name = "project-manager",
		defaultPhase = LifecyclePhase.INITIALIZE,
		requiresDependencyResolution = ResolutionScope.COMPILE,
		requiresProject = true
)
public class MvnProjectManager extends AbstractMojo {

	/** The configuration directory, home of all files with properties. */
	@Parameter (defaultValue = PmConstants.PROJECT_PM_PATH)
	private File configDir;

	/** The directory where the projects store their properties and configurations. */
	@Parameter (defaultValue = PmConstants.PROJECT_PM_PATH)
	private String projectPmDir;

	/** The current Maven project. */
	@Parameter(property = "project", required = true, readonly = true)
	protected MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		ProjectManager pm = null;
		try{
			pm = new ProjectManager(this.configDir, this.projectPmDir);
		}
		catch(Exception ex){
			getLog().error("- " + ex.getMessage());
			throw new MojoFailureException(ex.getMessage());
		}

		getLog().info("projectes file: " + this.configDir.toString() + File.separator + PmConstants.PROJECTS_FILE);

		//load the model, i.e. see what projects we manage and then load each project
		try{
			pm.loadModel();
		}
		catch(Exception ex){
			getLog().error("- " + ex.getMessage());
			throw new MojoFailureException(ex.getMessage());
		}
		getLog().info("standard PM dir: " + projectPmDir);

//		getLog().info("## : " + projects.keySet().toString());
//		getLog().info("@@ : " + projects.values().toString());

		try{
			String written = pm.writeModel();
			if(written.length()>0){
				getLog().info("did write POM file(s), see below\n" + written);
			}
			else{
				getLog().info("created POM files do not differ from existing, nothing changed");
			}
		}
		catch(Exception ex){
			getLog().error("- " + ex.getMessage());
			throw new MojoFailureException(ex.getMessage());
		}
	}
}
