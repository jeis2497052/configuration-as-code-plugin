FROM maven:3.5-jdk-8-slim as builder

ENV prj=/usr/src/casc

WORKDIR ${prj}

VOLUME ["/root/.m2"]

ADD . .

Build hpi file for jenkins
RUN mvn clean package -DskipTests

FROM jenkins/jenkins:lts

ARG JAVA_OPTS
ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"

##### JENKINS SETUP
ENV JENKINS_UC https://updates.jenkins-ci.org
ENV JENKINS_REF /usr/share/jenkins/ref
ENV JENKINS_HOME /var/jenkins_home
ENV JENKINS_WAR /usr/share/jenkins/jenkins.war

#COPY --from=builder /usr/src/casc/target/configuration-as-code.hpi /var/jenkins_home/plugins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN xargs /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
