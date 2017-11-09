node("utility-slave") {
    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
        stage('Build Docker Image') {
            def docker4jcasc = docker.build("praqma/docker4jcasc:${env.BUILD_ID}", '.')
            docker4jcasc.push()
        }

        stage('Deploy Docker Image') {
            echo 'Deploying...'
            docker4jcasc.push('latest');
        }
    }
}


