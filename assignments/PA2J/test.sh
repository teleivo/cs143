#!/bin/bash

set -e

TEST_DIR="${1:-./testdata}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        echo "Testing $file..."
        
	# ignore the first line which contains `#name <input file>`
        if ! diff <(../../bin/lexer "$file" | tail -n +2) <(./lexer "$file" | tail -n +2); then
            echo "Failed: Difference found in $file"
            exit 1
        fi
    fi
done

echo "All tests passed!"
