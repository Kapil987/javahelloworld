//def projectName = params['PROJECT_NAME'].trim()
//def agentName = params['AGENT_NAME'].trim()
def projectName = 'TestProject2' // mock value for testing
def agentName = 'algoworks-dev-server' // mock value for testing


pipelineJob("${projectName}-Pipeline") {
    description("Pipeline job for ${projectName}")
    parameters {
        stringParam('AGENT_NAME', agentName, 'The name of the agent to run the build on')
    }

    definition {
        cpsScm {
            scm {
                git("https://github.com/Kapil987/javahelloworld.git", 'main')  // Adjust as necessary
            }
        scriptPath('Jenkinsfile') 
            sandbox()
        }
    }
}
