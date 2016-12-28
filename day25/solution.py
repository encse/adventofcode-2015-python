import sys
import re

m = re.match('To continue, please consult the code grid in the manual\.  Enter the code at row (.*), column (.*)\.', sys.stdin.read())
def code(row, col):
	row += col - 1
	exp = row * (row - 1) / 2 + col - 1
	return (20151125 * pow(252533, exp, 33554393)) % 33554393
row, col =map(int, m.group(1, 2)
