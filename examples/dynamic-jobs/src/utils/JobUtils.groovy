package utils

@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

class JobUtils{

    def job_config

    JobUtils(current_pipeline){
        //parsing the yaml content
        def parsed_job_config = new Yaml().load((current_pipeline as File).text)
        this.job_config=parsed_job_config
    }

    String get_job_name(){
        return this.job_config.job_name
    }

    String get_build_command(){
        return this.job_config.build_command
    }

    ArrayList get_environments() {
        return this.job_config.env
    }

}
