#!/bin/bash

# Exit on any error
set -e

for file in *.cl; do
    if [ -f "$file" ]; then
        echo "Testing $file..."
        
        if ! diff <(../../bin/lexer "$file" | tail -n +2) <(./lexer "$file" | tail -n +2); then
            echo "Failed: Difference found in $file"
            exit 1
        fi
    fi
done

echo "All tests passed!"
exit 0

