pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'jdk17'
    }

    environment {
        NEXUS_VERSION = "0.0.1"
        NEXUS_URL = "http://nexus:8081/repository/enterprise-releases/"
        DOCKER_REGISTRY = "localhost:8082"
        IMAGE_NAME = "enterprise-app"
    }

    stages {
        stage('Build JAR') {
            steps {
                dir('app') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('app') {
                    script {
                        dockerImage = docker.build("${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}", "--no-cache .")
                    }
                }
            }
        }

        stage('Push Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    docker.withRegistry("http://${DOCKER_REGISTRY}", 'nexus-docker-auth') {
                        dockerImage.push()
                        dockerImage.push("latest")
                    }
                }
            }
        }
    }
}
