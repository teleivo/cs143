#!/bin/bash

set -e

TEST_DIR="${1:-./testdata-ok}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        # TODO I would like to diff myparser against the reference parser but the reference parser hangs forever
        # # ignore line numbers: lines that contain only whitespace followed by # and a number
        # if ! diff <(../../bin/parser "$file" | grep -v '^\s*#[0-9]\+$') <(./myparser "$file" | grep -v '^\s*#[0-9]\+$'); then
        #     echo "Testing $file: failed"
        #     failed=1
        # else
        #     echo "Testing $file: ok"
        # fi
        if ! ./myparser "$file" > /dev/null; then
            echo "Testing $file: failed"
            failed=1
        else
            echo "Testing $file: ok"
        fi
    fi
done

if [ -n "$failed" ]; then
    exit 1
fi

echo "All tests passed!"
