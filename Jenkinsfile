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
				sh './gradlew clean test'

				echo 'Generating Jacoco coverage report'
				sh './gradlew jacocoTestReport'
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
				sh './gradlew cucumber'
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
					sh './gradlew sonarqube'
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
				sh './gradlew build'

				echo 'Generating Javadoc'
				sh './gradlew javadoc'
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
				sh './gradlew publish'
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
		}

		failure {
			mail to: 'dev-team@mail.com',
			subject: 'Jenkins Pipeline FAILED',
			body: """
                 The Jenkins pipeline has failed.

                 Please check Jenkins logs for details.
                 """
		}
	}
}
