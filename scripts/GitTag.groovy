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
import org.eclipse.jgit.api.DeleteTagCommand
import org.eclipse.jgit.api.TagCommand
import org.eclipse.jgit.revwalk.RevTag
import org.eclipse.jgit.api.errors.JGitInternalException

/**
 * @author Andres Almiray
 */

gitManager = null

target(name: 'git-tag',
        description: "Create, list, or delete a tag object",
        prehook: null, posthook: null) {
    if(!gitManager) gitManager = new GitManager()

    if (!argsMap.params) {
        // list
        println ' '
        gitManager.git().tagList().call().each { RevTag tag ->
            println tag.tagName
        }
        exit 1
    }
    String tagName = argsMap.params[0]

    if (argsMap.d || argsMap.delete) {
        // delete
        DeleteTagCommand cmd = gitManager.git().tagDelete()
        cmd.setTags(tagName)

        try {
            cmd.call()
            event 'StatusFinal', ["Deleted tag $tagName"]
        } catch (x) {
            if (x instanceof JGitInternalException) {
                x = x.cause
            }
            event 'StatusError', ["Could not delete tag '$tagName': $x"]
        }
    } else {
        // create
        TagCommand cmd = gitManager.git().tag()
        cmd.forceUpdate = argsMap.force || argsMap.f ?: false
        cmd.name = tagName

        try {
            cmd.call()
            event 'StatusFinal', ["Created tag $tagName"]
        } catch (x) {
            if (x instanceof JGitInternalException) {
                x = x.cause
            }
            event 'StatusError', ["Could not create tag '$tagName': $x"]
        }
    }
}

setDefaultTarget('git-tag')
