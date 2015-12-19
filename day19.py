import sys
rules = []
for line in sys.stdin:
	line = line.strip()
	if line == "": 
		break
	rules.append(line.split(" => "))

goal = next(sys.stdin)

def prev(src, seen):
	dst = set()
	iStart = 0;
	iLim = 0
	i0 = None
	i=0
	for i in xrange(0, len(src)):
		l = 0
		for rule in rules:
			if src[i:].startswith(rule[1]):
				m = {src[:i] + rule[0] + src[(i+len(rule[1])):]}
				if m not in seen:
					dst |= m
					l = max(l, len(rule[0]))
		if len(dst) > 0:
			i0 = i+1
			iLim = i+l+1
			break
	if i0:
		for i in xrange(i0, iLim):
			for rule in rules:
				if src[i:].startswith(rule[1]):
					m = {src[:i] + rule[0] + src[(i+len(rule[1])):]}
					if m not in seen:
						dst |= m
	return dst

def step(src):
	dst = set()
	for i in xrange(0, len(src)):
		for rule in rules:
			if src[i:].startswith(rule[0]):
				m = {src[:i] + rule[1] + src[(i+len(rule[0])):]}
				dst |= m
	return dst

print len(step(goal))

current = {goal}
seen = set(current)

i = 0
while not 'e' in current:
	next = set()
	i += 1
	for m in current:
		next |= prev(m, seen)
		if goal in next: break
	seen |= next
	current = next

	if not len(current): raise 'coki'

print i