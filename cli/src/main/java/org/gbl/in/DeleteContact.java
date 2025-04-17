package org.gbl.in;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "delete",
        mixinStandardHelpOptions = true,
        description = "Delete an existent contact by id")
public class DeleteContact implements Callable<Integer> {

    @Parameters(index = "0", description = "The contact id to delete.")
    private String id;

    @Override
    public Integer call() {
        System.out.println("Delete contact with id: " + id);
        return 0;
    }
}
