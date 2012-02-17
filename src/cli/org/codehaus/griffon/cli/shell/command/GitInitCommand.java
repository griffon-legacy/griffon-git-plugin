package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;

@Command(scope = "git",
        name = "git-init",
        description = "Create an empty git repository or reinitialize an existing one")
public class GitInitCommand extends AbstractGriffonCommand {
}