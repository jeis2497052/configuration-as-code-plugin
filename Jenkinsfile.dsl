pipelineJob('configuration-as-code-plugin') {

  def repo = 'https://github.com/Praqma/configuration-as-code-plugin.git'

  description("Pipeline for $repo")

  triggers {
        githubPush()
  }
  definition {
    cpsScm {
      scm {
        git {
          remote { url(repo) }
          branches('master')
          scriptPath('Jenkinsfile')
          extensions { }  // required as otherwise it may try to tag the repo, which you may not want
        }
      }
    }
  }
}