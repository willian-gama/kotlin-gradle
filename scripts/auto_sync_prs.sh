#!/bin/bash

TARGET_BRANCH=develop

if [ -z "$(git config --get user.name)" ]; then
  git config user.name "renovate[bot]"
fi

if [ -z "$(git config --get user.email)" ]; then
  git config user.email "29139614+renovate[bot]@users.noreply.github.com"
fi

git fetch --all

PR_BRANCHES=$(gh pr list --state open --repo willian-gama/kgp)

echo "$PR_BRANCHES"

for branch in $PR_BRANCHES; do
  echo "$branch"
#  git checkout "$branch"
#  git merge origin/"$TARGET_BRANCH" --no-edit
#  git push origin "$branch"
done