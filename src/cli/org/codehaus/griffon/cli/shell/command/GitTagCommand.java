package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "git",
        name = "git-tag",
        description = "Create, list, or delete a tag object")
public class GitTagCommand extends AbstractGriffonCommand {
    @Argument(index = 0,
            name = "name",
            description = "The name of the tag to create/delete.",
            required = false)
    private String name;

    @Option(name = "--delete",
            aliases = "-d",
            description = "Delete existing tag with the given names.",
            required = false)
    private boolean delete;

    @Option(name = "--force",
            aliases = "-f",
            description = "Replace an existing tag with the given name (instead of failing).",
            required = false)
    private boolean force;
}