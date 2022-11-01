pipeline {
  agent any

  stages {
      stage('SCM-Checkout') {
          steps {
            script { 
                if (env.BRANCH_NAME == 'dev') {
                    git branch: 'dev',
                    //   credentialsId: 'abhi_repo',
                    url: 'https://github.com/Kapil987/javahelloworld.git'
                    echo 'This is dev'
                } 
                else {
                    echo 'things and stuff'
                }

          }
        
      }
  }
}
}
