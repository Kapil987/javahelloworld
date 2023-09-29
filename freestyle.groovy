// List of project details
def projects = [
    [name: 'TestProject1', environment: 'Dev', team: 'TeamA', job: 'Build'],
    [name: 'TestProject2', environment: 'Prod', team: 'TeamB', job: 'Deploy'],
    // ... add more projects as needed
]

def credentialsIds = ['pm2-stage-aws-creds', 'Cred2']

// Variable to determine the number of days to retain builds
def daysToKeep = 7

// Shell script to be executed in the freestyle job
def shellScript = '''
#!/bin/bash
set -x
export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
aws s3 ls
'''

// Loop over projects
for (int i = 0; i < projects.size(); i++) {
    def project = projects[i]
    def projectName = "${project.name}-${project.environment}-${project.team}-${project.job}"
    
    def currentCredentialsId = credentialsIds[i]

    freeStyleJob("${projectName}-FreeStyle") {
        description("Freestyle job for ${projectName}")

        // Add log rotator to retain builds only for the specified number of days
       logRotator {
            daysToKeep(daysToKeep)
        }

        // Bind AWS credentials as environment variables
        wrappers {
            credentialsBinding {
                amazonWebServicesCredentialsBinding {
                    accessKeyVariable('AWS_ACCESS_KEY_ID')
                    secretKeyVariable('AWS_SECRET_ACCESS_KEY')
                    credentialsId(currentCredentialsId)
                }
            }
        }

        // Add shell script build step
        steps {
            shell(shellScript)
        }
    }
}
