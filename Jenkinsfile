// =============================================================================
// Jenkins Pipeline for Test Automation
// =============================================================================
// This pipeline is configured to NOT run automatically.
// It must be triggered manually from Jenkins UI.
// =============================================================================

pipeline {
    agent any
    
    // Disable automatic triggers - manual only
    triggers {
        // Uncomment below to enable automatic triggers
        // pollSCM('H/5 * * * *')  // Poll every 5 minutes
        // cron('H 0 * * *')       // Run daily at midnight
    }
    
    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['smoke', 'regression', 'api', 'all'],
            description: 'Select the test suite to run'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Select the browser for UI tests'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['qa', 'staging', 'production'],
            description: 'Select the environment to test'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )
        booleanParam(
            name: 'SEND_EMAIL',
            defaultValue: false,
            description: 'Send email notification with report'
        )
    }
    
    environment {
        JAVA_HOME = tool 'JDK11'
        MAVEN_HOME = tool 'Maven3'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
        
        // Credentials - Configure these in Jenkins Credentials
        BASE_URL = credentials('base-url')
        API_BASE_URL = credentials('api-base-url')
        TEST_USERNAME = credentials('test-username')
        TEST_PASSWORD = credentials('test-password')
    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 60, unit: 'MINUTES')
        timestamps()
        ansiColor('xterm')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Checked out branch: ${env.BRANCH_NAME ?: 'N/A'}"
            }
        }
        
        stage('Setup') {
            steps {
                echo "Setting up test environment..."
                sh '''
                    java -version
                    mvn -version
                '''
            }
        }
        
        stage('Build') {
            steps {
                echo "Building project..."
                sh 'mvn clean compile -DskipTests'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo "Running ${params.TEST_SUITE} tests on ${params.BROWSER}..."
                sh """
                    mvn test \
                        -P${params.TEST_SUITE} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS} \
                        -Denvironment=${params.ENVIRONMENT}
                """
            }
            post {
                always {
                    // Archive test results
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                echo "Generating Allure report..."
                sh 'mvn allure:report'
            }
            post {
                always {
                    // Publish Allure report
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
        
        stage('Archive Artifacts') {
            steps {
                echo "Archiving test artifacts..."
                archiveArtifacts artifacts: 'target/reports/**/*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/screenshots/**/*', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/logs/**/*', allowEmptyArchive: true
            }
        }
        
        stage('Send Notification') {
            when {
                expression { params.SEND_EMAIL == true }
            }
            steps {
                echo "Sending email notification..."
                // Email notification - configure SMTP in Jenkins
                emailext (
                    subject: "Test Execution Report - ${currentBuild.currentResult}",
                    body: """
                        <h2>Test Execution Summary</h2>
                        <p><b>Build:</b> ${env.BUILD_NUMBER}</p>
                        <p><b>Status:</b> ${currentBuild.currentResult}</p>
                        <p><b>Test Suite:</b> ${params.TEST_SUITE}</p>
                        <p><b>Browser:</b> ${params.BROWSER}</p>
                        <p><b>Environment:</b> ${params.ENVIRONMENT}</p>
                        <p><b>Duration:</b> ${currentBuild.durationString}</p>
                        <p>Please check the attached report for details.</p>
                    """,
                    to: '${EMAIL_RECIPIENTS}',
                    attachmentsPattern: 'target/reports/*.html',
                    mimeType: 'text/html'
                )
            }
        }
    }
    
    post {
        always {
            echo "Cleaning up workspace..."
            cleanWs()
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed! Check the logs for details."
        }
        unstable {
            echo "Pipeline is unstable. Some tests may have failed."
        }
    }
}
