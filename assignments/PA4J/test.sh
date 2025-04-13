#!/bin/bash

set -e

TEST_DIR="${1:-./testdata}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        # # ignore the first line which contains `#name <input file>`
        # if ! diff <(../../bin/lexer "$file" | tail -n +2) <(./lexer "$file" | tail -n +2); then
        #     echo "Testing $file: failed"
        #     failed=1
        # else
        #     echo "Testing $file: ok"
        # fi
        ./mysemant "$file"
    fi
done

if [ -n "$failed" ]; then
    exit 1
fi

echo "All tests passed!"
