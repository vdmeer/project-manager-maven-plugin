<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>${pm.version.maven-javadoc-plugin}</version>
    <executions>
        <execution>
            <id>attach-javadocs</id>
            <goals>
                <goal>jar</goal>
            </goals>
            <configuration>
                <encoding>${project.resources.sourceEncoding}</encoding>
                <docencoding>${project.resources.sourceEncoding}</docencoding>
                <charset>${project.resources.sourceEncoding}</charset>
                <aggregate>true</aggregate>
                <overview>${basedir}/src/main/javadoc/overview.html</overview>
                <nohelp>true</nohelp>
                <header>${project.name}: ${project.version}</header>
                <footer>${project.name}: ${project.version}</footer>
                <doctitle>${project.name}: ${project.version}</doctitle>
                <noqualifier>all</noqualifier>
                <detectLinks>true</detectLinks>
                <detectJavaApiLink>true</detectJavaApiLink>
                <javadocDirectory>${basedir}/src/main/javadoc</javadocDirectory>
                <docfilessubdirs>true</docfilessubdirs>
            </configuration>
        </execution>
    </executions>
</plugin>