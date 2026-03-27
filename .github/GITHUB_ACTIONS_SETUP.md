# GitHub Actions Setup Checklist

## ✅ Pre-Deployment Checklist

### 1. Repository Prerequisites
- [ ] Repository is on GitHub
- [ ] `.git` folder exists locally
- [ ] Git remote is configured correctly
  ```bash
  git remote -v
  ```

### 2. Branch Setup
- [ ] Main branch exists (`main` or `master`)
- [ ] Default branch is set to `main`
  - Go to: Settings → Branches → Default branch

### 3. Files in Place
- [ ] `.github/workflows/main-test-pipeline.yml` ✓
- [ ] `.github/workflows/parallel-test-execution.yml` ✓
- [ ] `.github/workflows/code-quality.yml` ✓
- [ ] `.github/workflows/build-release.yml` ✓
- [ ] `pom.xml` exists ✓
- [ ] `testng.xml` exists ✓

### 4. GitHub Settings
- [ ] Actions enabled: Settings → Actions → General
- [ ] Workflow permissions set: "Read and write permissions"
- [ ] Settings → Actions → General → Allow all actions and reusable workflows

---

## 🚀 Deployment Steps

### Step 1: Commit All Changes
```bash
cd D:\DTAutomationframework
git add .
git commit -m "Add CI/CD pipeline configuration"
```

### Step 2: Verify Git Status
```bash
git status
git log --oneline -n 5
```

Expected output: All files staged and committed

### Step 3: Push to GitHub
```bash
git push origin main
# or if using master:
# git push origin master
```

### Step 4: Monitor First Workflow
1. Go to: https://github.com/YOUR_USERNAME/DTAutomationframework/actions
2. Wait for workflows to appear (may take 1-2 minutes)
3. Click on the first workflow run
4. Watch job execution in real-time

---

## 🔐 Optional: Configure Slack Integration

### Get Slack Webhook URL

1. Go to: https://api.slack.com/apps
2. Click "Create New App" → "From scratch"
3. Name: "Banking Automation Alerts"
4. Select your workspace
5. Go to: "Incoming Webhooks"
6. Click "Add New Webhook to Workspace"
7. Select channel: `#test-automation` (or create one)
8. Copy webhook URL

### Add to GitHub Secrets

1. Repository → Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Name: `SLACK_WEBHOOK`
4. Value: Paste webhook URL
5. Click "Add secret"

### Enable Slack Notifications

Edit `.github/workflows/main-test-pipeline.yml`:
- Lines with `secrets.SLACK_WEBHOOK` will now work
- Uncomment if commented out

---

## 📊 First Workflow Run

### What to Expect

1. **Checkout** (30 seconds)
   - Repository cloned

2. **Setup JDK** (1-2 minutes)
   - Java 11 installed
   - Maven cache restored

3. **Install Dependencies** (2-3 minutes)
   - Maven dependencies downloaded
   - Plugins configured

4. **Run Tests** (3-5 minutes)
   - Test execution
   - Allure report generation

5. **Upload Artifacts** (30 seconds)
   - Test results uploaded
   - Allure report stored

6. **Notifications** (Instant)
   - Slack message sent (if configured)
   - GitHub status updated

**Total Time: ~8-12 minutes**

---

## 🎯 Verify Workflow Success

### Check Test Results
1. Go to Actions tab
2. Click on workflow run
3. See "Published Test Results" section
4. View test summary

### Download Allure Report
1. Actions → Workflow run → Artifacts section
2. Download `allure-report.zip`
3. Extract and open `index.html`

### Check Artifacts
1. Actions → Workflow run → Artifacts
2. View:
   - `allure-report` (test metrics & trends)
   - `test-results` (XML files)
   - `maven-site-report` (optional)

---

## 🔄 Trigger Workflows Manually

### Main Test Pipeline
- Automatically triggers on:
  - Push to `main`, `master`, `develop`
  - Pull requests to these branches
  - Daily at 2 AM UTC

### Parallel Execution
1. Go to: Actions → Parallel Test Execution
2. Click "Run workflow"
3. Select test suite (optional)
4. Click "Run workflow"

### Code Quality
- Automatically triggers on:
  - Push to main branches
  - Pull requests

### Build & Release
1. Create git tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. Workflow automatically triggers
3. GitHub release created

---

## 🐛 Troubleshooting

### Workflows Not Showing
**Problem:** Actions tab is empty

**Solution:**
1. Verify `.github/workflows/` files exist
2. Push again:
   ```bash
   git add .github/
   git commit -m "Fix workflows"
   git push origin main
   ```
3. Refresh Actions page
4. May take 1-2 minutes to appear

### Tests Failing
**Problem:** Tests showing failures

**Solutions:**
1. Check test output in workflow
2. Verify `testng.xml` is correct
3. Check for missing dependencies
4. Run locally:
   ```bash
   mvn clean test
   ```

### Allure Report Not Generated
**Problem:** Report artifact is missing

**Solutions:**
1. Check "Run all tests" step output
2. Verify `allure-results/` has files
3. Check Maven surefire plugin config
4. Manually generate:
   ```bash
   mvn allure:report
   ```

### Slack Not Receiving Messages
**Problem:** No Slack notifications

**Solutions:**
1. Verify webhook URL is correct
2. Check GitHub secret:
   ```bash
   gh secret list
   ```
3. Ensure channel exists and bot has access
4. Check workflow logs for errors

---

## 📈 Next Advanced Steps

### 1. Enable Branch Protection
Settings → Branches → Add rule:
- Branch name: `main`
- Require status checks to pass
- Select workflow checks to require

### 2. Add Code Coverage
In `pom.xml`, add JaCoCo plugin:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
</plugin>
```

### 3. Setup SonarQube
1. Install SonarQube (Docker or local)
2. Create project token
3. Add GitHub secret: `SONAR_TOKEN`
4. Uncomment in `.github/workflows/code-quality.yml`

### 4. Configure Email Notifications
1. GitHub → Settings → Notifications
2. Set up email or third-party integration
3. Configure rule for failed workflows

---

## 💻 Local Testing Before Push

### Test Workflow Syntax
```bash
# Install act (GitHub Actions runner)
brew install act  # Mac
choco install act # Windows
# Or download from: https://github.com/nektos/act

# Test workflows locally
act -j test  # Run specific job
act -l       # List all jobs
```

### Manual Build & Test
```bash
# Build
mvn clean compile

# Run tests
mvn test

# Generate report
mvn allure:report
```

---

## 📚 Useful Commands

```bash
# Check latest workflow runs
gh run list

# View workflow run details
gh run view <RUN_ID>

# Cancel running workflow
gh run cancel <RUN_ID>

# View logs
gh run view <RUN_ID> --log

# View secrets
gh secret list

# Create secret
gh secret set SECRET_NAME -b "secret_value"
```

---

## 🔍 Monitoring & Maintenance

### Daily Tasks
- [ ] Monitor Actions tab for failures
- [ ] Review test results

### Weekly Tasks
- [ ] Check artifact storage usage
- [ ] Review workflow durations
- [ ] Check for flaky tests

### Monthly Tasks
- [ ] Archive important reports
- [ ] Update dependencies
- [ ] Review security scan results
- [ ] Optimize workflow performance

---

## 📞 Support Resources

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)
- [Runners](https://docs.github.com/en/actions/using-runners)
- [Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)

---

## ✨ Quick Wins

Once workflow is running:

1. **Add Status Badge**
   ```markdown
   ![Tests](https://github.com/YOUR_USER/DTAutomationframework/actions/workflows/main-test-pipeline.yml/badge.svg)
   ```

2. **Link Allure Reports**
   - Save all Allure reports
   - Link from GitHub Pages

3. **Configure Notifications**
   - Email on failures
   - Slack on success/failure

4. **Set Up Scheduled Runs**
   - Daily nightly tests
   - Weekly regression suite

---

**Ready? Push your code!** 🚀
