import sys
import math

def product(list):
	return reduce(lambda x, y : x * y, list, 1)

def pick(max_count, sum_weight, packages):
	if sum_weight == 0:
		yield ([],packages)
	elif max_count > 0 and len(packages) > 0:
		if packages[0] > sum_weight:
			return
		for (packages0, packages1) in pick(max_count - 1, sum_weight - packages[0], packages[1:]):
			yield ([packages[0]] + packages0, packages1)
		for (packages0, packages1) in pick(max_count, sum_weight, packages[1:]):
			yield (packages0, [packages[0]] + packages1) 

def splittable(part_count, weight, packages):
	if part_count == 1: 
		return True if sum(packages) == weight else False
	else:
		for (packages0, packages1) in pick(len(packages), weight, packages):
			if splittable(part_count - 1, weight, packages1):
				return True
	return False

def solve(part_count, packages):
	weight = sum(packages) / part_count
	for min_length in xrange(len(packages)):
		minQ = float('inf')
		for (packages0, packages1) in pick(min_length, weight, packages):
			if splittable(part_count - 1, weight, packages1):
				minQ = min (minQ, product(packages0))
		if not math.isinf(minQ):
			return minQ

packages = map(int, sys.stdin.read().split('\n'))
print solve(3, packages)
print solve(4, packages)