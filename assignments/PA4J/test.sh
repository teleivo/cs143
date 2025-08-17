#!/bin/bash

set -e

TEST_DIR="${1:-./testdata-ok}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

failed=0

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        echo -n "Testing $file: "

        # Run reference implementation with error handling
        if ! reference=$(../../bin/lexer "$file" | ../../bin/parser "$file" | ../../bin/semant "$file" 2>/tmp/ref_error.txt); then
            echo "FAILED (reference implementation)"
            echo "Reference errors:"
            cat /tmp/ref_error.txt
            failed=1
            continue
        fi

        # Run my implementation with error handling
        if ! my=$(../PA2J/lexer "$file" | ../PA3J/parser "$file" | ./semant "$file" 2>/tmp/my_error.txt); then
            echo "FAILED (my implementation)"
            echo "My implementation errors:"
            cat /tmp/my_error.txt
            failed=1
            continue
        fi

        # Filter out lines with #<number> patterns and compare
        if ! diff_output=$(diff -u <(echo "$reference" | grep -v '^\s*#[0-9]\+$') <(echo "$my" | grep -v '^\s*#[0-9]\+$')); then
            echo "FAILED (Outputs differ between implementations)"
            echo "Diff (-reference +my):"
            echo "$diff_output"
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
