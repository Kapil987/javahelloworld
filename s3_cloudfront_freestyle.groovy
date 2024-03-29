// Variables that may need modification
def AGENT_LABEL = "algoworks-dev-server"

// List of project details
def projects = [
    [name: 'ProjectName1', environment: 'Dev', team: 'TeamA', job: 'JobName'],
    //[name: 'ProjectName2', environment: 'Prod', team: 'TeamB', job: 'JobName'],
    // ... add more projects as needed
]

def credentialsIds = ['awsAlgoTestCreds', 'awsAlgoTestCreds']

// Variable to determine the number of days to retain builds
def daysToKeep = 7
//if freestly has to accept parameters println "Printing from Shell: ${temp}"

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

echo "########### Exporting AWS Creds ###########"
export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
#aws s3 ls

echo "########### Unzipping Zip File ###########"
ls -l
mv build ${build}
unzip ${build}
sleep 5

echo "########### Uploading files to s3 ###########"
if [ -d "${WORKSPACE}/build" ]
then
    cd ${WORKSPACE}/build/build/
    aws s3 cp . s3://${BUCKET_NAME} --recursive
    aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DIST_ID} --paths "/*"
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
            preBuildCleanup()
        }
        // Add file parameter to accept build.zip
        parameters {
            fileParam('build', 'Provide the build.zip file')
        }

        // Add shell script build step
        steps {
            shell(shellScript)
        }
    }
}
