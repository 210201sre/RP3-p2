pipeline{
    agent {
        kubernetes {
          label 'build-agent'
          defaultContainer 'jnlp'
          yaml """
          apiVersion: v1
          kind: Pod
          metadata:
          labels:
            component: ci
          spec:
            containers:
            - name: jnlp
              image: odavid/jenkins-jnlp-slave:jdk11
              volumeMounts:
              - name: docker-sock
                mountPath: /var/run/docker.sock 
              resources:
                requests:
                  memory: "300Mi"
                  cpu: "0.3"
                limits:
                  memory: "500Mi"
                  cpu: "0.5"
            - name: kubectl
              image: jshimko/kube-tools-aws:latest
              command:
              - cat
              tty: true
            volumes:
            - name: docker-sock
              hostPath:
                path: /var/run/docker.sock 
          """
        }
    }

    environment {
        DOCKER_IMAGE_NAME = "speedy1096/project-two"
        MAVEN_IMAGE_NAME =  "project-one"
    }
    stages{
        stage('Build'){
            steps{
                sh 'chmod +x mvnw'
                sh 'docker build -t $MAVEN_IMAGE_NAME .'
                sh 'docker tag $MAVEN_IMAGE_NAME $DOCKER_IMAGE_NAME'
                script {
                    app = docker.image(DOCKER_IMAGE_NAME)
                }
            }
        }

        stage('Sonar Quality Analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'sonar-token-2', installationName: 'sonarcloud') {
                    sh './mvnw -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                }
            }
        }

        stage('Wait for Quality Gate') {
            steps {
                script {
                    timeout(time: 30, unit: 'MINUTES') {
                        qualitygate = waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-jenkins-token-RP3') {
                        app.push('latest')
                        
                        app.push("${env.BUILD_NUMBER}")
                        app.push("${env.GIT_COMMIT}")
                    }
                }
            }
        }
    }
    
}