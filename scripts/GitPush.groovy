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
import griffon.util.GriffonExceptionHandler
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.api.errors.JGitInternalException
import org.eclipse.jgit.transport.PushResult

/**
 * @author Andres Almiray
 */

gitManager = null

target(name: 'git-push',
        description: "Update remote refs along with associated objects",
        prehook: null, posthook: null) {
    if(!gitManager) gitManager = new GitManager()
    PushCommand cmd = gitManager.git().push()
    gitManager.readyPush()

    cmd.force = argsMap.force || argsMap.f ?: false
    if (argsMap.all) cmd.setPushAll()
    if (argsMap.tags) cmd.setPushTags()

    String remote = argsMap.params ? argsMap.params[0] : 'origin'
    cmd.credentialsProvider = gitManager.getCredentialsProvider(remote)

    try {
        Iterable<PushResult> results = cmd.call()
        results.each { PushResult result ->
            def branch = gitManager.git().repository.branch
            def oldRef = result.trackingRefUpdates.oldObjectId
            def newRef = result.trackingRefUpdates.newObjectId
            if (oldRef && newRef) {
                def previousHash = (oldRef[0]?.name() ?: '00000000')[0..7]
                def newHash = (newRef[0]?.name() ?: '00000000')[0..7]
                println "To ${result.URI}"
                println "   ${previousHash}..${newHash} ${branch}"
            } else {
                println "To ${result.URI}"
                if (argsMap.tags) println "   pushed tags"
            }
        }
    } catch (x) {
        GriffonExceptionHandler.sanitize(x).printStackTrace()
        if (x instanceof JGitInternalException) {
            x = x.cause
        }
        event 'StatusError', ["An error ocurred when pushing refs to remote: $x"]
    }
}

setDefaultTarget('git-push')
