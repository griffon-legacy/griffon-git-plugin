package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "git",
        name = "git-release",
        description = "Commits, tags and pushes a new release.")
public class GitReleaseCommand extends AbstractGriffonCommand {
    @Option(name = "--message",
            aliases = "-m",
            description = "Use the given <msg> as the commit message.",
            required = true)
    private String message;
}