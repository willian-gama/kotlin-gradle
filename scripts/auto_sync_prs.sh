#!/bin/bash

GIT_BRANCH=develop
# https://cli.github.com/manual/gh_pr_view
PR_BRANCHES=$(gh pr list --state open --base "$GIT_BRANCH" --json headRefName --jq '.[].headRefName')

if [ -z "$(git config --get user.name)" ]; then
  git config user.name "github-actions[bot]"
fi

if [ -z "$(git config --get user.email)" ]; then
  git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
fi

if [ ${#PR_BRANCHES[@]} -gt 0 ]; then
  for branch in $PR_BRANCHES; do
    echo -e "\nSyncing $branch\n"

    git fetch origin "$branch"
    git checkout "$branch"

    if ! git merge "origin/$GIT_BRANCH" --no-edit; then
      git merge --abort
    else
      git push origin "$branch"
    fi
  done
else
  echo "No PRs available for the target: $GIT_BRANCH"
fi