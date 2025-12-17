# 🚀 Enterprise DevOps: The "Software Supply Chain"
>
> **A Production-Grade CI/CD & Kubernetes Implementation**

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/FarizDemiri/enterprise-devops-bootcamp)
[![Kubernetes](https://img.shields.io/badge/k8s-EKS-blue)](https://aws.amazon.com/eks/)
[![Infrastructure](https://img.shields.io/badge/IaC-Terraform-purple)](terraform/)
[![Progress](https://img.shields.io/badge/Milestone-8%2F14-orange)](PROGRESS.md)

---

## 🎯 What This Project Demonstrates

This isn't a "Hello World" deployment. This repository simulates a **real enterprise environment** where:

* **Fragmentation is solved**: Developers, Ops, and Security work in a unified pipeline.
* **Drfit is eliminated**: Infrastructure is code, ensuring Prod matches Dev.
* **Governance is enforced**: All artifacts pass through a centralized warehouse (Nexus).
* **Deployments are immutable**: Docker containers ensure "Write Once, Run Anywhere".

**The goal**: Prove understanding of the *"Why"* behind every tool, not just the *"How"*.

---

## 🏗️ Architecture: "From Farm to Table"

We treat software delivery like a global supply chain.

```mermaid
graph LR
    A[The Farm (Code)] -->|Git Push| B(The Factory (Jenkins))
    B -->|Test & Bake| C[The Warehouse (Nexus)]
    C -.->|Export| D[The Public Port (Docker Hub)]
    D -->|Import| E[The Restaurant (AWS EKS)]
    E -->|Serve| F[The Customer (Internet)]
```

### The Flow (Plain English)

1. **The Farm (Code)**: Java Spring Boot app with Maven.
2. **The Factory (CI)**: Jenkins detects the commit, runs Unit Tests, builds the JAR, and packages the **Docker Image**.
3. **The Warehouse (Artifacts)**: Nexus stores the immutable artifacts (Versioning).
4. **The Restaurant (CD)**: AWS EKS pulls the image and deploys it to the public via LoadBalancer.

---

## 💻 Technology Stack

| Component | Technology | Why I Chose It (Enterprise Logic) |
| :--- | :--- | :--- |
| **Application** | Java 17, Spring Boot | Standard for high-performance microservices. Type safety & mature testing (JUnit). |
| **CI Server** | Jenkins (LTS) | Unlike GitHub Actions, Jenkins teaches *how CI works under the hood* (Agents, Executors, Docker-in-Docker). |
| **Artifact Storage** | Sonatype Nexus 3 | "Single Source of Truth". Proxies Maven Central & hosts private Docker Registry. |
| **Containerization** | Docker | Eliminates "works on my machine". Same bytes in dev = same bytes in prod. |
| **Orchestrator** | AWS EKS (Kubernetes) | Self-healing, auto-scaling, production-grade container management. |
| **IaC** | Terraform *(Planned)* | Declarative infrastructure. Defines the Virtual Datacenter (VPC) as code. |

---

## 📊 Monitoring Strategy (The Vital Signs)

*How we ensure reliability (Planned for Milestone 11)*

1. **Prometheus (The Doctor)**: Scrapes `/actuator/prometheus` metrics from Pods every 15s.
2. **Grafana (The Chart)**: Visualizes CPU, Memory, and Request Latency to spot trends.
3. **AlertManager (The Pager)**: Pings on-call engineers if Error Rate > 1% or Latency > 500ms.

---

## 💡 Key Learnings & Debugging Stories

Real learning comes from fixing things that broke.

| Challenge | What Happened | How I Fixed It | The Lesson |
| :--- | :--- | :--- | :--- |
| **The "Air Gap"** | EKS couldn't pull images from local Nexus. | Pushed to Docker Hub as an intermediary. | Localhost doesn't exist in the Cloud. |
| **Security Groups** | App deployed but browser timed out. | Updated AWS Inbound Rules (Port 80/8080). | Cloud networking is "Deny by Default". |
| **Docker-in-Docker** | Jenkins couldn't build images. | Mounted `/var/run/docker.sock`. | Trade-off: Less isolation for simpler architecture. |
| **Nexus Auth** | `401 Unauthorized` on push. | Configured `settings.xml` credentials. | Never hardcode secrets; use credential managers. |
| **Drift** | Manual EC2 server became "Snowflake". | Switched to EKS + IaC. | Servers should be "Cattle, not Pets". |

---

## 📸 Screenshots

<details>
<summary>👀 Click to see the Pipeline in Action</summary>

### Jenkins Pipeline

![Jenkins Pipeline](docs/images/jenkins-pipeline.png)
*Automated build → test → dockerize → push flow*

### Nexus Repository

![Nexus Repos](docs/images/nexus-repos.png)
*Maven releases and Docker images stored centrally*

### Application Health Check

![App Health](docs/images/app-health.png)
*Spring Actuator endpoint confirming app is alive*
</details>

---

## 🚦 How to Run the "Local Factory"

If you want to replicate the CI/CD environment locally:

### Prerequisites

* Docker Desktop
* Java 17 & Maven 3.8+

### Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/FarizDemiri/enterprise-devops-bootcamp.git

# 2. Start the Tools (Jenkins & Nexus)
docker-compose -f jenkins/docker-compose.yml up -d
docker-compose -f nexus/docker-compose.yml up -d

# 3. Access the Consoles
# Jenkins: http://localhost:8083 (User: admin)
# Nexus:   http://localhost:8081 (User: admin / admin123)
```

**⚠️ Note**: This spins up the *Build Factory*. To run the *App*, use:

```bash
cd app && mvn spring-boot:run
```

---

## 🔮 Roadmap & Progress

```
[██████████████░░░░░░] 60% Complete (Milestone 8)
```

* [x] **Foundation**: Java App + Docker + Unit Tests
* [x] **CI Pipeline**: Jenkins + Nexus + Versioning
* [x] **Cloud CD**: AWS EKS Deployment
* [ ] **IaC**: Terraform VPC Provisioning
* [ ] **Observability**: Prometheus/Grafana Stack
* [ ] **Security**: Image Scanning (Trivy)

---

## 📬 Connect

**Fariz Demiri** - *DevOps Engineer*

* **GitHub**: [github.com/FarizDemiri](https://github.com/FarizDemiri)
* **LinkedIn**: [linkedin.com/in/FarizDemiri](https://linkedin.com/in/FarizDemiri)
* **Email**: <Farizdemiri@gmail.com>

> *"Built with curiosity, debugged with patience, documented with care."*
