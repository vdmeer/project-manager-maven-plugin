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
 * Constant definitions for the plugin, such as standard file names.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.1 build 160301 (01-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public abstract class PmConstants {

	/**
	 * The file that defines the projects to be managed.
	 * The file is essentially a Java property file.
	 * The keys should be the names of the projects, but they are largely ignored.
	 * The values are the project directories, either absolute or relative from the current directory.
	 */
	public static final String PROJECTS_FILE = "projects.pm";

	/**
	 * The file that provides details about external dependencies for dependency coordination.
	 * The file is essentially a Java property file.
	 * The key should be used in managed projects to point to a described dependency.
	 * The value should contain the three important aspects, in order, separated by whitespace(s):
	 * <ul>
	 * 		<li>groupID</li>
	 * 		<li>artifactID</li>
	 * 		<li>version</li>
	 * </ul>
	 * For instance, for a dependency identified by the key 'commons-lang3' the entry in this file should be:
	 * <pre>
commons-lang3=org.apache.commons commons-lang3 3.4
	 * </pre>
	 */
	public static final String BUILD_VERSIONS_FILE = "build-versions.pm";

	/**
	 * Standard path to project specific PM property and configuration files.
	 * The file names are named in {@link ProjectFiles}.
	 */
	public static final String PROJECT_PM_PATH = "src/pm";

	/**
	 * Filename of a file containing plugin definition for a Jar plugin.
	 */
	public static final String JAR_PLUGIN_FILE = "plugin-jar.pm";

	/**
	 * Filename of a file containing plugin definition for a Javadoc plugin.
	 */
	public static final String JAVADOC_PLUGIN_FILE = "plugin-javadoc.pm";

	/**
	 * Filename of a file containing plugin definition for a source plugin.
	 */
	public static final String SOURCE_PLUGIN_FILE = "plugin-sourcejar.pm";

	/**
	 * Filename of a file containing plugin definition for a compiler plugin.
	 */
	public static final String COMPILER_PLUGIN_FILE = "plugin-compiler.pm";

	/**
	 * Filename of a file containing plugin definition for compiling bundle documentation.
	 */
	public static final String BUNDLEDOC_PLUGIN_FILE = "plugin-bundledoc.pm";

	/**
	 * Filename of a file containing plugin definition for adding bundle documentation to the main jar.
	 */
	public static final String BUNDLEDOC_RESOURCE_FILE = "plugin-bundledoc-resource.pm";

}
