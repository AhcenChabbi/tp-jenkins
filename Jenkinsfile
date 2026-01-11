pipeline {
	agent any

	tools {
		jdk 'JDK11'
	}

	environment {
		MAVEN_REPO_URL      = "${env.MAVEN_REPO_URL}"
		MAVEN_REPO_USER     = "${env.MAVEN_REPO_USER}"
		MAVEN_REPO_PASSWORD = "${env.MAVEN_REPO_PASSWORD}"
	}

	stages {

		/* ================================================= */
		/* PHASE 1 : TEST                                    */
		/* ================================================= */
		stage('Test') {
			steps {
				echo 'Running unit tests'
				bat './gradlew clean test'

				echo 'Generating Jacoco coverage report'
				bat './gradlew jacocoTestReport'
			}

			post {
				always {
					echo 'Archiving unit test results'
					junit 'build/test-results/test/*.xml'
				}
			}
		}

		/* ================================================= */
		/* PHASE 2 : CUCUMBER REPORTS                        */
		/* ================================================= */
		stage('Cucumber') {
			steps {
				echo 'Running Cucumber tests'
				bat './gradlew cucumber'
			}

			post {
				always {
					echo 'Publishing Cucumber report'
					cucumber buildStatus: 'UNSTABLE',
					fileIncludePattern: 'build/reports/cucumber/cucumber.json'
				}
			}
		}

		/* ================================================= */
		/* PHASE 3 : CODE ANALYSIS (SONARQUBE)               */
		/* ================================================= */
		stage('Code Analysis') {
			steps {
				echo 'Running SonarQube analysis'
				withSonarQubeEnv('SonarQube') {
					bat './gradlew sonar'
				}
			}
		}

		/* ================================================= */
		/* PHASE 4 : CODE QUALITY (QUALITY GATE)             */
		/* ================================================= */
		stage('Code Quality') {
			steps {
				echo 'Waiting for SonarQube Quality Gate'
				timeout(time: 1, unit: 'MINUTES') {
					waitForQualityGate abortPipeline: true
				}
			}
		}

		/* ================================================= */
		/* PHASE 5 : BUILD                                   */
		/* ================================================= */
		stage('Build') {
			steps {
				echo 'Building JAR'
				bat './gradlew build'

				echo 'Generating Javadoc'
				bat './gradlew javadoc'
			}

			post {
				success {
					echo 'Archiving build artifacts'
					archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
					archiveArtifacts artifacts: 'build/docs/javadoc/**'
				}
			}
		}

		/* ================================================= */
		/* PHASE 6 : DEPLOY                                  */
		/* ================================================= */
		stage('Deploy') {
			steps {
				echo 'Publishing artifact to MyMavenRepo'
				bat './gradlew publish'
			}
		}
	}

	/* ================================================= */
	/* PHASE 7 : NOTIFICATION                             */
	/* ================================================= */
	post {
		success {
			mail to: 'dev-team@mail.com',
			subject: 'Jenkins Pipeline SUCCESS',
			body: """
                 The Jenkins pipeline completed successfully.

                 - Tests: OK
                 - Code Quality: PASSED
                 - Build: OK
                 - Deploy: SUCCESS
                 """


			// Slack
			echo 'Sending Slack success notification'
			bat """
            curl -X POST -H "Content-type: application/json" --data "{\\"text\\": \\"✅ Pipeline SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER} \\n${env.BUILD_URL}\\"}" ${env.SLACK_WEBHOOK}
            """
		}

		failure {
			mail to: 'dev-team@mail.com',
			subject: 'Jenkins Pipeline FAILED',
			body: """
                 The Jenkins pipeline has failed.

                 Please check Jenkins logs for details.
                 """


			// Slack
			echo 'Sending Slack failure notification'
			bat """
            curl -X POST -H "Content-type: application/json" --data "{\\"text\\": \\"❌ Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER} \\n${env.BUILD_URL}\\"}" ${env.SLACK_WEBHOOK}
            """
		}
	}
}
