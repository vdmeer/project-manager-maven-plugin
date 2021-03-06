<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-plugin-plugin</artifactId>
	<version>3.3</version>
	<configuration>
		<!-- see http://jira.codehaus.org/browse/MNG-5346 -->
		<skipErrorNoDescriptorsFound>true</skipErrorNoDescriptorsFound>
	</configuration>
	<executions>
		<execution>
			<id>mojo-descriptor</id>
			<goals>
				<goal>descriptor</goal>
			</goals>
		</execution>
		<execution>
			<id>help-goal</id>
			<goals>
				<goal>helpmojo</goal>
			</goals>
		</execution>
	</executions>
</plugin>
