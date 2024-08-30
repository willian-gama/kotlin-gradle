#!/bin/bash

GIT_BRANCH=$CIRCLE_BRANCH

# https://cli.github.com/manual/gh_pr_view
PR_BRANCHES=$(gh pr list --state open --base "$GIT_BRANCH" --json headRefName --jq '.[].headRefName')

if [ -z "$(git config --get user.name)" ]; then
  git config user.name "github-actions[bot]"
fi

if [ -z "$(git config --get user.email)" ]; then
  git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
fi

for branch in $PR_BRANCHES; do
  echo -e "\nSyncing $branch\n"

  git fetch origin "$branch"
  git checkout "$branch"

  if ! git merge "origin/$GIT_BRANCH" --no-edit; then
    git merge --abort
    continue
  fi

  git push origin "$branch"
done