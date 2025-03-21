pipeline {
    agent any

    environment {
        OPEN_AI_KEY = credentials('open-ai-key')
    }

    stages {
        stage('Build image and run with docker compose') {
            steps {
                script {
                    sh "QUARKUS_LANGCHAIN4J_OPENAI_API_KEY=$OPEN_AI_KEY docker compose up -d --build"
                }
            }
        }
    }
}