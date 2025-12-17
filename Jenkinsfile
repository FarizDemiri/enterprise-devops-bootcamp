@Library('jenkins-shared-library') _

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
        stage('Increment Version') {
            steps {
                dir('app') {
                    script {
                        echo "Calculating Next Version..."
                        if (env.BRANCH_NAME == 'main') {
                            // 1. Read Current Version
                            def currentVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                            
                            // 2. Calculate Next Version (Groovy Magic)
                            // Assumes format X.Y.Z (e.g., 0.0.1)
                            def parts = currentVersion.tokenize('.')
                            def major = parts[0]
                            def minor = parts[1]
                            def patch = parts[2].toInteger()
                            def newVersion = "${major}.${minor}.${patch + 1}"
                            
                            // 3. Update pom.xml
                            sh "mvn versions:set -DnewVersion=${newVersion} versions:commit"
                            
                            env.IMAGE_TAG = newVersion
                            echo "This is a Release Build. New Version: ${env.IMAGE_TAG}"
                        } else {
                            // Feature Branches
                            env.IMAGE_TAG = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
                            echo "This is a Feature Build. Tag: ${env.IMAGE_TAG}"
                        }
                    }
                }
            }
        }

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
                        // Use the dynamic TAG we calculated above
                        dockerImage = buildDockerImage("${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}")
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
        
        stage('Commit Version') {
             when { branch 'main' }
             steps {
                 dir('app') {
                     script {
                         withCredentials([usernamePassword(credentialsId: 'git-credentials', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                             // Configure Git Identify
                             sh "git config user.email 'jenkins@example.com'"
                             sh "git config user.name 'Jenkins CI'"
                             
                             // Set the URL to include credentials (so we can push without interactive password prompt)
                             sh "git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/FarizDemiri/enterprise-devops-bootcamp.git"
                             
                             // Commit the modified pom.xml
                             sh 'git add pom.xml'
                             sh 'git commit -m "ci: Bump version to ${IMAGE_TAG} [skip ci]"'
                             
                             // Push back to GitHub
                             sh 'git push origin HEAD:main'
                         }
                     }
                 }
             }
        }
    }
}
