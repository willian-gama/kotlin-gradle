#!/bin/bash

#KOTLIN_FILE=$(find . -type f -name "*.kt" | grep "KtLintConstants")
KOTLIN_FILE=src/main/kotlin/com/kitmanlabs/code/analysis/constants/KtLintConstants.kt
EDITORCONFIG_FILE=".editorconfig"

echo "[*.{kt,kts}]" > "$EDITORCONFIG_FILE"

sed -n '/KTLINT_RULES *=/,/^)/p' "$KOTLIN_FILE" | grep -E 'to' | while IFS=' ' read -r rule _ status; do
  rule=$(echo "$rule" | sed 's/"//g; s/,//')
  status=$(echo "$status" | sed 's/"//g; s/,//; s/)$//')
  echo "$rule = $status" >> "$EDITORCONFIG_FILE"
done

echo "Created .editorconfig from $KOTLIN_FILE"