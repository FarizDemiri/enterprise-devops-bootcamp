# Enterprise DevOps Pipeline

> A production-grade CI/CD implementation demonstrating the complete software supply chain—from commit to Kubernetes deployment.

[![Build](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/FarizDemiri/enterprise-devops-bootcamp)
[![Progress](https://img.shields.io/badge/progress-75%25-green)](PROGRESS.md)
[![Kubernetes](https://img.shields.io/badge/AWS-EKS-orange)](terraform/)

---

## The Problem

Most teams struggle with three things:

1. **"It works on my machine"** — Code that runs locally breaks in production because environments differ.
2. **"Who deployed what?"** — No audit trail of which version is running where, making rollbacks a nightmare.
3. **"Manual deployments"** — SSH into servers, pull images, restart containers, hope nothing breaks. This doesn't scale.

These aren't tool problems. They're **process problems** that tools can solve—if you understand why each tool exists.

---

## The Solution

An automated pipeline where:

- Every commit triggers a build (no manual intervention)
- Every build produces a versioned, immutable artifact (Docker image)
- Every artifact is stored in a governed registry (Nexus)
- Every deployment pulls from that registry (Kubernetes)

**The result**: I can tell you exactly which commit is running in production, roll back in seconds, and deploy 50 times a day without SSH.

---

## Architecture

```mermaid
graph LR
    A[Developer] -->|git push| B[GitHub]
    B -->|webhook| C[Jenkins]
    
    subgraph "CI Pipeline"
        C -->|1| D[Build JAR]
        D -->|2| E[Run Tests]
        E -->|3| F[Build Image]
        F -->|4| G[Push to Nexus]
        G -->|5| H[Bump Version]
    end
    
    subgraph "CD Pipeline"
        I[Kubernetes] -->|pull| G
        I --> J[Running Pods]
    end
    
    J --> K[Users]
```

### The Flow

| Step | What Happens | Tool |
|------|--------------|------|
| 1 | Developer pushes code | Git |
| 2 | Webhook notifies CI server | Smee → Jenkins |
| 3 | Code is compiled and tested | Maven |
| 4 | Application is packaged into container | Docker |
| 5 | Image is pushed with semantic version | Nexus |
| 6 | Version is committed back to repo | Jenkins → Git |
| 7 | Kubernetes pulls and deploys | Minikube / EKS |

---

## Tech Stack

| Layer | Tool | Why This Tool |
|-------|------|---------------|
| **Application** | Java 17, Spring Boot | Enterprise standard. Built-in health endpoints (`/actuator`), mature testing ecosystem. |
| **Build** | Maven | Reproducible builds. Dependency resolution. Industry standard for Java. |
| **CI Server** | Jenkins | Teaches how CI actually works—agents, executors, pipelines—not just YAML. |
| **Artifacts** | Sonatype Nexus | Single source of truth. Hosts both Maven JARs and Docker images. Audit trail. |
| **Containers** | Docker | Immutable deployments. Eliminates environment drift. |
| **Orchestration** | Kubernetes (EKS) | Self-healing, declarative, industry standard. Moved from Minikube (Toy) to EKS (Production). |
| **Infrastructure** | Terraform | Infrastructure as Code. Defines the VPC, Subnets, and Cluster in `.tf` files. |
| **Packaging** | Helm | The "App Store" for Kubernetes. Templates our YAMLs for multi-environment support. |
| **Observability** | Prometheus/Grafana | The "CCTV". Metrics collection and visualization. "Flying blind" is over. |
| **Webhooks** | Smee.io | Tunnels GitHub webhooks to localhost for instant CI triggers. |

---

## What I Built (and What Broke)

Real learning comes from debugging. Here's what actually happened:

| Challenge | Symptom | Root Cause | Fix | Lesson |
|-----------|---------|------------|-----|--------|
| **Security Groups** | Browser timeout after deploy | AWS blocks all ports by default | Added inbound rule for 8080 | Cloud is "deny by default" |
| **Docker-in-Docker** | Jenkins can't build images | No Docker daemon in container | Mounted `/var/run/docker.sock` | Trade isolation for simplicity |
| **Nexus 401** | Push rejected | Missing credentials | Configured `settings.xml` + Jenkins credentials | Never hardcode secrets |
| **Infinite loops** | Pipeline triggers itself | Version commit triggers webhook | Added `[skip ci]` to commit message | CI commits need escape hatches |
| **Air gap** | EKS can't pull from localhost Nexus | Localhost doesn't exist in cloud | Pushed to Docker Hub as intermediary | Local registries need exposure |
| **Version Hell** | Terraform Provider Conflicts | v6.0 broke v5.0 code | Pinned versions in `main.tf` | Lock your dependencies |
| **Blindness** | App crashing silently | No logs/metrics | Installed Prometheus Stack | You can't fix what you can't see |

---

## Project Status

```
[███████████████░░░░░] 75% Complete
```

| Milestone | Status | Description |
|-----------|--------|-------------|
| ✅ M1-2 | Complete | Spring Boot app, Maven build, unit tests |
| ✅ M3 | Complete | Multi-stage Dockerfile, Docker Compose |
| ✅ M4 | Complete | AWS EC2 manual deployment |
| ✅ M5 | Complete | Nexus artifact repository |
| ✅ M6 | Complete | Jenkins pipeline (multibranch, shared library, webhooks, versioning) |
| ✅ M7 | Complete | Kubernetes on Minikube |
| ✅ M8 | Complete | Kubernetes on AWS EKS |
| ✅ M9 | Complete | Infrastructure as Code (Terraform) |
| ✅ M10 | Complete | Helm Packaging (Charts, Values, Templates) |
| ✅ M11 | Complete | Observability (Prometheus & Grafana) |
| 🔄 M12 | In Progress | GitOps (Argo CD) |
| ⬜ M13-14 | Planned | Security scanning, Final Polish |

→ [Full checklist in PROGRESS.md](PROGRESS.md)

---

## Run It Yourself

### Prerequisites

- Docker Desktop
- Java 17
- Maven 3.8+
- (Optional) Minikube for Kubernetes

### Quick Start: CI Infrastructure

```bash
# Clone
git clone https://github.com/FarizDemiri/enterprise-devops-bootcamp.git
cd enterprise-devops-bootcamp

# Start Jenkins + Nexus
docker-compose -f jenkins/docker-compose.yml up -d
docker-compose -f nexus/docker-compose.yml up -d

# Access
# Jenkins: http://localhost:8083
# Nexus:   http://localhost:8081 (admin / admin123)
```

### Quick Start: The Application

```bash
cd app
mvn spring-boot:run
# http://localhost:8080/actuator/health
```

### Quick Start: Kubernetes (EKS + Helm)

```bash
# 1. Provision Infrastructure
cd terraform && terraform apply

# 2. Deploy Monitoring Stack
helm install my-prometheus prometheus-community/kube-prometheus-stack --namespace monitoring

# 3. Deploy Application
helm install my-app ./charts/enterprise-app
```

### Full Setup Guide

For complete setup including Jenkins credentials, Nexus repositories, and webhook configuration, see [docs/SETUP.md](docs/SETUP.md).

---

## Documentation

| Document | Purpose |
|----------|---------|
| [PROGRESS.md](PROGRESS.md) | Detailed checklist, technical debt tracking |
| [docs/LEARNING_JOURNEY.md](docs/LEARNING_JOURNEY.md) | Plain-language explanations of every concept |
| docs/SETUP.md | Complete setup instructions *(coming soon)* |

---

## Technical Debt

I'm tracking these intentional shortcuts:

| Severity | Item | Why | Remediation |
|----------|------|-----|-------------|
| 🔴 High | Plaintext passwords | Lab speed | HashiCorp Vault |
| 🟠 Med | Root user in containers | Docker socket permissions | Dedicated jenkins user |
| 🟠 Med | HTTP only | No certs on localhost | Nginx + Let's Encrypt |
| 🟡 Low | No Nexus backup | Ephemeral lab | S3 blob store |

---

## What's Next

- [x] Deploy to AWS EKS (Milestone 8)
- [x] Terraform for VPC/EKS provisioning (Milestone 9)
- [x] Helm Packaging (Milestone 10)
- [x] Prometheus + Grafana monitoring (Milestone 11)
- [ ] Argo CD for GitOps (Milestone 12)
- [ ] Trivy security scanning (Milestone 13)

---

## Connect

**Fariz Demiri** — Learning DevOps by building, not watching.

- GitHub: [github.com/FarizDemiri](https://github.com/FarizDemiri)
- LinkedIn: [linkedin.com/in/FarizDemiri](https://linkedin.com/in/FarizDemiri)
- Email: <Farizdemiri@gmail.com>

---

<p align="center"><i>Built with curiosity, debugged with patience, documented with care.</i></p>
