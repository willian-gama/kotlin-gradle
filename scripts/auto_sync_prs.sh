#!/bin/bash

echo "$GH_BRANCH"
GH_BRANCH=$1
echo "$GH_BRANCH"

# https://cli.github.com/manual/gh_pr_view
PR_BRANCHES=$(gh pr list --state open --base "$GH_BRANCH" --json headRefName --jq '.[].headRefName')

echo "Fetching target branch: $GH_BRANCH"
git fetch origin "$GH_BRANCH"

for branch in $PR_BRANCHES; do
  echo "Syncing $branch"
  git fetch origin "$branch"
  git checkout "$branch"
  git merge "origin/$GH_BRANCH" --no-edit
  git push origin "$branch"
done