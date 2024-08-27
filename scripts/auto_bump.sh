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
#  if [ -z "$(git config --get user.name)" ]; then
#    git config user.name "renovate[bot]"
#  fi
#
#  if [ -z "$(git config --get user.email)" ]; then
#    git config user.email "29139614+renovate[bot]@users.noreply.github.com"
#  fi

  git config --add --bool push.autoSetupRemote true # create a new branch automatically


#  git add "$FILE"
#  git commit -m "$commit_message"

  if ! git add "$FILE"; then
    echo "Error: Failed to add $FILE to the staging area."
    return 1
  fi

  # Commit the changes
  if ! git commit -m "$commit_message"; then
    echo "Error: Git commit failed."
    return 1
  fi

  if ! git push; then
    echo "Error when pushing new version"
    exit 1
  fi
}

bump_version_if_needed() {
  if ! local_file_content=$(cat "$FILE" 2>/dev/null); then
    echo "Local file $FILE could not be found or is empty"
    return 1
  fi

  if ! remote_file_content=$(git show origin/develop:"$FILE" 2>/dev/null); then
    echo "Remote file $FILE could not be found or is empty in the develop branch"
    return 1
  fi

  local_version=$(get_version_number "$local_file_content") || {
    echo "Local version could not be found in the $FILE file"
    return 1
  }

  remote_version=$(get_version_number "$remote_file_content") || {
    echo "Remote version could not be found in the $FILE file"
    return 1
  }

  if compare_versions "$local_version" "$remote_version" -eq 0; then
    bump_and_push_new_version_to_git "$local_version" "$remote_version"
  else
    echo "local version: $local_version is already greater than remote version: $remote_version"
  fi
}

bump_version_if_needed