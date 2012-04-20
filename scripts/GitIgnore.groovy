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

/**
 * @author Andres Almiray
 */

target(name: 'git-ignore',
        description: "Add .gitignore patterns",
        prehook: null, posthook: null) {
    GitManager gitManager = new GitManager(grifonSettings)

    File gitignoreFile = new File('.gitignore')

    if (argsMap.params) {
        List<String> lines = gitignoreFile.readLines()
        argsMap.params.unique().each { pattern ->
            if (!lines.contains(pattern)) {
                gitignoreFile.append(pattern + '\n')
                println "... adding '${pattern}' to .gitignore"
            }
        }
    } else {
        println gitignoreFile.text
    }
}

setDefaultTarget('git-ignore')
