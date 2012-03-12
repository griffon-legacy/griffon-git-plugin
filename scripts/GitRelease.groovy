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

import org.codehaus.griffon.artifacts.ArtifactUtils
import org.codehaus.griffon.artifacts.model.Plugin
import org.codehaus.griffon.artifacts.model.Archetype
import griffon.util.Metadata

/**
 * @author Andres Almiray
 */

includePluginScript('git', 'GitCommit')
includePluginScript('git', 'GitTag')
includePluginScript('git', 'GitPush')

artifactInfo = null

target(name: 'git-release',
        description: "Commits, tags and pushes a new release",
        prehook: null, posthook: null) {

    if (getPropertyValue('git.disable.release', false)) return

    argsMap.all = true
    argsMap.tags = true
    argsMap.force = true

    'git-commit'()

    def paramsCopy = []
    paramsCopy.addAll(argsMap.params)
    argsMap.params << computeTagName()

    'git-tag'()

    gitManager.close()
    argsMap.params = paramsCopy

    'git-push'()
}

setDefaultTarget('git-release')

computeTagName = {
    String version = ''
    if (isPluginProject) {
        if (!artifactInfo) {
            includeTargets << griffonScript('PackagePlugin')
            def pluginDescriptor = ArtifactUtils.getPluginDescriptor(basedir)
            artifactInfo = loadArtifactInfo(Plugin.TYPE, pluginDescriptor)
        }
        version = artifactInfo.version
    } else if(isArchetypeProject) {
        if (!artifactInfo) {
            includeTargets << griffonScript('PackageArchetype')
            def archetypeDescriptor = ArtifactUtils.getArchetypeDescriptor(basedir)
            artifactInfo = loadArtifactInfo(Archetype.TYPE, archetypeDescriptor)
        }
        version = artifactInfo.version
    } else {
        version = Metadata.current.getApplicationVersion()
    }

    return "RELEASE_${version.replace('.', '_').replace('-', '_').toUpperCase()}"
}
