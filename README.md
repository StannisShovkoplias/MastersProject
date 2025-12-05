# 🥇 Winner of INT20H Hackathon – MainBackend

This project **won 1st place** at the **INT20H Hackathon** 🏆  
It showcases the integration of AI (ChatGPT) with GitHub to fully automate project scaffolding, repository creation, and architecture generation.

---

# MainBackend

**MainBackend** is the backend service that powers an AI-driven assistant (based on ChatGPT) capable of fully managing and generating GitHub repositories. It allows seamless integration with your GitHub account and enables the AI to:

- Analyze and manage your existing repositories
- Automatically create new repositories with complete project architecture
- Generate full file and directory structures for real-world applications

---

## 🚀 Features

- 🔗 **GitHub Integration** – Authenticate and link your GitHub account
- 🤖 **AI Assistant (ChatGPT)** – Conversational assistant that helps with project setup, ideas, and coding
- 🏗️ **Auto Project Generation** – Generate a complete GitHub repository with full structure and content
- 📂 **Architecture Generator** – Create projects with best-practice layered architecture and file hierarchy

---

## 📦 Tech Stack

- **Java** / **Spring Boot** – Backend framework
- **Gradle** – Build system
- **GitHub REST API** – Repository management
- **OpenAI API** – AI assistant (ChatGPT)
- **PostgreSQL** – Database
- **JWT Authentication** – Secure access
- **Docker** – Containerized deployment

---

## 🔧 Getting Started

### Prerequisites

- Java 17+
- Gradle 7.5+ (or use the wrapper)
- PostgreSQL database
- GitHub Developer App (for OAuth)
- OpenAI API key

### Setup Instructions

```bash
# Clone the repository
git clone https://github.com/SigmaDevsTeam/MainBackend.git
cd MainBackend

# Create a `.env` or configure `application.yml` with the following:
# - GitHub OAuth credentials
# - OpenAI API key
# - PostgreSQL database config
# - JWT secret

# Build and run the app
./gradlew bootRun
