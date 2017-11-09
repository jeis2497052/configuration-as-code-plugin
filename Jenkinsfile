node("utility-slave") {
    docker.withRegistry('https://docker.example.com/', 'dockerhub') {
        stage('Build Docker Image') {
            def docker4jcasc = docker.build("praqma/docker4jcasc:${env.BUILD_TAG}", '.')
            docker4jcasc.push()
        }

        stage('Deploy Docker Image') {
            echo 'Deploying...'
            docker4jcasc.push('latest');
        }
    }
}


