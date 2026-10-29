pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        DOCKERHUB_USER       = 'ankith33'
        IMAGE_NAME           = "${DOCKERHUB_USER}/calculator-devops"
        DOCKER_IMAGE         = "${IMAGE_NAME}:${BUILD_NUMBER}"
        COMPOSE_PROJECT_NAME = 'calculator'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_PASS')]) {
                    sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
                    sh 'docker push ${IMAGE_NAME}:${BUILD_NUMBER}'
                    sh 'docker push ${IMAGE_NAME}:latest'
                    sh 'docker logout'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose up -d --remove-orphans'
                sh '''
                    for i in $(seq 1 20); do
                        if curl -fs http://localhost:8081/actuator/health | grep -q UP; then
                            echo "Deployment healthy"
                            exit 0
                        fi
                        echo "Waiting for service... ($i/20)"
                        sleep 5
                    done
                    echo "Health check failed"
                    docker compose logs calculator
                    exit 1
                '''
            }
        }
    }
}
