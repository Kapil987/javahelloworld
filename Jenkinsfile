pipeline {
  agent any

  stages {
      stage('Test') {
          steps {
            script { 
                if (env.BRANCH_NAME == 'dev') {
                    //git branch: 'dev',
                    //   credentialsId: 'abhi_repo',
                    //url: 'https://github.com/Kapil987/javahelloworld.git'
                    echo 'This is dev'
                } 
                else if (env.BRANCH_NAME == 'main') {
                    echo 'this is main'
                }

          }
        
      }
  }
}
}
