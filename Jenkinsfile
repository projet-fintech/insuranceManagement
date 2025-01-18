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
        IMAGE_NAME = "innsurance-management"
        COMPONENT_NAME = "Inssurance-Management"
        SONAR_TOKEN = "39cc334a0a13dc54d616ab48a6949fae534f6b15"
        SONAR_HOST = "http://192.168.0.147:9000"
    }
    stages {
        // Étape 1 : Checkout du code
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/insuranceManagement.git']]
                )
            }
        }

        // Étape 2 : Build du projet
        stage('Build') {
            steps {
                script {
                    sh "mvn clean install -DskipTests"
                }
            }
        }

        // Étape 3 : Exécution des tests unitaires
        stage('Run Unit Tests') {
            steps {
                script {
                    sh "mvn test"
                }
            }
        }

        // Étape 4 : Analyse SonarQube
        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube') {
                        sh """
                        mvn sonar:sonar -X \
                            -Dsonar.projectKey=${COMPONENT_NAME}-project \
                            -Dsonar.sources=src/main/java \
                            -Dsonar.tests=src/test/java \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Dsonar.host.url=${SONAR_HOST} \
                            -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }

        // Étape 5 : Build de l'image Docker (optionnel)
        stage('Build Docker Image') {
            steps {
                script {
                    def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                    sh "docker build -t ${localImageName} ."
                }
            }
        }

        // Étape 6 : Push de l'image Docker vers ECR
       /* stage('Push to ECR') {
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
        }*/

        // Étape 7 : Nettoyage
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
        // Actions post-build
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