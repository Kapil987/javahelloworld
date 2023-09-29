// List of project details
def projects = [
    [name: 'TestProject1', environment: 'Dev', team: 'TeamA', job: 'Build'],
    [name: 'TestProject2', environment: 'Prod', team: 'TeamB', job: 'Deploy'],
    // ... add more projects as needed
]

// Lists for repo URLs, credentials IDs, agent names, and branch names
def repoURLs = ['None', '']
def credentialsIds = ['Cred1', 'Cred2']
def agentNames = ['algoworks-dev-server', 'algoworks-dev-server',]
def branchNames = ['main', 'develop']

// Variable to determine the number of days to retain builds
def daysToKeep = 7

// Loop over projects
for (int i = 0; i < projects.size(); i++) {
    def project = projects[i]
    def projectName = "${project.name}-${project.environment}-${project.team}-${project.job}"
    
    def currentRepoURL = repoURLs[i]
    def currentCredentialsId = credentialsIds[i]
    def currentAgentName = agentNames[i]
    def currentBranchName = branchNames[i]

    pipelineJob("${projectName}-Pipeline") {
        description("Pipeline job for ${projectName}")
        
        // Add log rotator to retain builds only for the specified number of days
        logRotator {
            numToKeep(daysToKeep)
        }

        // Use agent, repo URL, credentials, and branch name as needed.
        if (currentRepoURL && currentRepoURL != 'None' && currentRepoURL != '') { 
            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                url(currentRepoURL)
                                credentials(currentCredentialsId)
                            }
                            branch(currentBranchName)
                        }
                    }
                    scriptPath('Jenkinsfile-s3-cloudfront')
                }
            }
        } else {
                cps {
                    // No scm block here, as there's no SCM for this job
                   script(readFileFromWorkspace('Jenkinsfile-s3-cloudfront'))
                }
            }
    }
}
