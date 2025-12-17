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
                            // 1. Maven calculates the next version (e.g. 1.0.1 -> 1.0.2)
                            // 2. We use versions:set to actually update the pom.xml file on disk
                            sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextPatchVersion} versions:commit'
                            
                            // 3. We read that new version into a variable
                            def newVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                            env.IMAGE_TAG = newVersion
                            echo "This is a Release Build. New Version: ${env.IMAGE_TAG}"
                        } else {
                            // Feature Branches: Just use the build number (Safety fallback)
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
