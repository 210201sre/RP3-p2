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
          workingDir: /home/jenkins
          env:
          - name: DOCKER_HOST
            value: tcp://localhost:2375
          resources:
            requests:
              memory: "900Mi"
              cpu: "0.3"
            limits:
              memory: "999Mi"
              cpu: "0.5"
        - name: dind-daemon
          image: docker:18-dind
          workingDir: /var/lib/docker
          securityContext:
            privileged: true
          volumeMounts:
          - name: docker-storage
            mountPath: /var/lib/docker
          resources:
            requests:
              memory: "900Mi"
              cpu: "0.3"
            limits:
              memory: "999Mi"
              cpu: "0.5"
        - name: kubectl
          image: jshimko/kube-tools-aws:latest
          command:
          - cat
          tty: true
        volumes:
        - name: docker-storage
          emptyDir: {}
      """
    }
  }

    environment {
        DOCKER_IMAGE_NAME = "speedy1096/project-two"
    }
    stages{
        stage('Build'){
            steps{
                script {
                    app = docker.build(DOCKER_IMAGE_NAME)
                }
            }
        }

        stage('Sonar Quality Analysis') {
            steps {
                sh 'chmod +x mvnw'
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

        stage('Canary Deployment') {
          environment {
            CANARY_REPLICAS = 1
          }
          steps {
            container('kubectl') {
                      withKubeConfig([credentialsId: 'kubeconfig']) {
                          sh "aws eks update-kubeconfig --name matt-oberlies-sre-943"
                          sh "kubectl -n rp3 set image deployment/online-store-canary p-one=$DOCKER_IMAGE_NAME:$GIT_COMMIT"
                          sh "kubectl scale deployment.v1.apps/online-store-canary --replicas=$CANARY_REPLICAS -n rp3"
                      }
                    }
          }
        }

        stage('Production Deployment') {
          environment {
            CANARY_REPLICAS = 0
          } 
          input {
            message "Deploy to Production?"
            ok "Yes"
          }
          steps {
            echo "Confirmed production has been deloyed"

          // Scale Down the canary
          container('kubectl') {
                      withKubeConfig([credentialsId: 'kubeconfig']) {
                          sh "aws eks update-kubeconfig --name matt-oberlies-sre-943"
                          sh "kubectl scale deployment.v1.apps/online-store-canary --replicas=$CANARY_REPLICAS -n rp3"
                      }
                    }

          // Scale up the new production version
          container('kubectl') {
                      withKubeConfig([credentialsId: 'kubeconfig']) {
                          sh "aws eks update-kubeconfig --name matt-oberlies-sre-943"
                          sh "kubectl -n rp3 set image deployment/online-store-production p-one=$DOCKER_IMAGE_NAME:$GIT_COMMIT"
                      }
                    }
          }
    }
  }
}