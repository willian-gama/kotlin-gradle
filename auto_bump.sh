#!/bin/bash

FILE="build.gradle.kts"

get_version_number() {
  local content="$1"
  if [[ "$content" =~ version\ *=\ *\"([0-9]+\.[0-9]+\.[0-9]+)\" ]]; then
    echo "${BASH_REMATCH[1]}"
  else
    echo "failed to find the line with the version number"
  fi
}

compare_versions() {
  local local_version=$1
  local remote_version=$2

  IFS="." read -r local_major local_minor local_patch <<<"$local_version"
  IFS="." read -r remote_major remote_minor remote_patch <<<"$remote_version"

  # compare major versions
  if [ "$remote_major" -gt "$local_major" ]; then
    echo "local major version is lower than remote version"
    return 0
  elif [ "$remote_major" -lt "$local_major" ]; then
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi

  # compare minor versions
  if [ "$remote_minor" -gt "$local_minor" ]; then
    echo "local minor version is lower than remote version"
    return 0
  elif [ "$remote_minor" -lt "$local_minor" ]; then
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi

  # compare patch versions
  if [ "$remote_patch" -ge "$local_patch" ]; then
    echo "remote patch version is greater than or equal to local version"
    return 0
  else
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi
}

bump_and_push_new_version_to_git() {
  local_version=$1
  remote_version=$2

  IFS='.' read -r major minor patch <<< "$remote_version"
  new_local_version="$major.$minor.$((patch + 1))"
  perl -i -pe "s/$local_version/$new_local_version/" "$FILE"

  commit_message="auto bump version from $local_version to $new_local_version"
  echo "$commit_message"

  # https://github.com/actions/checkout/blob/main/README.md#push-a-commit-using-the-built-in-token
  git config user.name "renovate[bot]"
  git config user.email "29139614+renovate[bot]@users.noreply.github.com"
  git config --add --bool push.autoSetupRemote true # create a new branch automatically

  git add "$FILE"
  git commit -m "$commit_message" --allow-empty
  git push

#  WORKFLOW_FILE="pull_request_ci.yml"
#  GIT_BRANCH="refs/heads/main"  # Branch or ref to trigger the workflow on
  # Trigger the workflow
#  REPO="$GIT_OWNER/$GIT_REPO"
#  gh run list --repo "$REPO"
#
##  WORKFLOW_RUN_ID=$(gh run list --repo "$REPO" --branch "$BRANCH" --limit 1 --json databaseId --jq '.[0].databaseId')
#  if [ -z "$WORKFLOW_RUN_ID" ]; then
#   echo "No workflow runs found for the branch $BRANCH."
#   exit 1
#  fi

  # Re-run all jobs in the workflow

#  RESPONSE=$(curl -X POST \
#    -H "Authorization: token $GITHUB_TOKEN" \
#    -H "Accept: application/vnd.github+json" \
#    "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID/cancel")
#
#  if [ $? -eq 0 ]; then
#    echo "Workflow run $RUN_ID canceled successfully."
#
#    gh run rerun "$WORKFLOW_RUN_ID" --debug
#    echo "Executing callback actions..."
#  fi

  if gh run cancel "$WORKFLOW_RUN_ID" ; then
     sleep 10
     gh run rerun "$WORKFLOW_RUN_ID" --debug
  else
      echo "Failed to cancel workflow run $RUN_ID."
      exit 1
  fi


#  gh run cancel "$WORKFLOW_RUN_ID"
#  gh run rerun "$WORKFLOW_RUN_ID" --debug
  echo "Re-ran workflow with run ID: $WORKFLOW_RUN_ID"

#
#  RUN_ID=$(gh run list --repo "$REPO" --pr "$PR_NUMBER" --json databaseId --jq '.[0].databaseId')
#  echo "run_id=$RUN_ID"
#  gh run rerun "$RUN_ID" --debug
}

bump_version_if_needed() {
  git pull
  local_version=$(get_version_number "$(cat "$FILE")")
#  echo "local version: $local_version"

#  git fetch origin
#  remote_version=$(get_version_number "$(git show origin/develop:"$FILE")")
#  echo "remote version: $remote_version"

#  if compare_versions "$local_version" "$remote_version" == 0; then
#    bump_and_push_new_version_to_git "$local_version" "$remote_version"
#  fi
  bump_and_push_new_version_to_git "$local_version" "$local_version"
}

bump_version_if_needed