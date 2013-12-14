/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.types.BytesType;
import com.mysema.query.sql.types.DateTimeType;
import com.mysema.query.sql.types.LocalDateType;
import com.mysema.query.sql.types.LocalTimeType;

public class MetadataExportMojoTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();

    private final MavenProject project = new MavenProject();

    private final MetadataExportMojo mojo = new MetadataExportMojo();

    @Before
    public void setUp() {
        mojo.setProject(project);
        mojo.setJdbcDriver("org.h2.Driver");
        mojo.setJdbcUrl(url);
        mojo.setJdbcUser("sa");
        mojo.setNamePrefix("Q"); // default value
        mojo.setPackageName("com.example");

    }

    @Test
    public void Execute() throws Exception {
        mojo.setTargetFolder("target/export");
        mojo.execute();

        assertEquals(Collections.singletonList("target/export"), project.getCompileSourceRoots());
        assertTrue(new File("target/export").exists());

    }

    @Test
    public void Execute_With_CustomTypes() throws Exception {
        mojo.setTargetFolder("target/export2");
        mojo.setCustomTypes(new String[]{BytesType.class.getName()});
        mojo.execute();

        assertEquals(Collections.singletonList("target/export2"), project.getCompileSourceRoots());
        assertTrue(new File("target/export2").exists());
    }

    @Test
    public void Execute_With_JodaTypes() throws Exception {
        mojo.setTargetFolder("target/export3");
        mojo.setCustomTypes(new String[]{LocalDateType.class.getName(), LocalTimeType.class.getName(), DateTimeType.class.getName()});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export3"), project.getCompileSourceRoots());
        assertTrue(new File("target/export3").exists());
    }

    @Test
    public void Execute_With_TypeMappings() throws Exception {
        mojo.setTargetFolder("target/export4");
        TypeMapping mapping = new TypeMapping();
        mapping.table = "CATALOGS";
        mapping.column = "CATALOG_NAME";
        mapping.type = Object.class.getName();
        mojo.setTypeMappings(new TypeMapping[]{mapping});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export4"), project.getCompileSourceRoots());
        assertTrue(new File("target/export4").exists());
    }

    @Test
    public void ExecuteWithNumericMappings() throws Exception {
        mojo.setTargetFolder("target/export5");
        NumericMapping mapping = new NumericMapping();
        mapping.size = 1;
        mapping.digits = 1;
        mapping.javaType = Number.class.getName();
        mojo.setNumericMappings(new NumericMapping[]{mapping});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export5"), project.getCompileSourceRoots());
        assertTrue(new File("target/export5").exists());
    }

}
