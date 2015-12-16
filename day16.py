import fileinput

def match(aunt, template):
	for key in aunt:
		if key in template:
			cond = template[key]
			if (callable(cond) and not cond(aunt[key])) or (not callable(cond) and aunt[key] != cond):
				return False
	return True

template1={'children': 3, 'cats': 7,'samoyeds': 2,'pomeranians': 3,'akitas': 0,	'vizslas': 0,'goldfish': 5,'trees': 3,'cars': 2, 'perfumes': 1}

template2 = dict(template1)
template2['cats'] = lambda x: x > template1['cats']
template2['trees'] = lambda x: x > template1['trees']
template2['pomeranians'] = lambda x: x < template1['pomeranians']
template2['goldfish'] = lambda x: x < template1['goldfish']

res1 = None
res2 = None

for line in fileinput.input():
	m = line.translate(None, '\n\r:,').split()
	aunt = dict(zip(m[::2], map(int, m[1::2])))
	if match(aunt, template1):
		res1 = aunt['Sue']
	if match(aunt, template2):
		res2 = aunt['Sue']

print res1
print res2