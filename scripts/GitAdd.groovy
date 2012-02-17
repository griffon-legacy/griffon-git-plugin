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
import org.eclipse.jgit.api.AddCommand

/**
 * @author Andres Almiray
 */

target(name: 'git-add',
        description: "Add file contents to the index",
        prehook: null, posthook: null) {
    GitManager gitManager = new GitManager()
    AddCommand cmd = gitManager.git().add()
    if (argsMap.params) {
        argsMap.params.each { pattern ->
            println "... adding $pattern to staging area"
            cmd.addFilepattern(pattern)
        }
        cmd.call()
    } else {
        event 'StatusError', ['Command git-add was invoked without arguments.']
    }
}

setDefaultTarget('git-add')
