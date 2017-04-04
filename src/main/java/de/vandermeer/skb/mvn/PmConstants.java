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
 * @version    v0.0.3 build 170404 (04-Apr-17) for Java 1.8
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
	public static final String DEPENDENCY_VERSIONS_FILE = "dependency-versions.pm";

	/**
	 * The file that provides version information used in plugins.
	 * The file is essentially a Java property file.
	 * The key is the Maven property used in the plugin definition.
	 * The value is the version.
	 * Dependencies that are defined within a plugin definition should be in here.
	 */
	public static final String PLUGIN_VERSIONS_FILE = "plugin-versions.pm";

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
	 * Filename of a file containing profile with definition for a Javadoc jar profile.
	 */
	public static final String JAVADOC_JAR_PROFILE_FILE = "profile-jdjar.pm";

	/**
	 * Filename of a file containing profile with definition for a Javadoc jar profile using an AsciiDoctor doclet.
	 */
	public static final String JAVADOC_ADOC_JAR_PROFILE_FILE = "profile-jdjar-adoc.pm";

	/**
	 * Filename of a file containing profile with definition for a source jar profile.
	 */
	public static final String SRC_JAR_PROFILE_FILE = "profile-srcjar.pm";

	/**
	 * Filename of a file containing plugin definition for a compiler plugin.
	 */
	public static final String COMPILER_PLUGIN_FILE = "plugin-compiler.pm";

	/**
	 * Filename of a file containing profile definition for compiling and copying bundle documentation (as profile).
	 */
	public static final String BUNDLEDOC_PROFILE_FILE = "profile-bundledoc.pm";

	/**
	 * Filename of a file containing plugin definition for the maven site plugin.
	 */
	public static final String SITE_PLUGIN_FILE = "plugin-site.pm";
}
