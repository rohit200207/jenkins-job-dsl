
import hudson.model.*
import utils.JobUtils

// function to search for all the files ending with .yaml or .yml
ArrayList searchYamlFiles(String dirPath) {
    ArrayList file_list = []
    File dir = new File(dirPath)
    if (dir.isDirectory()) {
        dir.eachFileRecurse { file ->
            if (file.isFile() && (file.name.endsWith(".yaml") || file.name.endsWith(".yml"))) {
                file_list.add(file.absolutePath)
            }
        }
    } else {
        println("Error: $dirPath is not a valid directory.")
    }
    return file_list
}

// get the current working directory
def cwd = hudson.model.Executor.currentExecutor().getCurrentWorkspace().absolutize()

pipeline_file_list  = searchYamlFiles(cwd.toString())

for (current_pipeline in pipeline_file_list) {

    println("Working on :" + current_pipeline)


    JobUtils job_config = new JobUtils(current_pipeline)

    print("jobname is: "+job_config.get_job_name())
    pipelineJob(job_config.get_job_name()) {
        definition {
            cps {
                script("""

pipeline {
        agent any

        stages {

            stage('Checkout') {
                steps {
                    echo 'Checking out source code...'
                }
            }

            stage('Build') {
                steps {
                    echo 'Building the project...'
                }
            }

            stage('Test') {
                steps {
                    echo 'Running tests...'
                }
            }

            stage('Deploy DEV') {
                steps {
                    echo 'Deploying the project...'
                }
            }

            stage('Deploy QA') {
                steps {
                    echo 'Deploying the project...'
                }
            }

            stage('Deploy PROD') {
                steps {
                    echo 'Deploying the project...'
                }
            }
        }
    }
""")
                sandbox()
            }
        }
    }







}
