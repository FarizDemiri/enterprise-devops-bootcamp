# My DevOps Journey: From Code to Cloud

> **Timeline**: Built in ~5 days (Dec 2025)  
> **Purpose**: Document what I actually learned, not just what I did.

This isn't a tutorial. It's a record of how I went from "I know Docker exists" to a working CI/CD pipeline with Kubernetes—including everything that broke along the way.

---

## The Map

| Part | Milestone | Concept | Time Spent |
|------|-----------|---------|------------|
| 1 | M1-2 | Code → Artifact | ~3 hours |
| 2 | M3 | Containers | ~2 hours |
| 3 | M4 | Cloud Deployment | ~4 hours |
| 4 | — | Networking (Debugging) | ~2 hours |
| 5 | M5 | Artifact Storage | ~3 hours |
| 6 | M6 | CI/CD Automation | ~8 hours |
| 7 | M7 | Container Orchestration | ~4 hours |
| 8 | — | The Full Picture | — |

---

## Part 1: The Blueprint & The Assembly

**Milestones 1-2 | Concept: Code vs. Artifacts**

I wrote text files in Java. That's all source code is—human-readable instructions that can't actually *do* anything yet.

Then I ran `mvn package`. Maven took my instructions and assembled them into a single executable file: the **JAR**.

> **Analogy**: I wrote a recipe (Code), and Maven baked the cake (JAR).

**What clicked**: The JAR is the *product*. The code is just the *blueprint*. Everything downstream (Docker, Kubernetes, production) only cares about the JAR.

---

## Part 2: The Standardization

**Milestone 3 | Concept: "Works on my machine" → Containers**

I ran `docker build`. This took my JAR and put it inside a sealed box along with everything it needs to run—the OS, the Java runtime, the exact dependencies.

> **Analogy**: The JAR is a cake. The Docker image is a **TV Dinner**—sealed, complete, just needs a microwave (Docker) to heat up.

Then I learned that applications are never just code. They're **Code + Data**. Docker Compose let me define a universe where my app and its database live together.

> **Analogy**: Instead of building custom furniture in my living room, I designed a flat-pack IKEA box that contains everything needed for assembly anywhere.

**What clicked**: The multi-stage Dockerfile. Build in a fat image (with Maven, compilers, tools), then copy *only* the JAR into a tiny runtime image. The final image went from 500MB to 80MB.

---

## Part 3: The Distribution

**Milestone 4 | Concept: Local → Cloud**

I rented a computer in the cloud (EC2). It had an IP address but was completely empty.

The process:

1. **Provision**: Click through AWS console, pick an instance type, launch
2. **SSH in**: `ssh -i key.pem ubuntu@<ip>` — I'm now inside a server in Virginia
3. **Install Docker**: Same commands I'd run locally
4. **Copy files**: `scp docker-compose.yml` to the server
5. **Deploy**: `docker-compose up -d`

**What clicked**: The cloud isn't magic. It's just someone else's computer with a good API. Everything I did locally, I can do remotely.

---

## Part 4: The Bouncer

**Concept: Networking & Security Groups**

This is where I hit my first real wall.

**The situation**: App deployed. Docker running. Logs look healthy.

**The symptom**: Browser shows "Connection Timeout."

**The debugging**:

- Is the app running? Yes (`docker ps` shows it up)
- Is it listening? Yes (`curl localhost:8080` inside the server works)
- Is it the network? ...

**The root cause**: AWS Security Groups. By default, all inbound traffic is blocked. I'd allowed SSH (port 22) but not my app (port 8080).

**The fix**: Add inbound rule for port 8080 from 0.0.0.0/0.

> **Analogy**: The cloud comes with a Bouncer (Security Group). I told him "only let people in the front door (22) and staff entrance (80)." My app was serving from the side door (8080). The Bouncer had no instructions for that door, so he ignored all knocking.

**What clicked**: Cloud networking is "deny by default." If you don't explicitly allow it, it doesn't exist. This is good for security, confusing for debugging.

---

## Part 5: The Warehouse

**Milestone 5 | Concept: Artifact Governance**

I spun up Nexus—a dedicated server to store my artifacts.

Why not just rebuild from source every time? Because:

- Builds can be non-deterministic (different timestamps, different dependency resolutions)
- You lose the audit trail ("what exact bytes are running in prod?")
- It's slow

Nexus gives me:

- **Maven repository**: Stores the JAR files
- **Docker registry**: Stores the container images
- **Version history**: Every build is immutable and traceable

> **Analogy**: Nexus is the **Freezer Warehouse**. Instead of baking a fresh cake every time someone wants dessert, I bake once, label it with a date, and store it. When someone orders, I ship the exact same frozen cake. Same bytes, every time.

**What broke**: `401 Unauthorized` when pushing.

**Root cause**: I hadn't configured credentials. Maven didn't know how to authenticate.

**Fix**: Created `settings.xml` with Nexus credentials, added to Jenkins.

**What clicked**: Artifacts need governance. "Which version is in prod?" should never be a mystery.

---

## Part 6: The Automated Factory

**Milestone 6 | Concept: CI/CD**

This is where everything connected.

I stopped being the one who runs `mvn package` and `docker build`. I hired a robot (Jenkins) to do it for me.

**The setup**:

1. Jenkins runs in Docker (on my laptop)
2. I give it tools: Maven, Docker, credentials for Nexus and GitHub
3. I write a `Jenkinsfile`—a script that defines the assembly line

**The pipeline**:

```
Checkout → Build JAR → Run Tests → Build Image → Push to Nexus → Update Version
```

> **Analogy**: I hired a Robot Chef. I don't shout orders anymore—I wrote a recipe book (Jenkinsfile) and the robot follows it perfectly every time. No typos, no forgotten steps, no "I thought I ran the tests."

**What broke (a lot)**:

| Problem | Symptom | Fix |
|---------|---------|-----|
| Docker-in-Docker | Jenkins can't run `docker build` | Mount host's `/var/run/docker.sock` |
| Credentials | 401 on push | Configure Jenkins credentials, not hardcoded |
| Infinite loops | Pipeline triggers itself forever | Add `[skip ci]` to version bump commits |
| Webhook delivery | GitHub can't reach localhost | Use Smee.io as a tunnel |

---

### Part 6.5: The Smart Factory

**Phase 6d | Concept: Multibranch Pipelines**

One pipeline for everything? Bad idea.

I don't want feature branch experiments polluting my production artifact registry.

**The upgrade**: Multibranch Pipeline. Jenkins scans all branches and applies different rules:

- `main` → Full pipeline (test, build, push, version bump)
- `feature/*` → Build and test only (no push to Nexus)

**What clicked**: The pipeline should be branch-aware. Not all code deserves to be an artifact.

---

### Part 6.6: The Franchise Model

**Phase 6e | Concept: Shared Libraries**

I imagined having 10 microservices. Each needs a Jenkinsfile. Each Jenkinsfile has the same Docker build logic.

Copy-paste 10 times? That's a maintenance nightmare.

**The solution**: Jenkins Shared Library. I wrote the Docker build function *once* in a separate repo. Every Jenkinsfile just calls `buildDockerImage()`.

**What clicked**: DRY applies to CI/CD too. One bug fix in the library fixes all pipelines.

---

### Part 6.7: The Doorbell

**Phase 6f | Concept: Webhooks vs. Polling**

Jenkins was checking GitHub every minute: "Any new commits? Any new commits?"

Wasteful. And slow—up to 60 seconds delay.

**The fix**: Webhooks. GitHub *tells* Jenkins when something happens.

**The problem**: GitHub (public internet) can't reach my laptop (localhost).

**The solution**: Smee.io. It's a tunnel—GitHub sends to Smee, Smee forwards to my local Jenkins.

**What clicked**: Event-driven beats polling. But localhost needs a bridge to the public internet.

---

### Part 6.8: The Self-Aware Robot

**Phase 6g | Concept: Dynamic Versioning**

My images were tagged with build numbers (`enterprise-app:47`). My `pom.xml` said version `1.0.0`. They didn't match.

**The fix**: Jenkins reads the current version from `pom.xml`, calculates the next version, updates the file, and commits it back to Git.

Now the Docker tag matches the Git tag matches the `pom.xml`. Perfect traceability.

**What broke**: Infinite loops. Jenkins commits the version bump, which triggers the webhook, which triggers Jenkins, which commits...

**Fix**: Commit message includes `[skip ci]`. Jenkins recognizes this and doesn't trigger.

**What clicked**: Version should be automated, not manual. But CI systems need escape hatches.

---

## Part 7: The Captain

**Milestone 7 | Concept: Container Orchestration**

Docker Compose works for one server. What about 50 servers? What if a container crashes?

Enter Kubernetes.

I stopped giving direct orders (`docker run`). I started writing **declarations**:

```yaml
# "I want 2 copies of this app running at all times"
replicas: 2
```

Kubernetes figures out the rest—which server, what port, how to restart if it dies.

**The manifests I wrote**:

- `Deployment`: What container to run, how many replicas
- `Service`: A stable network endpoint (containers come and go, the Service IP doesn't)
- `ConfigMap`: Environment variables, injected at runtime

> **Analogy**: I stopped driving the delivery truck myself. I hired a **Captain** (Kubernetes) and gave him a shipping manifest. "Make sure this cargo reaches port. I don't care which truck you use."

**What broke**:

| Problem | Symptom | Root Cause | Fix |
|---------|---------|------------|-----|
| Image not found | `ImagePullBackOff` | Minikube can't reach localhost:8082 | `minikube image load` to manually import |
| ConfigMap not applied | Old welcome message | Forgot to apply configmap.yaml first | Apply order matters |
| Pod crashloop | `CrashLoopBackOff` | Typo in environment variable name | Check `kubectl logs` |

**What clicked**: Kubernetes is declarative. I describe the *desired state*, it figures out how to get there. But debugging requires understanding what it's actually doing (`kubectl describe`, `kubectl logs`).

---

## Part 8: The Full Picture

We now have a complete **Software Factory**.

```
Code → Git → Jenkins → Maven → Docker → Nexus → Kubernetes → Users
```

No manual steps. No SSH. No "I forgot to run tests."

### The Value Flow

1. I write code, push to GitHub
2. Webhook triggers Jenkins instantly
3. Jenkins:
   - Reads current version from `pom.xml`
   - Bumps to next version
   - Builds JAR, runs tests
   - Builds Docker image with version tag
   - Pushes to Nexus
   - Commits version back to Git (with `[skip ci]`)
4. Kubernetes pulls the image and deploys
5. Users hit the LoadBalancer, get served by healthy pods

### The Mental Model Shift

| Before | After |
|--------|-------|
| "DevOps is running scripts" | DevOps is designing reliable systems |
| "Docker is complicated" | Docker is just standardized packaging |
| "CI/CD is for big teams" | CI/CD saves time even for solo projects |
| "The cloud is magic" | The cloud is rented computers with APIs |
| "Kubernetes is overkill" | Kubernetes is a declarative control plane |

---

## What's Next

| Milestone | Concept | Why It Matters |
|-----------|---------|----------------|
| **M8: EKS** | Cloud Kubernetes | Minikube is a toy. EKS is production. |
| **M9: Terraform** | Infrastructure as Code | Stop clicking. Define the datacenter in Git. |
| **M11: Prometheus** | Observability | Stop hoping it works. Know it works. |
| **M12: Argo CD** | GitOps | Git becomes the source of truth for deployments, not just code. |
| **M13: Trivy** | Security Scanning | Find vulnerabilities before production. |

---

## The Takeaway

I didn't learn DevOps by watching tutorials. I learned it by breaking things:

- Security Groups taught me cloud networking
- 401 errors taught me credential management
- Infinite loops taught me CI escape hatches
- ImagePullBackOff taught me container registry architecture

The tools are just tools. The skill is understanding *why* they exist and *when* they break.

---

*This document will grow as the project continues.*
