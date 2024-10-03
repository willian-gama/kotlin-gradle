#!/bin/bash

#KOTLIN_FILE=$(find . -type f -name "*.kt" | grep "KtLintConstants")
KOTLIN_FILE=src/main/kotlin/com/kitmanlabs/code/analysis/constants/KtLintConstants.kt
EDITORCONFIG_FILE=".editorconfig"

if [ ! -f "$KOTLIN_FILE" ]; then
  echo "File not found $KOTLIN_FILE"
  exit 1
fi

rules=$(sed -n '/KTLINT_RULES *=/,/^)/p' "$KOTLIN_FILE" | grep -E 'to' | sed 's/"//g; s/,//; s/)$//')

if [[ -z "$rules" ]]; then
  echo "Error: No KTLINT rules found in '$KOTLIN_FILE'!"
  exit 1
fi

echo "[*.{kt,kts}]" > "$EDITORCONFIG_FILE"

echo "$rules" | while IFS=' ' read -r rule _ status; do
  rule=$(echo "$rule" | sed 's/"//g; s/,//')
  status=$(echo "$status" | sed 's/"//g; s/,//; s/)$//')
  echo "$rule = $status" >> "$EDITORCONFIG_FILE"
done

echo "$EDITORCONFIG_FILE created from $KOTLIN_FILE"
cat "$EDITORCONFIG_FILE"