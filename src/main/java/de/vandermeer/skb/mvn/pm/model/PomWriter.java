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
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import de.vandermeer.skb.mvn.Licenses;
import de.vandermeer.skb.mvn.PmConstants;
import de.vandermeer.skb.mvn.ProjectFiles;

/**
 * Writes POM files to target directory and copies to project folder if POM there differs.
 *
 * @author     Sven van der Meer &lt;vdmeer.sven@mykolab.com&gt;
 * @version    v0.0.2-SNAPSHOT build 160304 (04-Mar-16) for Java 1.8
 * @since      v0.0.1
 */
public class PomWriter {

	/**
	 * Creates a new writer.
	 */
	public PomWriter(){
//		Validate.notNull(mc);
//		this.mc = mc;
	}

	/**
	 * Writes POM files to target folder, test difference with original POM files, copies if the files differ
	 * @param mps the collection of managed projects to process
	 * @return an empty string if no original POM was written, a string with information about written files otherwise
	 */
	@SuppressWarnings("resource")
	public String writePoms(Collection<Model_ManagedProject> mps){
		Validate.notNull(mps);
		Validate.noNullElements(mps);

		File gpf = new File("target/project-manager/generated-pom-files");
		gpf.mkdirs();

		StrBuilder ret = new StrBuilder();
		STGroup stg = new STGroupFile("de/vandermeer/skb/mvn/pm/pom.stg");
		for(Model_ManagedProject mp : mps){
			ST pom = stg.getInstanceOf("pom");
			pom.add("mp", mp);
			if(mp.doesBundleDocs()){
					pom.add("profiles", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/" + PmConstants.BUNDLEDOC_PROFILE_FILE), "UTF-8").useDelimiter("\\A").next());
			}
			if(mp.wantsJarPLugin()){
					pom.add("plugins", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/" + PmConstants.JAR_PLUGIN_FILE), "UTF-8").useDelimiter("\\A").next());
			}
			if(mp.wantsSourcePlugin()){
					pom.add("profiles", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/" + PmConstants.SRC_JAR_PROFILE_FILE), "UTF-8").useDelimiter("\\A").next());
			}
			if(mp.wantsCompilerPlugin()){
					pom.add("plugins", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/" + PmConstants.COMPILER_PLUGIN_FILE), "UTF-8").useDelimiter("\\A").next());
			}
			if(mp.wantsJavadocPlugin()){
				pom.add("profiles", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/" + PmConstants.JAVADOC_JAR_PROFILE_FILE), "UTF-8").useDelimiter("\\A").next());
			}
			if(mp.getLicenses().size()>0){
				for(Licenses l : mp.getLicenses()){
						pom.add("licenses", new Scanner(PomWriter.class.getResourceAsStream("/de/vandermeer/skb/mvn/pm/licenses/" + l.getFileName()), "UTF-8").useDelimiter("\\A").next());
				}
			}
			for(Entry<ProjectFiles, File> pf : mp.getOtherProjectFiles().entrySet()){
				try{
					pom.add(pf.getKey().getStAttribute(), new Scanner(pf.getValue(), "UTF-8").useDelimiter("\\A").next());
				}
				catch(Exception ignore){
					ignore.printStackTrace();
				}
			}
			for(File f : mp.getPlugins()){
				try{
					pom.add("plugins", new Scanner(f, "UTF-8").useDelimiter("\\A").next());
				}
				catch(Exception ignore){
					ignore.printStackTrace();
				}
			}
			for(File f : mp.getProfiles()){
				try{
					pom.add("profiles", new Scanner(f, "UTF-8").useDelimiter("\\A").next());
				}
				catch(Exception ignore){
					ignore.printStackTrace();
				}
			}

			File out = new File(gpf + File.separator + mp.getPmId() + ".pom");
			try{
				FileUtils.write(out, pom.render(), false);
			}
			catch(Exception ignore){
				ignore.printStackTrace();
			}

			File pomFile = new File(mp.baseDir + File.separator +"pom.xml");
			try{
				if(FileUtils.contentEqualsIgnoreEOL(out, pomFile, "UTF-8")==false){
					FileUtils.copyFile(out, pomFile);
					ret.append("writing new pom file: ");
					ret.append(out);
					ret.append(" -> ");
					ret.append(pomFile);
					ret.appendNewLine();
				}
			}
			catch(Exception ignore){
				ignore.printStackTrace();
			}
		}
		return ret.toString();
	}
}
