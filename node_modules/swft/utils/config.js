const axios = require("axios");
const fs = require("fs");
const os = require("os");
const path = require("path");
const chalk = require("chalk");

async function saveToken(token) {
  try {
    // Test request to GitHub API
    const res = await axios.get("https://api.github.com/user", {
      headers: {
        Authorization: `token ${token}`,
        "User-Agent": "git-create-cli",
      },
    });

    // Config dir create if not exist
    const configDir = path.join(os.homedir(), ".ghx");
    if (!fs.existsSync(configDir)) {
      fs.mkdirSync(configDir);
    }

    // Save token to config.json
    fs.writeFileSync(
      path.join(configDir, "config.json"),
      JSON.stringify({ githubToken: token }, null, 2)
    );

    console.log(chalk.green(`✅ Authenticated as ${res.data.login}`));
  } catch (err) {
    console.log(chalk.red("❌ Failed to authenticate with GitHub"));
  }
}

function getToken() {
  const file = path.join(os.homedir(), ".ghx", "config.json");
  if (!fs.existsSync(file)) return null;

  const data = JSON.parse(fs.readFileSync(file));
  return data.githubToken || null;
}

function Logout() {
  const file = path.join(os.homedir(), ".ghx", "config.json");

  if (fs.existsSync(file)) {
    fs.unlinkSync(file);
    console.log(chalk.green("✅ Logged out successfully. Token removed."));
  } else {
    console.log(chalk.yellow("⚠️ You are not logged in."));
  }
}

module.exports = { saveToken, getToken, Logout };
