#!/bin/bash

#KOTLIN_FILE=$(find . -type f -name "*.kt" | grep "KtLintConstants")
KOTLIN_FILE=src/main/kotlin/com/kitmanlabs/code/analysis/constants/KtLintConstants.kt
EDITORCONFIG_FILE=".editorconfig"

if [ ! -f "$KOTLIN_FILE" ]; then
  echo "File not found $KOTLIN_FILE"
  exit 1
fi

rules=$(sed -n '/KTLINT_RULES *=/,/^)/p' "$KOTLIN_FILE" | grep -E 'to' | tr -d '"' | tr -d ',' | sed 's/)$//; s/to//g;')

if [ -z "$rules" ]; then
  echo "No Ktlint rules found in $KOTLIN_FILE"
  exit 1
fi

{
  echo "[*.{kt,kts}]"
  echo "$rules" | while read -r rule status; do
    echo "$rule = $status"
  done
} > "$EDITORCONFIG_FILE"

echo "$EDITORCONFIG_FILE created from $KOTLIN_FILE"
cat "$EDITORCONFIG_FILE"