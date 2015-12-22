import fileinput
import itertools as it
import re
import math

distances = {}
cities = set()

for line in fileinput.input():
	m = re.match('(.*) to (.*) = (.*)', line)
	(frm,to,d) = (m.group(1), m.group(2), int(m.group(3)))

	distances[(frm, to)] = d
	distances[(to, frm)] = d

	cities.add(frm)
	cities.add(to)


min = 100000000000000
max = 0
for route in it.permutations(cities):
	d = sum ([distances[(frm,to)] for (frm,to) in zip(route, route[1:])])
	if d < min:
		min = d
	if d > max:
		max = d
print (min, max)