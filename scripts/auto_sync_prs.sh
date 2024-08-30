#!/bin/bash

TARGET_BRANCH=$1

#if [ -z "$(git config --get user.name)" ]; then
#  git config user.name "renovate[bot]"
#fi
#
#if [ -z "$(git config --get user.email)" ]; then
#  git config user.email "29139614+renovate[bot]@users.noreply.github.com"
#fi

PR_BRANCHES=$(gh pr list --state open --base "$TARGET_BRANCH" --json headRefName --jq '.[].headRefName')

echo "Target branch: $TARGET_BRANCH"

for branch in $PR_BRANCHES; do
  echo "Syncing $branch"
  git fetch origin "$branch"
  git checkout "$branch"
  git merge origin/"$TARGET_BRANCH" --no-edit
  git push origin "$branch"
done