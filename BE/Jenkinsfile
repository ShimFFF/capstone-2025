pipeline {
    agent any

    environment {
        ANSIBLE_HOST_KEY_CHECKING = 'False'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/9oormthon-univ/2024_DANPOON_TEAM_22_BE.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'  // Maven 대신 Gradle 빌드 명령 사용
            }
        }

        stage('Deploy') {
            steps {
                ansiblePlaybook(
                    playbook: 'deploy.yml',
                    inventory: 'hosts.ini',
                    extras: '--private-key /path/to/private/key'
                )
            }
        }
    }

    post {
        success {
            echo '배포가 성공적으로 완료되었습니다.'
        }
        failure {
            echo '배포에 실패했습니다.'
        }
    }
}
