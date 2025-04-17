package org.gbl.in;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "get", mixinStandardHelpOptions = true, description = "Get a contact by id")
public class GetContact implements Callable<Integer> {

    @Parameters(index = "0", description = "The contact id to retrieve.")
    private String id;

    @Override
    public Integer call() {
        System.out.println("get contact with id: " + id);
        return 0;
    }
}
