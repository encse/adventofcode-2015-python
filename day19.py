import sys

def apply_rule(molecule, rule, i): 
	return molecule[:i] + rule[1] + molecule[(i+len(rule[0])):] 

def match(molecule, rule, i): 
	return molecule[i:].startswith(rule[0])

def get_first_match(molecule, rules): 
	return next ((i for i in xrange(len(molecule)) for rule in rules if match(molecule, rule, i)), None)

def get_next_molecules(molecule, rules, lookahead):
	i = get_first_match(molecule, rules)
	if i == None: return []
	return set([apply_rule(molecule, rule, j)
			for j in xrange(i, i+lookahead)
			for rule in rules 
			if match(molecule, rule, j)])
	
rules = []
for line in sys.stdin:
	line = line.strip()
	if line == "": 
		goal = next(sys.stdin)
		break
	rules.append(line.split(" => "))

print len(get_next_molecules(goal, rules, len(goal)))

lookahead = max(map(lambda rule: len(rule[1]), rules)) # longest rule
rules = map(lambda rule: rule[::-1], rules)	# we go backwards so change the left and right side of the rules

molecules = {goal}
i = 0
while not 'e' in molecules:
	molecules = set([next_molecule for molecule in molecules for next_molecule in get_next_molecules(molecule, rules, lookahead)])
	i += 1
print i