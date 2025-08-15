#!/bin/bash

set -e

TEST_DIR="${1:-./testdata}"

if [ ! -d "$TEST_DIR" ]; then
    echo "Error: Directory '$TEST_DIR' does not exist"
    exit 1
fi

failed=0

for file in "$TEST_DIR"/*.cl; do
    if [ -f "$file" ]; then
        echo -n "Testing $file: "

        ref_asm="${file}-reference.s"
        my_asm="${file}-my.s"

        # Compile with reference implementation
        if ! ../../bin/lexer "$file" | ../../bin/parser | ../../bin/semant | ../../bin/cgen > "$ref_asm" 2> /tmp/ref_compile_error.txt; then
            echo "FAILED (reference compilation)"
            echo "Compilation errors:"
            cat /tmp/ref_compile_error.txt
            failed=1
            continue
        fi

        # Compile with my implementation
        if ! ../PA2J/lexer "$file" | ../PA3J/parser | ../PA4J/semant | ./cgen > "$my_asm" 2> /tmp/my_compile_error.txt; then
            echo "FAILED (my compilation)"
            echo "Compilation errors:"
            cat /tmp/my_compile_error.txt
            failed=1
            continue
        fi

        # Run reference assembly in SPIM
        if ! ref_output=$(../../bin/spim -file "$ref_asm" 2>&1); then
            echo "FAILED (reference SPIM simulation)"
            failed=1
            continue
        fi

        # Run my assembly in SPIM
        if ! my_output=$(../../bin/spim -file "$my_asm" 2>&1); then
            echo "FAILED (my SPIM simulation)"
            failed=1
            continue
        fi

        # Compare outputs
        if ! diff_output=$(diff -u <(echo "$ref_output") <(echo "$my_output")); then
            echo "FAILED (Outputs differ between implementations)"
            echo "-reference +my"
            echo "$diff_output"
            failed=1
        else
            echo "OK"
        fi
    fi
done

if [ "$failed" -ne 0 ]; then
    exit 1
fi

echo "All tests passed!"
