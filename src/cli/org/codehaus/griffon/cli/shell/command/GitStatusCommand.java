package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;

@Command(scope = "git",
        name = "git-status",
        description = "Show the working tree status")
public class GitStatusCommand extends AbstractGriffonCommand {
}