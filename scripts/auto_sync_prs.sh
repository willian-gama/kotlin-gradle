#!/bin/bash

TARGET_BRANCH=$1
PR_BRANCHES=$(gh pr list --state open --base "$TARGET_BRANCH" --json headRefName --jq '.[].headRefName')

echo "Target branch: $TARGET_BRANCH"

for branch in $PR_BRANCHES; do
  echo "Syncing $branch"
  git fetch origin "$branch"
  git checkout "$branch"
  git merge "$TARGET_BRANCH" --no-edit
  git push origin "$branch"
done