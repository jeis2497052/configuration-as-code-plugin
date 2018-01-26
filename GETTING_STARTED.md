## Generating the docs
To start the plugin from scratch do

```sh
mvn clean package -DskipTests hpi:run
```
Running the above command will delete all packages that have been manually installed (not added in pom.xml).


To access the `cac`-generated docs, use this url
```
http://localhost:8080/jenkins/plugin/configuration-as-code
```
To refresh the docs, you need to re-run `hpi:run`


## Added deps to pom.xml for testing
To test that dependencies works with `cac` you can install them by adding the to the pom.xml
```xml
    <dependency>
      <groupId>org.jenkins-ci.plugins</groupId>
      <artifactId>github-oauth</artifactId>
      <version>0.27</version>
      <type>hpi</type>
    </dependency>

    <dependency>
      <groupId>org.jenkins-ci.plugins</groupId>
      <artifactId>pam-auth</artifactId>
      <version>1.3</version>
      <type>hpi</type>
    </dependency>

    <dependency>
      <groupId>org.jenkins-ci.plugins</groupId>
      <artifactId>spotinst</artifactId>
      <version>2.0.2</version>
      <type>hpi</type>
    </dependency>

```
The deps above work, and is already included in the pom.xml file. Notice that some of them require you to update jenkins plugins manually through the UI before the `cac`-docs will be generated; github-oauth was one of them.

### Note
I install the plugins through the pom.xml because sometimes the plugins installed through the UI does appear in the `cac`-docs. Nothing to do about that.

## How does it work - Findings
The hypothesis is that only plugins with hooks into certain extension points or plugins that follow a specific convention can be configured in `cac`.

The github-oauth plugin works and can be installed and configured, and that plugin works with the `securityRealm` extensionpoint. Other extension points like `cloud` also works, I tried that with the `aws-spotinst` plugin.

However the artifactory plugin does not work. I thought it was because it lacked a static class implementing the descriptor interface but that is not the case. Both plugins does this right. The only difference is the security realm.

**Artifactory plugin** - ArtifactoryBuilder.java
```java
@Extension
    public static final class DescriptorImpl extends Descriptor<GlobalConfiguration> {
```

**Github-OAuth** - GithubSecurityRealm.java
```java
@Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
```

But, where they do differ is the `@DataboundContructor`. The artifactory has tons of `@DataboundContructor`s, but it does not have one it the class where the static class of the descriptor is! And THAT must be where we need to start.


## Important
The artifactory can not be installed though the pom.xml, it makes the Configurator.java fail on line 153. Possibly due to some missing help page or wrongly named attributes, it would make sense with the missing `@DataboundConstructor`.
```java
 /**
     * Retrieve the html help tip associated to an attribute.
     * FIXME would prefer <st:include page="help-${a.name}.html" class="${c.target}" optional="true"/>
     */
    public String getHtmlHelp(String attribute) throws IOException {
        final URL resource = getKlass().getResource("help-" + attribute + ".html");
        if (resource != null) {
            return IOUtils.toString(resource);
        }
        return "";
    }
```
failing dependency
```xml
   <dependency>
      <groupId>org.jenkins-ci.plugins</groupId>
      <artifactId>artifactory</artifactId>
      <version>2.13.1</version>
      <type>hpi</type>
    </dependency>

```

## Resources

jenkins extension points: https://jenkins.io/doc/developer/extensions/jenkins-core/#artifactmanagerfactory

full list of extension points:
https://jenkins.io/doc/developer/extensions/

how to define an extension point: https://wiki.jenkins.io/display/JENKINS/Defining+a+new+extension+point

jenkins oauth plugin:
https://github.com/jenkinsci/github-oauth-plugin
