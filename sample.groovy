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
                        stage('build') {
                            steps {
                                echo 'This is a build stage'
                            }
                        }
                        stage('test') {
                            steps {
                                echo 'This is a test stage'
                            }
                        }
                        stage('deploy') {
                            steps {
                                echo 'This is a deploy stage'
                            }
                        }
                    }
                }
            """)
            sandbox(false)
        }
    }
}
