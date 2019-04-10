#!/usr/bin/env bash

usage() {
    echo "Unzips all the MDS files in ../api/src/main/resources/ to here."
    echo "For some reason half of the resultant XML files are redudant."
    echo "Therefore this deletes the latter half of them."
    echo
    echo "Usage: ./update.sh"
}

if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    usage
    exit 0
fi

NUM_PACKAGES=$(ls ../api/src/main/resources/*.zip -1 | wc -l)

unzip -B '../api/src/main/resources/*.zip'
for i in $(seq $NUM_PACKAGES $(($NUM_PACKAGES * 2))); do rm header.xml~$i metadata.xml~$i ; done

