# Git-Swft( Smart Workflow For Teams ) CLI Tool🚀

Git-Swft is a powerful command-line tool that simplifies GitHub repository management. With just a few commands, you can authenticate, create repositories, push changes, clone repositories, delete repositories, and more — all directly from your terminal.

## Features ✨

🔑 Authenticate with GitHub using a Personal Access Token

📦 Create new repositories on GitHub instantly

🚀 Push existing local folders to GitHub easily

⚡ Quick add → commit → push

📥 Clone repositories by name or URL

🗑️ Delete repositories safely

🔒 Supports private/public repos

🔄 Automatically handles branches (main / master)

---

## How to Use

### Install the Tool

To install the package globally, run the following command:

```bash
npm install -g swft
```

Verify installation:

```bash
swft --version
```

### Commands & Usage 🛠️

### 1️⃣ Authenticate with GitHub

```bash
swft auth
```

Follow the instructions to generate and save your Personal Access Token.
🔑 GitHub Authentication Guide

1. Open: https://github.com/settings/tokens
2. Click on 'Generate new token (classic)'
3. Give it a name (e.g. 'git-create-cli').
4. Select these required scopes:

   - repo (Full control of private and public repositories)
   - delete_repo (To allow deleting repositories)
   - read:user (To read your GitHub profile)

⚠️ Copy the token and keep it safe, you won’t see it again!

### 2️⃣ Create a New Repository

Create a New Repository this is to create a new repo remotely this doesn’t create repo locally

```bash
swft create <repo-name> --private --description "My awesome repo"
```

Options:

--private → make repository private

--description <text> → add repository description

### 2️⃣ Create and initial new Repository

Create and initial new Repository create a new repo locally and push it to remote

```bash
swft create-push <repo-name> --private --description "My awesome repo"
```

Options:

--private → make repository private

--description <text> → add repository description

### 3️⃣ Push Existing Local Folder

Push Existing Local Folder pushes the local folder to remote github url

```bash
swft init-push <repo-url> -b main
```

Automatically performs:

Initializes Git if not done already

Adds all files

Commits with "Initial commit"

Creates/switches to branch (main by default)

Sets remote origin

Pushes to GitHub

### 4️⃣ Quick Add → Commit → Push

```bash
swft push "my commit message"
```

Adds all changes

Commits with message (default: "add")

Pushes to current branch

### 5️⃣ Clone a Repository

By GitHub username:

```bash
swft clone <repo-name>
```

By direct URL:

```bash
swft clone-url <repo-url>
```

### 6️⃣ Delete a Repository

```bash
swft delete <repo-name>
```

⚠️ Warning: This is irreversible!

### ️7️⃣ Check Login Status

```bash
swft status
```

Shows the currently logged-in GitHub username.

### 8️⃣ Logout

```bash
swft logout
```

Removes saved token and logs you out.

### Why Git-Swft CLI? ⚡

- Saves time on repetitive Git tasks

- Handles GitHub authentication seamlessly

- Automates branch creation and conflict handling

- Perfect for developers who want fast GitHub workflow
