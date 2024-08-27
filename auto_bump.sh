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
  git commit -m "$commit_message"
  git push

  WORKFLOW_RUN_ID=$(gh run list --repo "$REPO" --branch "$BRANCH" --json databaseId,headBranch,status --jq '.[] | select(.headBranch == "'"$BRANCH"'") | .databaseId' | head -n 1)
  gh run rerun "$WORKFLOW_RUN_ID" --repo willian-gama/kgp
  echo "Workflow re-run triggered for run ID: $WORKFLOW_RUN_ID"
}

bump_version_if_needed() {
  local_version=$(get_version_number "$(cat "$FILE")")
  echo "local version: $local_version"

  git fetch origin
  remote_version=$(get_version_number "$(git show origin/develop:"$FILE")")
  echo "remote version: $remote_version"

#  if compare_versions "$local_version" "$remote_version" == 0; then
#    bump_and_push_new_version_to_git "$local_version" "$remote_version"
#  fi

  bump_and_push_new_version_to_git "$local_version" "$local_version"
}

bump_version_if_needed