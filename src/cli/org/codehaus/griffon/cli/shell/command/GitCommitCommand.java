package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Command;
import org.codehaus.griffon.cli.shell.Option;

@Command(scope = "git",
        name = "git-commit",
        description = "Record changes to the repository")
public class GitCommitCommand extends AbstractGriffonCommand {
    @Option(name = "--message",
            aliases = "-m",
            description = "Use the given <msg> as the commit message.",
            required = true)
    private String message;

    @Option(name = "--all",
            aliases = "-a",
            description = "Tell the command to automatically stage files that have been modified and deleted, " +
                    "but new files you have not told git about are not affected.",
            required = false)
    private boolean all;

    @Option(name = "--amend",
            description = "Used to amend the tip of the current branch. Prepare the tree object you would want to replace " +
                    "the latest commit as usual (this includes the usual -i/-o and explicit paths), " +
                    "and the commit log editor is seeded with the commit message from the tip of the current branch. " +
                    "The commit you create replaces the current tip -- if it was a merge " +
                    "it will have the parents of the current tip as parents -- so the current top commit is discarded.",
            required = false)
    private boolean amend;
}