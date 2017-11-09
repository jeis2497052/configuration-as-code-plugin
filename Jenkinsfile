node("utility-slave") {
    docker.withRegistry('docker.io', 'dockerhub') {
        stage('Build Docker Image') {
            sh "docker build -t praqma/docker4jcasc:${env.BUILD_ID} ."
        }

        stage('Test') {
            echo 'Testing..'
        }

        stage('Deploy Docker Image') {
            echo 'Deploying...'


        }
    }
}


