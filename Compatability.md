## Tested Plugins
The plugins we have tried to configure with Casc

- Warnings `v4.63`   
https://plugins.jenkins.io/warnings  

- Spotinst-aws `v2.0.2`   
https://plugins.jenkins.io/spotinst

- Artifactory `v2.13.1`   
https://plugins.jenkins.io/artifactory 

- PAM Authentication `v1.3`   
https://plugins.jenkins.io/pam-auth 

- Github Authentication `v0.28.1`   
https://plugins.jenkins.io/github-oauth 

- Mailer `v1.20`   
https://plugins.jenkins.io/mailer 


## Compatability matrix

| plugin        | Security Realm | Nodes   |
|---------------|----------------|---------|
| Warnings      | x              |x        |
| Spotinst      | x              | &#x2713; / x|
| Artifactory   | x              |x
| GitHub Auth   | &#x2713;       |x
| PAM Auth      | &#x2713;       |x
| Mailer        | x              |x

&#x2713; - has supoported configuration; x - no supported configuration; &#x2713; / x - Shows up in docs but lacks configuration support.

## Configuration

#### Github Authentication 

Configuration example for GitHub Authentication
  ```yml
securityRealm:
  github:
    clientID: "someid"
    clientSecret: "78wuierlkhku"
    githubApiUri: "api.github.com"
    githubWebUri: "github.com"
    oauthScopes: "{repo}"
  ```

#### Spotinst

Spotinst does not work, however it does show up in the generated Casc docs. I am led to believe that it will work with an easy fix however. It fails with a `Missing Databound Constructor` exception, and I can see that the databound constructor is missing in the [code](https://github.com/jenkinsci/spotinst-plugin/blob/08391e59d47506c12b2f061a01a12f9cef1c3c84/src/main/java/hudson/plugins/spotinst/slave/SpotinstSlave.java#L38)

Expected valid exmaple configuration for spotinst
```yml
  nodes:
    - spotinstSlave:
        instanceId: 'aws-32yriudskjh-instanceid' 
        name: "foo"
        remoteFS: '/tmp/2'
        launcher: "jnlp"
        numExecutors: 1

```

#### PAM Authentication

Example configuration for PAM Authentication
```yml
  securityRealm:
    pam:
      serviceName: "hello"
```

## Unknowns

Our checklist to make plugins work was initially: does it have a `@DataboundConstructor`, and the appropriate `config.jelly`file, does it have a nested static class of `DescriptorImpl` wich extends a Descriptor. and does it have the following two methods `getDescriptor()` and `configure()` along with getters and setters for the configurable fields i the class. 

The example below shoved the Descriptor we were looking for. It is however not that simple. There are many descriptors and the one in mailer ([2]) is not globally configurable. In stead mailer uses a `global.jelly` to statically fetch variables from the global configuration. Descriptor ([1]) does work because SecurityRealm is located with the jenkins model. 

[1] **Github-OAuth** - GithubSecurityRealm.java
```java
@Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        ...
    }
```

[2] **mailer** 
```java
public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    ...
}
```