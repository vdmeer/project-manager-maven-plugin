package de.vandermeer.skb.mvn.pm;

import java.io.File;

import org.junit.Test;

public class Test_EclRun_PM {

	@Test
	public void test_Run(){
		if("true".equals(System.getProperty("EclRun"))){
			ProjectManager pm = new ProjectManager(new File("V:/dev/github/skb-java/mvn/project-manager/src/test/resources/pm"), "src/bundle/pm");
			pm.loadModel();
			pm.writeModel();
		}
	}
}
