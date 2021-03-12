pipeline{
    agent any

    environment {
        DOCKER_IMAGE_NAME - "speedy1096/project-two"
        MAVEN_IMAGE_NAME =  "project-one:0.0.1-SNAPSHOT"
    }
    stages{
        stage('Build'){
            steps{
                sh 'chmod +x mvnw && ./mvnw spring-boot:build-image'
                sh 'docker tag $MAVEN_IMAGE_NAME $DOCKER_IMAGE_NAME'
                script {
                    app = docker.image(DOCKER_IMAGE_NAME)
                }
            }
          
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-jenkins-token') {
                        app.push('latest')
                        // Double quotes will interpolate environment variables in Groovy
                        app.push("${env.BUILD_NUMBER}")
                        app.push("${env.GIT_COMMIT}")
                    }
                }
            }
        }
    }
    
}