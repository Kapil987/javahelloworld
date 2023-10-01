// Variables that may need modification

def AGENT_LABEL = "algoworks-dev-server"

// List of project details
def projects = [
    [name: 'TestProject1', environment: 'Dev', team: 'TeamA', job: 'Build'],
    //[name: 'TestProject2', environment: 'Prod', team: 'TeamB', job: 'Deploy'],
    // ... add more projects as needed
]

def credentialsIds = ['awsAlgoTestCreds', 'awsAlgoTestCreds']

// Variable to determine the number of days to retain builds
def daysToKeep = 7

// Shell script to be executed in the freestyle job
def shellScript = '''
#!/bin/bash
set -e

# Shell variables to be set
CLOUDFRONT_DIST_ID=""
BUCKET_NAME=""

# Check if the variables are set and not empty
if [ -z "$CLOUDFRONT_DIST_ID" ] || [ -z "$BUCKET_NAME" ]; then
    echo "Error: Either CLOUDFRONT_DIST_ID or BUCKET_NAME is not set."
    exit 1
fi

echo "\n########### exporting creds ###########"
export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
aws s3 ls

echo "\n########### unzipping file ###########"
mv dist ${params.dist}
unzip ${params.dist}
sleep 5

echo "\n########### uploading it to s3 ###########"
if [ -d "${WORKSPACE}/dist" ]
then
    cd ${WORKSPACE}/dist/
    aws s3 cp . s3://${BUCKET_NAME}
    aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DIST_ID} --paths "/*"
    rm -rf ${WORKSPACE}/*
else
    exit 1
fi

'''

// Loop over projects
for (int i = 0; i < projects.size(); i++) {
    def project = projects[i]
    def projectName = "${project.name}-${project.environment}-${project.team}-${project.job}"
    
    def currentCredentialsId = credentialsIds[i]

    freeStyleJob("${projectName}-FreeStyle") {
        description("Freestyle job for ${projectName}")
        
      // Set the agent for the job
        label(AGENT_LABEL)
        
        logRotator {
            numToKeep(daysToKeep)
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
        // Add file parameter to accept build.zip
        parameters {
            fileParam('dist', 'Provide the build.zip file')
        }

        // Add shell script build step
        steps {
            shell(shellScript)
        }
    }
}
