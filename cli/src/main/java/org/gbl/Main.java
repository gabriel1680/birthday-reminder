package org.gbl;

import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Help.ColorScheme;

public class Main {
    public static void main(String[] args) {
        ColorScheme colorScheme = new ColorScheme.Builder()
                .commands(Style.bold, Style.underline)
                .options(Style.fg_yellow)
                .parameters(Style.fg_yellow)
                .optionParams(Style.italic)
                .errors(Style.fg_red, Style.bold)
                .stackTraces(Style.italic)
                .build();
        final var commandLine = new CommandLine(new BReminder());
        int status = commandLine
                .setColorScheme(colorScheme)
                .execute("create", "-h");
        System.exit(status);
    }
}