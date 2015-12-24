import sys
from operator import mul
def product(list): return reduce(mul, list, 1)

def pick(max_len, w, ps):
	if w == 0:
		yield ([],ps)
	elif max_len == 0:
		return
	elif len(ps) == 0:
		return
	else:
		if ps[0] > w:
			return
		for (r1, r2) in pick(max_len - 1, w - ps[0], ps[1:]):
			yield ([ps[0]] + r1, r2)
		for (r1, r2) in pick(max_len, w, ps[1:]):
			yield (r1, [ps[0]] + r2) 


weights = map(int, sys.stdin.read().split('\n'))
w = sum(weights)/4
print w, len(weights)
i = 0

minQ = product(weights) * 2
minL = len(weights) * 2

for (p1, w1) in pick(4, w, weights):
	for (p2, w2) in pick(len(weights), w, w1):
		for (p3, p4) in pick(len(weights), w, w2):
			i+=1
			if len(p1) < minL:
				minL = len(p1)
				minQ = product(p1)
			elif len(p1) == minL:
				minQ = min (minQ, product(p1))
			break	
print i, minL, minQ
