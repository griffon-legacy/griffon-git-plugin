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
import org.eclipse.jgit.api.Status
import org.eclipse.jgit.api.StatusCommand
import static griffon.util.GriffonNameUtils.capitalize

/**
 * @author Andres Almiray
 */

target(name: 'git-status',
        description: "Show the working tree status",
        prehook: null, posthook: null) {
    GitManager gitManager = new GitManager()
    StatusCommand cmd = gitManager.git().status()

    Status status = cmd.call()

    String currentBranch = gitManager.git().repository.branch
    println "# On branch ${currentBranch}"
    ['added', 'removed', 'changed', 'modified', 'conflicting', 'untracked', 'missing'].each() { section -> printSection(status, section) }
}

setDefaultTarget('git-status')

printSection = { Status status, String section ->
    Set<String> files = status[section]
    if (!files) return
    println '#'
    println "# ${capitalize(section)} files:"
    files.sort().each { file ->
        println "#     $file"
    }
}