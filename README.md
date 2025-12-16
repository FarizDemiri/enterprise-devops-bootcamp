# 🚀 Enterprise DevOps Reference Implementation

> **Professional Portfolio Showcase**
> *Developed by [Fariz Demiroski]*

---

## 📖 Project Overview

This repository demonstrates a **Production-Grade CI/CD Pipeline** built from first principles. Unlike simple "Hello World" tutorials, this project simulates a real-world enterprise environment where software is built, tested, securely stored, and deployed using industry-standard tooling.

The goal is not just to run tools, but to understand the **"Why"** behind every architectural decision—from the choice of **Jenkins** for orchestration to **Nexus** for artifact governance.

## 🏗️ Architecture

The pipeline follows a "Build Once, Deploy Anywhere" philosophy using immutable Docker containers.

```mermaid
graph LR
    Dev[Developer] -->|Push Code| Git[GitHub Repo]
    Git -->|Webhook/Poll| Jenkins[Jenkins CI]
    
    subgraph "The Factory (CI)"
        Jenkins -->|1. Checkout| Code[Source Code]
        Jenkins -->|2. Build & Test| Maven[Maven Build]
        Jenkins -->|3. Dockerize| Image[Docker Image]
        Jenkins -->|4. Publish| Nexus[Nexus Repo]
    end
    
    subgraph "The Warehouse"
        Nexus -->|Store| JAR[Enterprise Releases (Maven)]
        Nexus -->|Store| DImg[Docker Registry]
    end

    subgraph "The Cloud (CD)"
        Ansible[Ansible Config] -->|Deploy| EC2[AWS EC2]
        EC2 -->|Pull Image| Nexus
    end
```

---

## 🛠️ Technology Stack & Decisions

| Component | Technology | Rationale (The "Why") |
|-----------|------------|-----------------------|
| **Application** | Java 17, Spring Boot | **Type Safety & Ecology**: The standard for enterprise backends. Robust testing framework (JUnit) and built-in health/metrics (`actuator`). |
| **CI Orchestrator** | Jenkins (LTS) | **Flexibility**: Unlike SaaS CI (CircleCI/GitHub Actions), Jenkins allows complete control over the execution environment. We run it as **Docker-in-Docker** for ephemeral build agents. |
| **Artifact Storage** | Sonatype Nexus 3 | **Governance**: Simply building an artifact isn't enough. We need a "Single Source of Truth". Nexus proxies external dependencies (caching Maven Central) and hosts our private internal releases. |
| **Containerization** | Docker | **Immutability**: Eliminates drift. The exact byte-for-byte image built in CI is what runs in Production. |
| **IaaC** | Terraform / Ansible | **Reproducibility**: Infrastructure is code. We treat our servers like cattle, not pets. |

---

## ⚡ The Loop: From Code to Cloud

### 1. The Application (`/app`)

A **Spring Boot** Microservice.

* **Key Feature**: Exposes generic health endpoints (`/actuator/health`) that allow the orchestration layer (Kubernetes/AWS Load Balancers) to know if the app is alive.
* **Testing**: Includes unit tests that run on every build. If tests fail, the pipeline stops immediately.

### 2. The Build Factory (`/jenkins`)

A containerized Jenkins controller.

* **Configuration**: We define the pipeline in code (`Jenkinsfile`). This ensures the build process is versioned just like the application code.
* **Docker Integration**: The Jenkins container mounts the host's Docker socket (`/var/run/docker.sock`), allowing it to spawn sibling containers for build tasks.

### 3. The Warehouse (`/nexus`)

A central repository manager.

* **Maven Hosted**: Stores the raw `.jar` files.
* **Docker Hosted**: Acts as a private registry (similar to DockerHub) for our proprietary images.
* **Security**: Anonymous access is disabled. The CI server authenticates using a dedicated service account.

---

## 🚦 How to Run Locally

This project is designed to be spin-up capable on a local workstation using Docker Compose.

### Prerequisites

* Docker Desktop (or Docker Engine)
* Git

### Steps

1. **Start the Infrastructure**

    ```bash
    # Start Nexus (The Warehouse)
    docker-compose -f nexus/docker-compose.yml up -d
    
    # Start Jenkins (The Factory)
    docker-compose -f jenkins/docker-compose.yml up -d
    ```

2. **Access the Consoles**
    * **Jenkins**: `http://localhost:8083`
    * **Nexus**: `http://localhost:8081`

3. **Run the App (Standalone)**

    ```bash
    cd app
    mvn spring-boot:run
    ```

---

## 🔮 Future Roadmap

* **Milestone 7**: Ansible Configuration Management (Automated Deployment)
* **Milestone 8**: Kubernetes (Orchestration on AWS EKS)
* **Milestone 10**: Monitoring Stack (Prometheus/Grafana)
