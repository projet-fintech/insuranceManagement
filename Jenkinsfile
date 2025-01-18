pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker'
    }
    environment {
        AWS_REGION = 'eu-west-3'
        ECR_REGISTRY = '329599629502.dkr.ecr.eu-west-3.amazonaws.com'
        IMAGE_NAME = "innsurance-management"  // Utilisez une variable pour le nom de l'image
    }
    stages {
        stage('Checkout') {
                    steps {
                        checkout scmGit(
                            branches: [[name: '*/main']],
                            extensions: [],
                            userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/insuranceManagement.git']]
                        )
                    }
                }
        stage('Build') {
            steps {
              script {
                   sh "mvn clean install -DskipTests"
              }
            }
        }
          /*stage('Run Unit Tests') {
           steps {
                script {
                      sh "mvn test"
                }
            }
        }*/
         stage('Build Docker Image') {
                               steps {
                                   script {
                                       def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                                        sh "docker build  --platform=linux/amd64 --add-host=registry-1.docker.io:8.8.8.8 -t ${localImageName} ."
                                   }
                               }
                           }
         stage('Push to ECR') {
            steps {
                script {
                    withCredentials([aws(credentialsId: 'aws-credentials')]) {

                        def awsCredentials = "-e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}"

                        docker.image('amazon/aws-cli').inside("--entrypoint='' ${awsCredentials}") {
                            sh """
                                aws ecr get-login-password --region ${AWS_REGION} > ecr_password.txt
                            """
                        }

                        // Login à ECR
                        sh "cat ecr_password.txt | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                        sh "rm ecr_password.txt"

                        // Tag et push des images
                        def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                        def ecrImageLatest = "${ECR_REGISTRY}/${IMAGE_NAME}:latest"
                        def ecrImageVersioned = "${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"

                        sh """
                            docker tag ${localImageName} ${ecrImageLatest}
                            docker tag ${localImageName} ${ecrImageVersioned}
                            docker push ${ecrImageLatest}
                            docker push ${ecrImageVersioned}
                        """
                    }
                }
            }
        }
        stage('Cleanup') {
            steps {
                script {
                    sh """
                        docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:latest || true
                    """
                }
            }
        }
    }
   post {
        failure {
            echo 'Pipeline failed! Cleaning up...'
            sh 'rm -f ecr_password.txt || true'
        }
        success {
            echo 'Pipeline succeeded! Image pushed to ECR.'
        }
        always {
            cleanWs()
        }
    }
}