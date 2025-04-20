package org.gbl;

import org.gbl.in.BReminder;
import org.gbl.in.di.GuiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class CLITest {
    protected CommandLine commandLine;
    protected ByteArrayOutputStream err;
    protected ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));
        System.setOut(new PrintStream(out));
        setCommandLine();
        commandLine.setOut(new PrintWriter(out));
    }

    @AfterEach
    void tearDown() {
        System.setErr(System.err);
        System.setOut(System.out);
    }

    protected void setCommandLine() {
        commandLine = new CommandLine(new BReminder(), new GuiceFactory());
    }
}
