FROM maven:3.5-jdk-8-slim as builder

ENV prj=/usr/src/casc

WORKDIR ${prj}

VOLUME ["/root/.m2"]

ADD . .

#Build hpi file for jenkins
RUN mvn clean package -DskipTests

FROM jenkins/jenkins:lts

COPY --from=builder /usr/src/casc/target/configuration-as-code.hpi .
