import fileinput
import re

m1 =  [[0 for x in range(1000)] for x in range(1000)]
m2 =  [[0 for x in range(1000)] for x in range(1000)]

def process1(m, inst, left,top,right,bottom):
	for x in range(left, right + 1):
		for y in range(top, bottom + 1):
			if inst == 'turn on':
				m[x][y] = 1
			if inst == 'turn off':
				m[x][y] = 0
			if inst == 'toggle':
				m[x][y] = 1 - m[x][y]

def process2(m, inst, left,top,right,bottom):
	for x in range(left, right + 1):
		for y in range(top, bottom + 1):
			if inst == 'turn on':
				m[x][y] += 1
			if inst == 'turn off':
				m[x][y] = max(0, m[x][y] - 1)
			if inst == 'toggle':
				m[x][y] += 2

def sum2(m):
	res = 0
	for x in range(0, 1000):
		res += sum(m[x])
	return res

for line in fileinput.input():
	p = re.match('([^0-9]*) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)', line)
	(inst, left,top,right,bottom) = (p.group(1),int(p.group(2)),int(p.group(3)),int(p.group(4)),int(p.group(5)))
	process1(m1, inst, left, top, right,bottom)
	process2(m2, inst, left, top, right,bottom)

print sum2(m1)
print sum2(m2)
