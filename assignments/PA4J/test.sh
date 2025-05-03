#!/bin/bash

set -e

TEST_DIR="${1:-./testdata-ok}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        echo -n "Testing $file: "

        # Run reference implementation with error handling
        if ! reference=$(../../bin/lexer "$file" | ../../bin/parser | ../../bin/semant 2>&1); then
            echo "FAILED (reference implementation)"
            failed=1
            continue
        fi

        # Run my implementation with error handling
        if ! my=$(../../bin/lexer "$file" | ../../bin/parser | ./semant 2>&1); then
            echo "FAILED (my implementation)"
            failed=1
            continue
        fi

        if ! diff <(echo "$reference") <(echo "$my"); then
            echo "FAILED (Outputs differ between implementations)"
            failed=1
        else
            echo "OK"
        fi
    fi
done

if [ -n "$failed" ]; then
    exit 1
fi

echo "All tests passed!"
