#!/bin/bash

set -e

TEST_DIR="${1:-./testdata}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        # Run the command and capture only stderr, then sort line number (numerically)
        output1=$(../../bin/lexer "$file" | ../../bin/parser | ../../bin/semant 2>&1 1>/dev/null | sort -t: -k2n)
        output2=$(../../bin/lexer "$file" | ../../bin/parser | ./semant 2>&1 1>/dev/null | sort -t: -k2n)

        if ! diff <(echo "$output1") <(echo "$output2"); then
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
