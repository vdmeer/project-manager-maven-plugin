<profile>
	<id>env-bdoc</id>
	<build>
		<plugins>
			<!-- Create HTML versions of Changelog/Readme -->
			<plugin>
				<groupId>org.asciidoctor</groupId>
				<artifactId>asciidoctor-maven-plugin</artifactId>
				<version>${pm.version.asciidoctor-maven-plugin}</version>
				<executions>
					<execution>
						<id>bundle-asciidoc</id>
						<phase>process-resources</phase>
						<configuration>
							<sourceDirectory>src/bundle/doc</sourceDirectory>
							<outputDirectory>./</outputDirectory>
							<sourceHighlighter>coderay</sourceHighlighter>
							<backend>html</backend>
							<attributes>
								<toc>true</toc>
								<baseDir>${project.basedir}/src</baseDir>
							</attributes>
						</configuration>
						<goals>
							<goal>process-asciidoc</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

			<!-- Copy Changelog/Readme and then include in jar with License -->
			<plugin>
				<artifactId>maven-resources-plugin</artifactId>
				<version>${pm.version.maven-resources-plugin}</version>
				<executions>
					<execution>
						<id>copy-bundle-docs</id>
						<phase>generate-resources</phase>
						<goals><goal>copy-resources</goal></goals>
						<configuration>
							<outputDirectory>./</outputDirectory>
							<resources>
								<resource>
									<directory>src/bundle/doc</directory>
									<filtering>true</filtering>
								</resource>
							</resources>
						</configuration>
					</execution>
					<execution>
						<id>copy-to-jar</id>
						<phase>prepare-package</phase>
						<goals><goal>copy-resources</goal></goals>
						<configuration>
							<outputDirectory>${basedir}/target/classes</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}</directory>
									<includes>
										<include>LICENSE</include>
										<include>CHANGELOG*</include>
										<include>README*</include>
									</includes>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</profile>