import hudson.model.*
import utils.JobUtils
import jobTemplates.KubeDeployment
import jobTemplates.MavenDeployment


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

//dir-structure
String create_folder_structure(ArrayList dir_list) {
    // folder(folder_name)
    String previous_path = ""
    for (folder_name in dir_list) {
        println("going to create folder: " + previous_path + folder_name)
        folder(previous_path + folder_name)
        previous_path = previous_path + folder_name + "/"
    }
    return previous_path
}


// get the current working directory
def cwd = hudson.model.Executor.currentExecutor().getCurrentWorkspace().absolutize()

pipeline_file_list = searchYamlFiles(cwd.toString())

for (current_pipeline in pipeline_file_list) {

    println("Working on :" + current_pipeline)


    JobUtils job_config = new JobUtils(current_pipeline)

    // call the function of dir structure
    ArrayList dir_structure = job_config.get_directory_structure()

    String directory_prefix = create_folder_structure(dir_structure)

    print("jobname is: " + job_config.get_job_name())

    String job_type = job_config.get_job_type()  // jobTemplates

    if ("kubernetes" == job_type) {
        new KubeDeployment().create(pipelineJob(directory_prefix + job_config.get_job_name()), job_config)
    } else if ("maven" == job_type) {
        new MavenDeployment().create(pipelineJob(directory_prefix + job_config.get_job_name()), job_config)
    }


}
