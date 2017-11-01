# Jenkins Configuration as Code Plugin

# Getting started
[getting started](/GETTING_STARTED.md)


### How to run in your development environment

Clone the project's git repo to your local space.
It is a maven project, to run in command line type

   ```mvn hpi:run [-q]```

After the java process will run you will have this project Jenkins instance available on http://localhost:8080/jenkins

To debug. If you are using IntelijIDEA, create a debug configuration for **hpi:run** command and debug as a usually java project.

### Structure

To be updated ...

# WoW/Contributing

This repository is currently being used in an "exploring" mode so we're more liberal when it comes to delivery rules.
Pushes to master are allowed. We kindly ask you though to _keep ALL of your work in issues and mention issues in commit messages_.

Development on *master* branch

To make is easier you can use [git phlow](https://github.com/Praqma/git-phlow/tree/master/docs) - please update to the latest version

```
# Tap our repository
brew tap praqma/praqma-tap

# Install git-phlow
brew install git-phlow

# Upgrade to a new version
brew upgrade git-phlow
```

Since there is no real integration attached to that repository yet use `git phlow deliver --local` to merge and push it to master



# Design decision

Though exploring there are some already agreed design decision taken and few choices along the way. Feedback are welcome on those - it never to late to change things.

* **We will be using YML as user-interface**: Users will configure their Jenkins with configuration as code using YML file(s), as we believe YML is the most easy to use format for humans, that is also easy to read for computers. It's also a well established format and it support comments. We will not be using json, as this is to verbose and just to much work to write by hand.
* **Plugin is installed based on an YML file**: Currently the list of plugin to install on the Jenkins master is specified using YML file. It allows us to make it part of the primary configuration file `jenkins.yml` for example, as this is YML. It also ensures the user only need to use one format. We know plugins.txt exists for the Jenkins docker container, where a shell script downloads the plugins and dumps them into the plugin installation dir. It would be possible to support the same format later.
