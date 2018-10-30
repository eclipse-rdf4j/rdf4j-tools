/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.assertj.core.api.AbstractBooleanAssert;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;

public class ConsoleIOTest {

	private ConsoleIO io;
	private LineReader reader;
	
	@Before
	public void initConsoleObject() throws IOException {
		InputStream input = mock(InputStream.class);
		OutputStream out =  mock(OutputStream.class);
		Terminal terminal = TerminalBuilder.builder().streams(input, out).build();
		
		reader = mock(LineReader.class);
		ConsoleState info = mock(ConsoleState.class);

		io = new ConsoleIO(terminal, reader, info);
	}

	@Test
	public void shouldSetErrorWrittenWhenErrorsAreWritten() {
		io.writeError(null);
		assertThat(io.wasErrorWritten()).isTrue();
	}

	@Test
	public void shouldSetErroWrittenOnParserError() {
		io.writeParseError("", 0, 0, "");
		assertThat(io.wasErrorWritten()).isTrue();
	}

	@Test
	public void shouldSetErroWrittenOnWriteUnoppenedError() {
		io.writeUnopenedError();
		assertThat(io.wasErrorWritten()).isTrue();
	}
	
	@Test
	public void shouldNotEscapeCharacters() throws IOException {
		String qry = "SELECT ?r WHERE {?s ?p ?o . BIND(REPLACE(?o, '\\.', 'x') AS ?r) }";
		
		when(reader.readLine("> ")).thenReturn(qry).thenReturn(".");
		
		assertThat(qry).isEqualTo(io.readMultiLineInput());
	}
}
