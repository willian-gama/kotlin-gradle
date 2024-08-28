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
  local version=$1

  echo "Create git tag with version $version"

  if ! git tag "$version"; then
    echo "Error when creating git tag $version"
    exit 1
  fi

  git push origin "$version"
}


get_version_to_create_tag() {
  file_content=$(cat "$FILE")
  version=$(get_current_version_number "$file_content")
  create_new_git_tag "$version"
}

get_version_to_create_tag