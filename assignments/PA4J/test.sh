#!/bin/bash

set -e

TEST_DIR="${1:-./testdata}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        if ! diff <(../../bin/lexer "$file" | ../../bin/parser | ../../bin/semant) <(../../bin/lexer "$file" | ../../bin/parser | ../../bin/semant); then
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
