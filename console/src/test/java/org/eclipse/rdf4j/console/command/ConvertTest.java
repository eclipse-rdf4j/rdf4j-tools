/*******************************************************************************
 * Copyright (c) 2018 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.console.command;

import com.github.jsonldjava.utils.JsonUtils;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.rdf4j.RDF4JException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Bart Hanssens
 */
public class ConvertTest extends AbstractCommandTest {

	private Convert convert;
	private File from;
	
	@Rule
	public final TemporaryFolder LOCATION = new TemporaryFolder();

	@Before
	public void prepare() throws IOException, RDF4JException {
		when(mockConsoleIO.askProceed("File exists, continue ?", false)).thenReturn(Boolean.TRUE);
		convert = new Convert(mockConsoleIO, mockConsoleState);
		
		from = LOCATION.newFile("alien.ttl");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = classLoader.getResourceAsStream("convert/alien.ttl")) {
			Files.copy(is, from.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	@After
	@Override
	public void tearDown() {
		from.delete();
	}
	
	@Test
	public final void testConvert() throws IOException {
		File json = LOCATION.newFile("alien.jsonld");
		//json.delete(); // only the location is needed, not a real (empty) file
		
		convert.execute("convert", from.toString(), json.toString());
		
		assertTrue("File is empty", json.length() > 0);
		
		Object o = null;
		try {
			o = JsonUtils.fromInputStream(Files.newInputStream(json.toPath()));
		} catch (IOException ioe) { 
			//
		}
		assertTrue("Invalid JSON", o != null);
	}
	
	@Test
	public final void testConvertParseError() throws IOException {
		File f = LOCATION.newFile("empty.nt");
		File json = LOCATION.newFile("empty.jsonld");
		json.delete(); // only the location is needed, not a real (empty) file
		
		convert.execute("convert", f.toString(), json.toString());
		assertTrue("File not empty", json.length() == 0);
	}
	
	@Test
	public final void testConvertInvalidFormat() throws IOException {
		File qyx = LOCATION.newFile("alien.qyx"); 
		qyx.delete(); // only the location is needed, not a real (empty) file

		convert.execute("convert", from.toString(), qyx.toString());
		verify(mockConsoleIO).writeError("No RDF writer for " + qyx.toString());
	}
}