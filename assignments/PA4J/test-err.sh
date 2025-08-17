#!/bin/bash

set -e

TEST_DIR="${1:-./testdata-err}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

failed=0

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        # Run the command and capture only stderr, remove line numbers, then sort
        output1=$(../../bin/lexer "$file" | ../../bin/parser "$file" | ../../bin/semant "$file" 2>&1 1>/dev/null | sed 's/:[0-9]*:/:/' | sort)
        output2=$(../PA2J/lexer "$file" | ../PA3J/parser "$file" | ./semant "$file" 2>&1 1>/dev/null | sed 's/:[0-9]*:/:/' | sort)

        if ! diff <(echo "$output1") <(echo "$output2"); then
            echo "Testing $file: failed"
            failed=1
        else
            echo "Testing $file: ok"
        fi
    fi
done

if [ "$failed" -ne 0 ]; then
    exit 1
fi

echo "All tests passed!"
