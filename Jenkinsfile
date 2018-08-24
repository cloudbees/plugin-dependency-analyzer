pipeline {
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '5', numToKeepStr: '10'))
  }
  libraries {
    lib 'elroy-libs'
  }
  agent none

  stages {
    stage('Build') {
      agent { label 'maven-build-tools' }
      steps {
        withMaven {
          sh 'mvn clean package -Dmaven.test.skip=true'
        }
      }
    }

    stage('Tests') {
      agent { label 'maven-build-tools' }
      steps {
        withMaven {
          sh 'mvn clean verify'
        }
      }

      post {
        always {
          junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      notify ( application: "Plugin's dependencies analyzer" )
    }
  }
}
