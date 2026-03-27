# CI/CD Pipeline Configuration

This file documents the environment variables and secrets needed for the CI/CD pipelines.

## Required GitHub Secrets

Add these secrets in your GitHub repository settings (Settings → Secrets and variables → Actions):

### 1. Slack Integration (Optional)
```
SLACK_WEBHOOK - Your Slack webhook URL for notifications
```

### 2. SonarQube Integration (Optional)
```
SONAR_TOKEN - SonarQube authentication token
SONAR_HOST_URL - SonarQube server URL (default: http://localhost:9000)
```

### 3. Artifactory/Repository (Optional)
```
ARTIFACTORY_USERNAME - Artifactory credentials
ARTIFACTORY_PASSWORD - Artifactory credentials
```

### 4. Email Notifications (Optional)
```
NOTIFICATION_EMAIL - Email address for test reports
SMTP_SERVER - SMTP server address
SMTP_USERNAME - SMTP username
SMTP_PASSWORD - SMTP password
```

## Environment Variables

Add these in your GitHub Actions workflows or repository settings:

```yaml
# Java Build
JAVA_VERSION: 11
MAVEN_CACHE: true

# Test Execution
HEADLESS_MODE: true
BROWSER: chrome
TIMEOUT: 30

# Reporting
ALLURE_RESULTS_DIR: allure-results
TEST_RESULTS_DIR: target/surefire-reports
```

## Setting Up Secrets

1. Go to your GitHub repository
2. Navigate to: Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Add each required secret:
   - Name: `SLACK_WEBHOOK`
   - Value: Your webhook URL
5. Repeat for other secrets

## Jenkins Integration (if using Jenkins instead)

For Jenkins, create a `Jenkinsfile` at the root of your repository:

```groovy
pipeline {
    agent any
    
    environment {
        JAVA_HOME = tool 'JDK11'
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'allure-results']]
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
            archiveArtifacts artifacts: 'allure-report/**', allowEmptyArchive: true
        }
    }
}
```

## GitLab CI Integration (if using GitLab)

Create a `.gitlab-ci.yml` file at the root:

```yaml
image: maven:3.8.1-openjdk-11

stages:
  - build
  - test
  - report

build:
  stage: build
  script:
    - mvn clean compile

test:
  stage: test
  script:
    - mvn test
  artifacts:
    when: always
    reports:
      junit: target/surefire-reports/**/*.xml
    paths:
      - allure-results/
      - target/surefire-reports/
    expire_in: 30 days

allure:
  stage: report
  image: maven:3.8.1-openjdk-11
  script:
    - mvn allure:report
  artifacts:
    paths:
      - allure-report/
    expire_in: 30 days
```

## Docker Integration

A `Dockerfile` for running tests in containers:

```dockerfile
FROM maven:3.8.1-openjdk-11

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn", "test"]
```

Build and run:
```bash
docker build -t banking-automation-framework .
docker run -v $(pwd)/allure-results:/app/allure-results banking-automation-framework
```

## Viewing Reports

### Allure Report
- After tests run, check the "Allure Report" artifact
- Download and open `index.html` in a browser

### Test Results
- Check the "Test Results" artifact for raw XML reports
- GitHub also displays test summary in workflow runs

### Maven Site Report
- Check "Maven Site Report" artifact for detailed build metrics

## Scheduled Execution

The main pipeline runs on a daily schedule at 2 AM UTC. Modify in `.github/workflows/main-test-pipeline.yml`:

```yaml
schedule:
  - cron: '0 2 * * *'  # Change time as needed
```

Cron format: `minute hour day-of-month month day-of-week`

## Local Testing

Test workflows locally before pushing:

```bash
# Install act (GitHub Actions local runner)
# https://github.com/nektos/act

act -j test  # Run specific job
act -l       # List all jobs
```

## Troubleshooting

### Tests not running
- Check Java version: `java -version`
- Verify Maven: `mvn -version`
- Ensure testng.xml is properly configured

### Allure report not generating
- Confirm `allure-results` directory exists
- Check test output for errors

### Slack notifications failing
- Verify webhook URL is correct
- Check firewall/proxy settings

### Artifacts not uploading
- Ensure paths are correct
- Check storage quota
