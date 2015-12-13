import fileinput
import itertools as it

def get_max_v(d, people):
	maxv = 0
	for order in it.permutations(people):
		pairs = [ (p1,p2) for (p1,p2) in zip(order, order[1:])] + [(order[-1], order[0])] 
		sv = sum ( [ d[(p1,p2)] + d[(p2,p1)] for (p1,p2) in pairs ])
		maxv = max (maxv, sv)

	return maxv

d = {}
people = set()

for line in fileinput.input():
	m = line.split(' ')
	(p1, num, p2) = (m[0], (1 if m[2] == 'gain' else -1) * int(m[3]), m[10].strip()[:-1])
	
	d[(p1,p2)] = num
	people.add(p1) 

print get_max_v(d, people)

for p in people:
	d[(p,'me')] = d[('me',p)] = 0

people.add('me')

print get_max_v(d, people)
