pipeline {
    agent any

    options {
        timeout(time: 1, unit: 'HOURS')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    environment {
        JAVA_HOME = tool 'JDK11'
        MAVEN_HOME = tool 'Maven3'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
    }

    parameters {
        string(name: 'SUITE_NAME', defaultValue: 'Banking Automation Framework Test Suite', description: 'TestNG Suite Name')
        string(name: 'TEST_CLASS', defaultValue: '', description: 'Specific test class to run (optional)')
        booleanParam(name: 'PARALLEL_EXECUTION', defaultValue: true, description: 'Run tests in parallel')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    echo "Branch: ${GIT_BRANCH}"
                    echo "Commit: ${GIT_COMMIT}"
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building project...'
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Generate Reports') {
            steps {
                echo 'Generating Allure reports...'
                sh 'mvn allure:report || true'
                publishHTML(target: [
                    reportDir: 'target/site/allure-report',
                    reportFiles: 'index.html',
                    reportName: 'Allure Test Report'
                ])
            }
        }

        stage('Code Quality') {
            steps {
                echo 'Running code quality checks...'
                sh 'mvn verify -DskipTests || true'
                // Uncomment for SonarQube integration:
                // sh 'mvn sonar:sonar -Dsonar.projectKey=banking-automation -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=${SONAR_TOKEN}'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
            archiveArtifacts artifacts: 'allure-results/**,target/surefire-reports/**', allowEmptyArchive: true
            cleanWs()
        }

        success {
            echo '✅ Pipeline succeeded!'
            script {
                // Slack notification
                // slackSend(channel: '#test-automation', message: '✅ Tests passed', color: 'good')
            }
        }

        failure {
            echo '❌ Pipeline failed!'
            script {
                // Slack notification
                // slackSend(channel: '#test-automation', message: '❌ Tests failed', color: 'danger')
            }
        }
    }
}
