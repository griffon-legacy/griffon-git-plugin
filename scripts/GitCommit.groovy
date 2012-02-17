/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

import griffon.plugins.git.GitManager
import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.api.errors.JGitInternalException

/**
 * @author Andres Almiray
 */

gitManager = null

target(name: 'git-commit',
        description: "Record changes to the repository",
        prehook: null, posthook: null) {
    if(!gitManager) gitManager = new GitManager()
    CommitCommand cmd = gitManager.git().commit()

    if (!argsMap.message) {
        event 'StatusError', ['You must specify a commit message!']
        exit 1
    }

    cmd.message = argsMap.message
    cmd.all = argsMap.all ?: false
    cmd.amend = argsMap.amend ?: false
    cmd.author = gitManager.author
    cmd.committer = gitManager.committer

    try {
        cmd.call()
    } catch (x) {
        if (x instanceof JGitInternalException) {
            x = x.cause
        }
        event 'StatusError', ["Could not commit refs: $x"]
    }

    String currentBranch = gitManager.git().repository.branch
    String currentHash = gitManager.git().repository.getRef('HEAD').objectId.name()

    println "[${currentBranch} ${currentHash[0..7]}] ${argsMap.message}"
}

setDefaultTarget('git-commit')
