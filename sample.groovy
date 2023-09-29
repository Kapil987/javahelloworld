// List of project details
def projects = [
    [name: 'TestProject1', environment: 'Dev', team: 'TeamA', job: 'Build'],
    [name: 'TestProject2', environment: 'Prod', team: 'TeamB', job: 'Deploy'],
    // ... add more projects as needed
]

// Lists for repo URLs, credentials IDs, agent names, and branch names
//def repoURLs = ['None', '']
//def credentialsIds = ['Cred1', 'Cred2']
def agentNames = ['algoworks-dev-server', 'algoworks-dev-server',]
//def branchNames = ['main', 'develop']

// Variable to determine the number of days to retain builds
def daysToKeep = 7

// Loop over projects
for (int i = 0; i < projects.size(); i++) {
    def project = projects[i]
    def projectName = "${project.name}-${project.environment}-${project.team}-${project.job}"
    
    pipelineJob("${projectName}-Pipeline") {
        description("Pipeline job for ${projectName}")
        
        // Add log rotator to retain builds only for the specified number of days
        logRotator {
            numToKeep(daysToKeep)
        }

        // Set the script path
        definition {
            cps {
                script('Jenkinsfile-s3-cloudfront')
            }
        }
    }
}
