#!/bin/bash

GH_BRANCH=dev
# https://cli.github.com/manual/gh_pr_view
PR_BRANCHES=$(gh pr list --state open --base "$GH_BRANCH" --json headRefName --jq '.[].headRefName')

if [ -z "$(git config --get user.name)" ]; then
  git config user.name "github-actions[bot]"
fi

if [ -z "$(git config --get user.email)" ]; then
  git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
fi

echo -e "\nFetching target branch: $GH_BRANCH"
git fetch origin "$GH_BRANCH"

for branch in $PR_BRANCHES; do
  echo -e "\nSyncing $branch\n"

  git fetch origin "$branch"
  git checkout "$branch"

  if ! git merge "origin/$GH_BRANCH"; then
    echo -e "\nMerge conflict detected on branch $branch.\n"
    git merge --abort
    continue
  fi

  git push origin "$branch"
done