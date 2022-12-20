#!/usr/bin/env python3


def elf_values(lines):
    elves = []
    current_elf = 0
    for line in content:
        if line == '\n':
            elves.append(current_elf)
            current_elf = 0
        else:
            current_elf += int(line)
    elves.append(current_elf)
    return elves


def part1(elves):
    return max(elves)


def part2(elves):
    return sum(sorted(elves)[-3:])


with open("input/day01.txt") as input:
    content = input.readlines()


print("part 1: ", part1(elf_values(content)))
# 69883


print("part 2: ", part2(elf_values(content)))
# 207576
