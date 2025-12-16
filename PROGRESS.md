# Project Progress Tracker
>
> **Last Updated**: 2025-12-15
> **Paste this at the start of each AI session**

---

## 📍 Current Status

| Field | Value |
|-------|-------|
| **Milestone** | 6 - Jenkins CI/CD Pipeline |
| **Phase** | CI/CD |
| **Step Number** | 6.1.0 |
| **Overall Completion** | 45% |
| **Blockers** | None |

---

## 📉 Technical Debt Registry

> **Mature Engineering** means acknowledging trade-offs. I am tracking these items to be addressed before "True Production".

| Severity | Item | Rationale (Why we did it) | Remediation Plan |
|:---:|---|---|---|
| 🔴 **High** | **Secrets in Plaintext** | `admin123` used for lab speed. | Integrate HashiCorp Vault or Jenkins Credentials Provider properly. |
| 🟠 **Med** | **Root User in Containers** | Simplifies permission issues in Docker-in-Docker. | Create dedicated `jenkins` user in Dockerfile with specific UID/GID mapping. |
| 🟠 **Med** | **HTTP Only** | SSL Certificates (Let's Encrypt) are hard on `localhost`. | Set up Nginx Reverse Proxy with self-signed certs for local, Real certs for Cloud. |
| 🟡 **Low** | **No Nexus Backup** | Ephemeral lab environment. | Mount Nexus `blob-store` to AWS S3 or persistent block storage. |

---

## ✅ Completion Checklist

### Milestone 0: Decisions & Prerequisites

- [x] 0.1 Chose application language: **Java (Spring Boot)**
- [x] 0.2 Chose local K8s: **Minikube**
- [x] 0.3 Chose Jenkins hosting: **Local Docker**
- [x] 0.4 Verified prerequisites installed
- [x] 0.5 Created GitHub repo

### Milestone 1: Repository Scaffold + Git

- [x] 1.1 Created folder structure
- [x] 1.2 Created README skeleton
- [x] 1.3 Created .gitignore
- [x] 1.4 Created docs/ skeleton
- [x] 1.5 Made initial commit

### Milestone 2: Application + Build Tools

- [x] 2.1 Created Java/Node app structure
- [x] 2.2 Implemented health endpoints
- [x] 2.3 Wrote unit tests
- [x] 2.4 Configured Maven/Gradle
- [x] 2.5 Built JAR locally
- [x] 2.6 Tests passing with coverage

### Milestone 3: Docker + Docker Compose

- [x] 3.1 Created Dockerfile (multi-stage)
- [x] 3.2 Created docker-compose.yml
- [x] 3.3 Added database service
- [x] 3.4 Configured volumes for persistence
- [x] 3.5 App runs with `docker-compose up`

### Milestone 4: Cloud Basics + Deploy

- [x] 4.1 Created cloud server (DO/AWS)
- [x] 4.2 Configured security groups
- [x] 4.3 Created non-root user
- [x] 4.4 Installed Docker on server
- [x] 4.5 Deployed app manually via SSH

### Milestone 5: Artifact Repository with Nexus

- [x] 5.1 Deployed Nexus locally via Docker
- [x] 5.2 Configured Maven & Docker Hosted Repositories
- [x] 5.3 Published JAR and Docker Image to Nexus

### Milestone 6: Jenkins CI/CD Pipeline

#### Phase 6a: Jenkins Setup

- [x] 6a.1 Created Jenkins Docker container
- [x] 6a.2 Completed initial Jenkins setup
- [x] 6a.3 Installed required plugins
- [x] 6a.4 Created credentials (Git, Nexus)

#### Phase 6b: Freestyle Job

- [x] 6b.1 Created Freestyle job
- [x] 6b.2 Configured Git repo connection
- [x] 6b.3 Added Maven build step
- [x] 6b.4 Job runs successfully

#### Phase 6c: Pipeline Job (Jenkinsfile)

- [x] 6c.1 Created basic Jenkinsfile
- [x] 6c.2 Added Build stage
- [x] 6c.3 Added Test stage
- [x] 6c.4 Added Docker Build stage
- [x] 6c.5 Added Push to Nexus stage
- [x] 6c.6 Pipeline runs end-to-end

#### Phase 6d: Multibranch Pipeline

- [x] 6d.1 Converted to Multibranch Pipeline
- [x] 6d.2 Configured branch discovery
- [x] 6d.3 Feature branch behavior working

#### Phase 6e: Shared Library

- [ ] 6e.1 Created shared library repo
- [ ] 6e.2 Created reusable functions
- [ ] 6e.3 Integrated into Jenkinsfile

#### Phase 6f: Webhooks

- [ ] 6f.1 Configured webhook in Git
- [ ] 6f.2 Jenkins triggers on push

#### Phase 6g: Dynamic Versioning

- [ ] 6g.1 Auto-increment version
- [ ] 6g.2 Commit version back to Git

### Milestone 7: Kubernetes Fundamentals

- [ ] 7.1 Installed Minikube
- [ ] 7.2 Created Deployment manifest
- [ ] 7.3 Created Service manifest
- [ ] 7.4 Created ConfigMap/Secret
- [ ] 7.5 Configured health probes
- [ ] 7.6 App running in Minikube

### Milestone 8: Kubernetes on AWS (EKS)

- [ ] 8.1 Created EKS cluster
- [ ] 8.2 Configured kubectl
- [ ] 8.3 Deployed app to EKS
- [ ] 8.4 Configured ECR
- [ ] 8.5 Jenkins deploys to EKS

### Milestone 9: Terraform

- [ ] 9.1 Created Terraform project structure
- [ ] 9.2 Provisioned VPC
- [ ] 9.3 Provisioned EC2
- [ ] 9.4 Created modules
- [ ] 9.5 Configured remote state

### Milestone 10: Ansible

- [ ] 10.1 Created inventory
- [ ] 10.2 Created Docker playbook
- [ ] 10.3 Created deploy playbook
- [ ] 10.4 Created roles
- [ ] 10.5 Integrated with Terraform

### Milestone 11: Monitoring

- [ ] 11.1 Deployed Prometheus stack
- [ ] 11.2 Configured app metrics
- [ ] 11.3 Created Grafana dashboards
- [ ] 11.4 Configured alerts

### Milestone 12: GitOps (Argo CD)

- [ ] 12.1 Installed Argo CD
- [ ] 12.2 Created Application manifests
- [ ] 12.3 Configured sync policies
- [ ] 12.4 Kustomize overlays working

### Milestone 13: Progressive Delivery + Security

- [ ] 13.1 Configured Argo Rollouts
- [ ] 13.2 Created Kyverno policies
- [ ] 13.3 Added Trivy scanning
- [ ] 13.4 Added Cosign signing
- [ ] 13.5 Generated SBOM

### Milestone 14: Documentation

- [ ] 14.1 Completed ARCHITECTURE.md
- [ ] 14.2 Completed EXPLAIN-LIKE-IM-12.md
- [ ] 14.3 Completed SECURITY-POSTURE.md
- [ ] 14.4 Completed RUNBOOK.md
- [ ] 14.5 Completed INTERVIEW-PREP.md
- [ ] 14.6 Polished README.md

---

## 📁 Files Created/Modified

| File Path | Status | Notes |
|-----------|--------|-------|
| `/app/pom.xml` | **Completed** | M5: Nexus config |
| `/app/Dockerfile` | **Completed** | M3: Multi-stage build |
| `/jenkins/Jenkinsfile` | **Completed** | M6: CI Pipeline |
| `/nexus/docker-compose.yml` | **Completed** | M5: Artifact Store |
| `/jenkins/docker-compose.yml` | **Completed** | M6: CI Server |

---

## 📝 Last Session Summary

**Date**: 2025-12-15

**What we accomplished**:

- **Milestone 5 (Nexus)**: Deployed Nexus, configured Repos, published artifacts manually.
- **Milestone 6 (Jenkins)**: Deployed Jenkins, configured Tools (JDK/Maven), Creds (Nexus/Docker).
- **Pipeline**: Created `Jenkinsfile` and ran successful CI build (Build -> Docker -> Push).

**What's next**:

- **Finish Milestone 6**: Advanced Jenkins (Multibranch, Shared Libs, Webhooks).
- **Roadmap Decision**: Decide between **Milestone 7: Kubernetes** (Original Plan) vs **Ansible** (Project Deviation).
  - *Context*: `task.md` suggested Ansible next, but `PROGRESS.md` lists Kubernetes. We will resolve this tomorrow.

**Open questions/blockers**:

- Need to decide if we deploy to EC2-via-Ansible first, or jump straight to Minikube.

---

## 🎯 Key Decisions Log

| Decision | Choice | Alternatives | Rationale |
|---|---|---|---|
| **App Language** | **Java (Spring Boot)** | Node.js, Python | Enterprise standard. Strong typing and Maven ecosystem simulate real-world complexity better than lightweight scripts. |
| **CI Tool** | **Jenkins** | GitHub Actions, CircleCI | "Classic" DevOps. Allows manual configuration of agents (Docker-in-Docker), providing deeper learning than managed SaaS. |
| **Artifact Repo** | **Nexus** | Artifactory, S3 | Industry standard for on-premise artifact governance. Supports both Maven and Docker natively. |
| **Deployment** | **Ansible** (Planned) | Shell Scripts, Chef | Agentless push model is perfect for immutable infrastructure deployment without overhead. |

---
