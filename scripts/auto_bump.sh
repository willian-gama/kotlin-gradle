#!/bin/bash

# Run locally in the Android Studio terminal for testing purposes: ./scripts/auto_bump.sh
FILE="build.gradle.kts"

get_version_number() {
  local content="$1"
  if [[ "$content" =~ version\ *=\ *\"([0-9]+\.[0-9]+\.[0-9]+)\" ]]; then
    echo "${BASH_REMATCH[1]}"
    return 0
  else
    return 1
  fi
}

compare_versions() {
  local local_version=$1
  local remote_version=$2

  IFS="." read -r local_major local_minor local_patch <<< "$local_version"
  IFS="." read -r remote_major remote_minor remote_patch <<< "$remote_version"

  if [ "$local_major" -gt "$remote_major" ] || [ "$local_minor" -gt "$remote_minor" ] || [ "$local_patch" -gt "$remote_patch" ]; then
    return 1
  else
    return 0
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
}

bump_version_if_needed() {
  local_file_content=$(cat "$FILE") || {
    echo "Local $FILE could not be found or is empty";
    return 1
  }

  remote_file_content=$(git show origin/develop:"$FILE") || {
    echo "Remote $FILE could not be found or is empty"
    return 1
  }

  local_version=$(get_version_number "$local_file_content") || {
    echo "Error when getting local version"
    return 1
  }

  remote_version=$(get_version_number "$remote_file_content") || {
    echo "Error when getting remote version"
    return 1
  }

  if compare_versions "$local_version" "$remote_version" -eq 0; then
    bump_and_push_new_version_to_git "$local_version" "$remote_version"
  else
    echo "local version: $local_version is already greater than remote version: $remote_version"
  fi
}

bump_version_if_needed