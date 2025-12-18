# 🏛️ Enterprise DevOps Architecture

## 1. The "Why"

This platform is designed to emulate a **Real-World Banking Environment**.
It moves away from "Toy" setups (Minikube, Shell Scripts) to "Enterprise" standards (EKS, Terraform, GitOps).

## 2. The Components (The Stack)

| Layer | Tool | Purpose |
|-------|------|---------|
| **Infrastructure** | **Terraform** | Defines the AWS VPC, EKS Cluster, and Networking as Code. |
| **Orchestration** | **AWS EKS (K8s)** | Manages the application containers in a production-grade cluster. |
| **Packaging** | **Helm** | Templates the Kubernetes manifests (Deployments, Services) for reusability. |
| **CI (Build)** | **Jenkins** | Compiles Java, Runs Tests, Builds Docker Images. |
| **Artifacts** | **Nexus / DockerHub** | Stores the compiled JARs and Container Images. |
| **Security** | **Trivy** | Scans images for CVEs before they leave the build pipeline. |
| **CD (Deploy)** | **Argo CD** | Syncs the Git state to the Cluster (GitOps). |
| **Observability** | **Prometheus/Grafana** | Monitors metrics and visualized cluster health. |

---

## 3. The Continuous Integration (CI) Pipeline

**Trigger**: Developer pushes code to `main`.

```mermaid
graph LR
    Dev[Developer] -->|Push Code| Git[GitHub]
    Git -->|Webhook| Jenkins[Jenkins CI]
    
    subgraph Jenkins Pipeline
        Build[Maven Build] --> Test[Unit Tests]
        Test --> Scan[Trivy Security Scan]
        Scan --> Package[Docker Build]
        Package --> Push[Docker Push]
    end
    
    Push --> Registry[Docker Hub]
```

---

## 4. The Continuous Delivery (CD) Pipeline (GitOps)

**Trigger**: Developer updates `values.yaml` (e.g., Image Tag).

```mermaid
graph LR
    Dev[Developer] -->|Update Config| Git[GitHub]
    
    subgraph Kubernetes Cluster
        Argo[Argo CD Controller] -->|Polls| Git
        Argo -->|Detects Change| Sync[Sync Status]
        Sync -->|Applies Manifests| K8s[EKS API]
        
        K8s -->|Rolling Update| Pods[Application Pods]
    end
```

---

## 5. The Infrastructure (AWS)

**Managed by**: Terraform.

```mermaid
graph TB
    subgraph AWS Cloud [us-east-1]
        subgraph VPC [10.0.0.0/16]
            IGW[Internet Gateway]
            
            subgraph Public Subnets
                LB[AWS Load Balancer]
                NAT[NAT Gateway]
            end
            
            subgraph Private Subnets
                Node1[EKS Node 1]
                Node2[EKS Node 2]
                Pod1[App Pod]
                Pod2[App Pod]
            end
        end
    end
    
    User -->|HTTPS| LB
    LB -->|Traffic| Node1
```
