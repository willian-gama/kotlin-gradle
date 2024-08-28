#!/bin/bash

# Run locally in the Android Studio terminal for testing purposes: ./scripts/auto_bump.sh
FILE="build.gradle.kts"

get_current_version_number() {
  local content="$1"
  if [[ "$content" =~ version\ *=\ *\"([0-9]+\.[0-9]+\.[0-9]+)\" ]]; then
    echo "${BASH_REMATCH[1]}"
    return 0
  else
    return 1
  fi
}

create_new_git_tag() {
  local_file_content=$(cat "$FILE")
  local_version=$(get_current_version_number "$local_file_content")

  git tag "$local_version"
}

create_new_git_tag