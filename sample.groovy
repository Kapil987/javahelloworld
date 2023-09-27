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
                        stage('Hello Stage') {
                            steps {
                                echo 'i have been automated2'
                            }
                        }
                    }
                }
            """)
            sandbox()
        }
    }
}
