#!/usr/bin/env python3

with open("input/day02.txt") as input:
    content = input.readlines()


def translate(item):
    return {"A": 1, "B": 2, "C": 3,
            "X": 1, "Y": 2, "Z": 3}.get(item)


def parse(lines):
    return [[translate(them), translate(me)]
            for them, me in [line.split() for line in lines]]


def score (play):
    if (play[0] - play[1]) % 3 == 1:  # win
        return play[1] + 6
    if (play[0] - play[1]) % 3 == 2:  # loss
        return play[1]
    return play[1] + 3  # draw


test_input = ["A Y", "B X", "C Z"]


def part1(input):
    return sum([score(play) for play in parse(input)])


print("part 1 test (should be 15): ", part1(test_input))
print("part 1: ", part1(content))
