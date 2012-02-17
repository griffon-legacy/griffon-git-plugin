package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "git",
        name = "git-push",
        description = "Update remote refs along with associated objects.")
public class GitPushCommand extends AbstractGriffonCommand {
    @Argument(index = 0,
            name = "remote",
            description = "The \"remote\" repository that is destination of a push operation. This parameter can be " +
                    "either a URL or the name of a remote.",
            required = false)
    private String remote;

    @Option(name = "--all",
            description = "Instead of naming each ref to push, specifies that all refs under refs/heads/ be pushed.",
            required = false)
    private boolean all;

    @Option(name = "--tags",
            description = "All refs under refs/tags are pushed, in addition to refspecs explicitly listed on the command line.",
            required = false)
    private boolean tags;

    @Option(name = "--force",
            aliases = "-f",
            description = "Usually, the command refuses to update a remote ref that is not an ancestor of the local ref " +
                    "used to overwrite it. This flag disables the check. This can cause the remote repository to lose " + "" +
                    "commits; use it with care.",
            required = false)
    private boolean force;
}