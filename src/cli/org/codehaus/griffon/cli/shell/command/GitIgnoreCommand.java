package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Command;

import java.util.List;

@Command(scope = "git",
        name = "git-ignore",
        description = "Add .gitignore patterns")
public class GitIgnoreCommand extends AbstractGriffonCommand {
    @Argument(index = 0,
            name = "patterns",
            description = "Patterns to be added to .gitgnore",
            required = false,
            multiValued = true)
    private List<String> patterns;
}