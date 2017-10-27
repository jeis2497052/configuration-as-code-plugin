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
