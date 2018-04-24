#!/usr/bin/env groovy

pipeline {
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '5', numToKeepStr: '15'))
  }
  agent { docker 'alecharp/maven-build-tools' }

  stages {
    stage('Build') {
      steps {
        sh 'mvn clean package -Dmaven.test.skip=true'
      }
    }

    stage('Tests') {
      steps {
        sh 'mvn clean verify'
      }

      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
        }
      }
    }
  }
}
