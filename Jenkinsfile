pipeline {
    agent  {
        label 'utility-slave'
    }

    stages {
        stage('Build Docker Image') {
            steps {
                sh "docker build -t praqma/docker4jcasc:${env.BUILD_ID} ."
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
		//sh "src/test/test_image.sh prodpc ${env.NEW_BUILD_NAME}"
		//sh "git tag -a ${env.NEW_BUILD_NAME} -m 'Tagged by Jenkins'"
		// temporarily disabled pushing tags - to verofy if it stops the pipeline from retriggering - FIXME
		//sh "git push origin --tags"
            }
        }
        stage('Deploy Docker Image') {
            steps {
                sh "docker push praqma/docker4jcasc:${env.BUILD_ID}"
            }
        }
    }
}
