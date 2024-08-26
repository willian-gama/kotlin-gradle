#!/bin/bash

## file path to the build.gradle
FILE="build.gradle.kts"

get_version_number() {
  local content="$1"
  if [[ "$content" =~ version\ *=\ *\"([0-9]+\.[0-9]+\.[0-9]+)\" ]]; then
    echo "${BASH_REMATCH[1]}"
    return 0
  else
    echo "failed to find the line with the version number"
    return 1
  fi
}

compare_versions() {
  local local_version=$1
  local remote_version=$2
  echo "comparing local version: $local_version and local version: $remote_version"

  IFS="." read -r local_major local_minor local_patch <<<"$local_version"
  IFS="." read -r remote_major remote_minor remote_patch <<<"$remote_version"

  # compare major versions
  if [ "$remote_major" -gt "$local_major" ]; then
    echo "local major version is lower than remote version"
    bump_version "$local_version" "$remote_version"
    return 0
  elif [ "$remote_major" -lt "$local_major" ]; then
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi

  # compare minor versions
  if [ "$remote_minor" -gt "$local_minor" ]; then
    echo "local minor version is lower than remote version"
    bump_version "$local_version" "$remote_version"
    return 0
  elif [ "$remote_minor" -lt "$local_minor" ]; then
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi

  # compare patch versions
  if [ "$remote_patch" -ge "$local_patch" ]; then
    echo "remote patch version is greater than or equal to local version"
    bump_version "$local_version" "$remote_version"
    return 0
  else
    echo "local version: $local_version is greater than remote version: $remote_version"
    return 1
  fi
}

increment_version() {
  local version=$1
  IFS='.' read -r major minor patch <<< "$version"
  echo "$major.$minor.$((patch + 1))"
}

# Function to actually bump the version
bump_version() {
  local_version=$1
  remote_version=$2
  new_version=$(increment_version "$remote_version")

  perl -i -pe "s/$local_version/$new_version/" "$FILE"
  echo "version updated from $local_version to $new_version"
}

commit_and_push_new_version() {
  # https://github.com/actions/checkout/blob/main/README.md#push-a-commit-using-the-built-in-token
  git config user.name "github-actions[bot]"
  git config user.email "41898282+github-actions[bot]@users.noreply.github.com"

  git checkout "$GITHUB_HEAD_REF"
  git config --add --bool push.autoSetupRemote true
  git add "$FILE"
  git commit -m "auto bump version"
  git push
}

bump_version_if_needed() {
#  git fetch origin "$GITHUB_HEAD_REF"
  git checkout "$GITHUB_HEAD_REF"
  local_version=$(get_version_number "$(cat "$FILE")")
  echo "local version: $local_version"

#  git fetch origin develop
  remote_version=$(get_version_number "$(git show origin/develop:"$FILE")")
#  echo "remote version: $remote_version"

#  if compare_versions "$local_version" "$remote_version" ; then
#    commit_and_push_new_version
#  fi
}

bump_version_if_needed