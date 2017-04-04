<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-javadoc-plugin</artifactId>
	<version>${pm.version.maven-javadoc-plugin}</version>

 	<reportSets>
		<reportSet>
			<id>default</id>
			<reports>
				<report>javadoc</report>
			</reports>
		</reportSet>
	</reportSets>
	<configuration>
		<destDir>javadocs/api-release</destDir>
	</configuration>
</plugin>
