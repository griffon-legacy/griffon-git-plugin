/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.git;

import com.jcraft.jsch.Session;
import griffon.util.BuildSettings;
import griffon.util.ConfigUtils;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.ConfigInvalidException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;

import static griffon.util.GriffonNameUtils.isBlank;

/**
 * @author Andres Almiray
 */
public class GitManager {
    private static final String KEY_USERNAME = ".username";
    private static final String KEY_PASSWORD = ".password";
    private static final String KEY_PASSPHRASE = ".passphrase";
    private static final String KEY_PREFIX = "git.repositories.";

    private final BuildSettings buildSettings;
    private Git git;

    public GitManager(BuildSettings buildSettings) throws IOException {
        this.buildSettings = buildSettings;

        initIfNeeded();
    }

    public void readyPush() {
        SshSessionFactory.setInstance(new MyJschConfigSessionFactory());
    }

    public Git git() throws IOException {
        if (null == git) {
            git = Git.open(new File(buildSettings.getBaseDir(), ".git"));
        }
        return git;
    }

    public void close() {
        if (null != git) {
            git.getRepository().close();
            git = null;
        }
    }

    public PersonIdent getAuthor() {
        return personIdentFor("author");
    }

    public PersonIdent getCommitter() {
        return personIdentFor("committer");
    }

    private PersonIdent personIdentFor(String key) {
        String name = (String) getConfigValue("git." + key + ".name", null);
        String email = (String) getConfigValue("git." + key + ".email", null);
        if (isBlank(name) || isBlank(email)) {
            try {
                StoredConfig storedConfig = git().getRepository().getConfig();
                storedConfig.load();
                name = storedConfig.getString("user", null, "name");
                email = storedConfig.getString("user", null, "email");
            } catch (IOException e) {
                // ignore
            } catch (ConfigInvalidException e) {
                // ignore
            }
        }

        if (!isBlank(name) && !isBlank(email)) {
            return new PersonIdent(name, email);
        }

        return null;
    }

    private Object getConfigValue(String key, Object defaultValue) {
        Object value = System.getProperty(key);
        if (value != null) return value;
        value = ConfigUtils.getConfigValue(buildSettings.getConfig(), key);
        return value != null ? value : defaultValue;
    }

    private void initIfNeeded() throws IOException {
        try {
            git();
        } catch (IOException e) {
            File basedir = new File(buildSettings.getBaseDir(), ".");
            InitCommand init = Git.init();
            init.setDirectory(basedir);
            init.call();

            System.out.println("Initialized Git repository in " + basedir.getAbsolutePath() + "git");

            addIgnoreFile(basedir);
        }
    }

    private void addIgnoreFile(File basedir) throws IOException {
        File gitignore = new File(basedir, ".gitignore");
        DefaultGroovyMethods.setText(gitignore, "target\n");

        AddCommand add = git().add();
        add.addFilepattern(gitignore.getName());
        try {
            add.call();
        } catch (NoFilepatternException e1) {
            // ignore
        }
    }

    public CredentialsProvider getCredentialsProvider(String repository) {
        String username = getUsername(repository);
        String password = getPassword(repository);
        String passphrase = getPassphrase(repository);

        if (isBlank(username) || isBlank(password)) {
            return null;
        }

        return new ExtendedCredentialsProvider(username, password, passphrase);
    }

    private String getUsername(String repository) {
        return ConfigUtils.getConfigValueAsString(buildSettings.getConfig(), KEY_PREFIX + repository + KEY_USERNAME, null);
    }

    private String getPassword(String repository) {
        return ConfigUtils.getConfigValueAsString(buildSettings.getConfig(), KEY_PREFIX + repository + KEY_PASSWORD, null);
    }

    private String getPassphrase(String repository) {
        return ConfigUtils.getConfigValueAsString(buildSettings.getConfig(), KEY_PREFIX + repository + KEY_PASSPHRASE, null);
    }

    private static class ExtendedCredentialsProvider extends UsernamePasswordCredentialsProvider {
        private String passphrase;

        public ExtendedCredentialsProvider(String username, String password, String passphrase) {
            super(username, password);
            this.passphrase = passphrase;
        }

        public String getPassphrase() {
            return passphrase;
        }
    }

    private static class MyJschConfigSessionFactory extends JschConfigSessionFactory {
        private CredentialsProvider credentialsProvider;

        @Override
        public RemoteSession getSession(URIish uri, CredentialsProvider credentialsProvider, FS fs, int tms) throws TransportException {
            this.credentialsProvider = credentialsProvider;
            try {
                return super.getSession(uri, credentialsProvider, fs, tms);
            } finally {
                this.credentialsProvider = null;
            }
        }

        @Override
        protected void configure(OpenSshConfig.Host host, Session session) {
            if (credentialsProvider instanceof ExtendedCredentialsProvider) {
                session.setUserInfo(new ExtendedCredentialsProviderUserInfo(session, credentialsProvider));
            }
        }
    }

    private static class ExtendedCredentialsProviderUserInfo extends CredentialsProviderUserInfo {
        private CredentialsProvider credentialsProvider;

        public ExtendedCredentialsProviderUserInfo(Session session, CredentialsProvider credentialsProvider) {
            super(session, credentialsProvider);
            this.credentialsProvider = credentialsProvider;
        }

        @Override
        public boolean promptPassphrase(String msg) {
            return !isBlank(getCredentials().getPassphrase());
        }

        @Override
        public String getPassphrase() {
            return getCredentials().getPassphrase();
        }

        private ExtendedCredentialsProvider getCredentials() {
            return (ExtendedCredentialsProvider) credentialsProvider;
        }
    }
}
