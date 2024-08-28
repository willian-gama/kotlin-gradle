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

push_new_tag_to_git() {
  local version=$1

  if ! git tag "$version"; then
    echo "Error when creating git tag: $version"
    exit 1
  else
    echo "Create git tag: $version"
    git push origin "$version"
  fi
}


create_new_git_tag() {
  file_content=$(cat "$FILE")
  version=$(get_current_version_number "$file_content")
  push_new_tag_to_git "$version"
}

create_new_git_tag