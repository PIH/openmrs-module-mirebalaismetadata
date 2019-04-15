#!/usr/bin/env bash

usage() {
    echo "Unzips all the MDS files in ../api/src/main/resources/ to here."
    echo
    echo "Usage: ./update.sh"
}

if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    usage
    exit 0
fi

rm *.xml*
unzip -B '../api/src/main/resources/*.zip'

