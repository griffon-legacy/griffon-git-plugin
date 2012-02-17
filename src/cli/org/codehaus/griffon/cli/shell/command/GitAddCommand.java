package org.codehaus.griffon.cli.shell.command;

import org.codehaus.griffon.cli.shell.AbstractGriffonCommand;
import org.codehaus.griffon.cli.shell.Argument;
import org.codehaus.griffon.cli.shell.Command;

import java.util.List;

@Command(scope = "git",
        name = "git-add",
        description = "Adds the selected files to the staging area.")
public class GitAddCommand extends AbstractGriffonCommand {
    @Argument(index = 0,
            name = "filePattern",
            description = "Files to add content from. Fileglobs (e.g.  *.c) can be given to add all matching files. " +
                    "Also a leading directory name (e.g.  dir to add dir/file1 and dir/file2) can be given to add all " +
                    "files in the directory, recursively.",
            required = true,
            multiValued = true)
    private List<String> filePattern;
}