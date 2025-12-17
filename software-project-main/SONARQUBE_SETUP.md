# SonarQube + GitHub Actions Setup Guide

## 1. GitHub Secrets Setup
Add these secrets to your GitHub repository:

### Required Secrets:
- `SONAR_TOKEN`: Your SonarQube authentication token
- `SONAR_HOST_URL`: Your SonarQube server URL (e.g., https://sonarcloud.io or your self-hosted instance)

### How to add secrets:
1. Go to your GitHub repository
2. Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Add the secrets above

## 2. SonarQube Setup

### For SonarCloud (Free for public projects):
1. Sign up at https://sonarcloud.io
2. Connect your GitHub repository
3. Get your SONAR_TOKEN from SonarCloud settings
4. Set SONAR_HOST_URL to https://sonarcloud.io

### For Self-hosted SonarQube:
1. Set up SonarQube server
2. Create a project in SonarQube
3. Generate a token from SonarQube → My Account → Security
4. Set SONAR_HOST_URL to your server URL

## 3. Coverage Configuration
The setup includes:
- JaCoCo coverage reports
- Exclusions for UI and repository layers
- Quality gate integration
- Coverage upload to Codecov (optional)

## 4. Workflow Triggers
The GitHub Actions workflow triggers on:
- Push to `main` or `develop` branches
- Pull requests to `main` branch

## 5. Quality Gates
SonarQube will check:
- Code coverage (minimum 90% based on your JaCoCo config)
- Code quality metrics
- Security vulnerabilities
- Code smells

## 6. Local Testing
You can test locally:
```bash
mvn clean test jacoco:report
mvn sonar:sonar -Dsonar.token=YOUR_TOKEN
```

## 7. Coverage Reports
- JaCoCo: `target/site/jacoco/index.html`
- SonarQube: Available in your SonarQube dashboard
- GitHub: PR comments with coverage metrics
