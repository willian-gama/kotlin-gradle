#!/bin/sh

LOCAL_PROPERTIES="local.properties"
touch $LOCAL_PROPERTIES

{
  echo "gpr_username=$GPR_USERNAME"
  echo "gpr_key=$GPR_KEY"
}  >> $LOCAL_PROPERTIES

cat $LOCAL_PROPERTIES