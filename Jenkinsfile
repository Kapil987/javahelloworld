pipeline {
  agent any

  stages {
      stage('SCM-Checkout') {
          steps {
              git branch: 'dev',
                //   credentialsId: 'abhi_repo',
                  url: 'https://github.com/Kapil987/javahelloworld.git'
          }
      }
  }
}
