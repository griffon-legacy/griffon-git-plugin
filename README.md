
Integrate a Griffon project with Git
------------------------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/git](http://artifacts.griffon-framework.org/plugin/git)


This plugin provides integration with [Git][1], a free & open source, distributed version control system designed to handle
everything from small to very large projects with speed and efficiency.

Usage
-----
Once installed the plugin will initialize an empty Git repository if none is already available. It provides some basic commands
for working with Git, perhaps the most important one is `git-release` in the context of a plugin or archetype project. Whenever 
a project of any of those types is released (by invoking `release-plugin`, `release-archetype` or just `release`) the plugin will
automatically make a commit with the release message, tag the release and push it to the default remote.

Configuration
-------------

The plugin makes use of the configuration available through the standard Git configuration files, that is `$USER_HOME/.gitconfig`
and `.git/config`, however it allows you to override some of the configuration locally or globally, whether you place it in
`griffon-app/conf/BuildConfig.groovy` or `$USER_HOME/.griffon/settings.groovy`. Here's how the configuration may look like

    git {
        author {
            name  = 'Andres Almiray'
            email = 'aalmiray@yahoo.com'
        }
        repositories {
            origin {
                username   = 'aalmiray'
                password   = '*******'
                passphrase = '*******'
            }
        }
    }

You may define multiple named blocks inside the repositories block. Each one of these blocks defines in turn the credentials
required to push to that particular repository. The `passphrase` property is only needed if connecting to a remote repository
that requires SSH authentication.

This plugin will **NOT** setup SSH keys for you, it expects that configuration step to be already in place. If your intention
is to publish to Github then please follow the instructions detailed in [Github's help][2] page.

### Command flags

It's possible to disable the automatic tagging and pushing of a release when invoking `release-plugin` and/or `release-archetype`.
You would like to do this when testing a release against a local artifact repository for example. Just specifiy the following flag
either at the command line or in the project's settings

    griffon -Dgit.disable.release=true release-plugin

Scripts
-------
 * **git-add** - Add file contents to the index
 * **git-commit** - Record changes to the repository
 * **git-ignore** - Add .gitignore patterns
 * **git-init** - Create an empty git repository or reinitialize an existing one
 * **git-push** - Update remote refs along with associated objects
 * **git-release** - Commits, tags and pushes a new release
 * **git-status** - Show the working tree status
 * **git-tag** - Create, list, or delete a tag object

[1]: http://git-scm.com
[2]: http://help.github.com

