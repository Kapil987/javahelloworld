//def projectName = params['PROJECT_NAME'].trim()
//def agentName = params['AGENT_NAME'].trim()
def projectName = 'TestProject2' // mock value for testing
def agentName = 'algoworks-dev-server' // mock value for testing


pipelineJob("${projectName}-Pipeline") {
    description("Pipeline job for ${projectName}")

    definition {
        cps {
          
            script("""
                pipeline {
                    agent {
                        label '${agentName}'
                    }

                    stages {
                        stage('Build') {
                            steps {
                                echo 'This is a build stages'
                                sleep 2
                            }
                        }
                        stage('Test') {
                            steps {
                                echo 'This is a test stage'
                                sleep 3
                            }
                        }
                        stage('Deploy') {
                            steps {
                                echo 'This is a deploy stage'
                                sleep 4
                            }
                        }
                    }
                }
            """)
            sandbox(false)
        }
    }
}
