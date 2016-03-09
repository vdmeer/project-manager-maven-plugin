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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.tuple.Pair;

import de.vandermeer.skb.mvn.ProjectFiles;

/**
 * Loads settings and information into a project model.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2 build 160306 (06-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class ModelLoader {

	/** Model context, everything the model needs to know. */
	protected final PM_Context mc;

	/** The loaded (checked) map of project directory to a pair with project PM directory and main configuration file. */
	protected final Map<File, Pair<File, File>> projectFiles;

	/**
	 * Creates a new model loader.
	 * @param mc model context
	 * @param folders collection of project folders
	 * @throws NullPointerException if the collection or any member was null
	 * @throws IllegalArgumentException if any collection member resulted in a was a blank string or pointed to an unreadable/unwritable directory
	 */
	public ModelLoader(PM_Context mc, Collection<Object> folders){
		Validate.notNull(mc);
		this.mc = mc;

		this.projectFiles = new LinkedHashMap<>();

		StrBuilder errors = new StrBuilder();
		StrBuilder _err;
		for(Object o : folders){
			File prjDir = new File(o.toString());

			//set the loaded map entry
			this.projectFiles.put(prjDir,
					Pair.of(new File(
							prjDir + File.separator + this.mc.getProjectPmDir()),
							new File(prjDir + File.separator + this.mc.getProjectPmDir() + File.separator + ProjectFiles.MANAGED_PROJECT_PROPERTIES.getFileName())
					)
			);

			_err = this.testProjectDirectory(prjDir);
			errors.append(_err);
			if(_err.size()==0){
				_err = this.testProjectPmDirectory(this.projectFiles.get(prjDir).getKey());
				errors.append(_err);
				if(_err.size()==0){
					_err = this.testProjectPmFiles(this.projectFiles.get(prjDir).getValue());
					errors.append(_err);
				}
			}
		}

		if(errors.size()>0){
			throw new IllegalArgumentException("pm loader: problems with one or more directories, see below\n" + errors.toString());
		}
	}

	/**
	 * Tests the project directory.
	 * @param prjDir the project directory to test
	 * @return errors if any occurred, empty if all was ok
	 */
	protected StrBuilder testProjectDirectory(File prjDir){
		StrBuilder errors = new StrBuilder();

		//do individual texts to provide specific error messages (is there are better way to do that?)
		if(!prjDir.exists()){
			errors.append("project directory does not exist: <").append(prjDir).append('>').appendNewLine();
		}
		else if(!prjDir.isDirectory()){
			errors.append("project directory is not a directory: <").append(prjDir).append('>').appendNewLine();
		}
		else if(!prjDir.canRead()){
			errors.append("project directory not readable: <").append(prjDir).append('>').appendNewLine();
		}
		else if(!prjDir.canWrite()){
			errors.append("project directory not writable: <").append(prjDir).append('>').appendNewLine();
		}

		return errors;
	}

	/**
	 * Tests the PM directory in the project.
	 * @param pmDir PM directory to test
	 * @return errors if any occurred, empty if all was ok
	 */
	protected StrBuilder testProjectPmDirectory(File pmDir){
		StrBuilder errors = new StrBuilder();

		//do individual texts to provide specific error messages (is there are better way to do that?)
		if(!pmDir.exists()){
			errors.append("PM directory does not exist: <").append(pmDir).append('>').appendNewLine();
		}
		else if(!pmDir.isDirectory()){
			errors.append("PM directory is not a directory: <").append(pmDir).append('>').appendNewLine();
		}
		else if(!pmDir.canRead()){
			errors.append("PM directory not readable: <").append(pmDir).append('>').appendNewLine();
		}

		return errors;
	}

	/**
	 * Tests a PM project property file.
	 * @param pp the file to test
	 * @return errors if any occurred, empty if all was ok
	 */
	protected StrBuilder testProjectPmFiles(File pp){
		StrBuilder errors = new StrBuilder();

		if(!pp.exists()){
			errors.append("project property file does not exist: <").append(pp).append('>').appendNewLine();
		}
		else if(!pp.canRead()){
			errors.append("cannot read project property file: <").append(pp).append('>').appendNewLine();
		}

		return errors;
	}

	/**
	 * Returns the map of loaded project files
	 * @return loaded project files map
	 */
	public Map<File, Pair<File, File>> getProjectFiles(){
		return this.projectFiles;
	}
}
