import fileinput
import re

calc = {}
def gate(st, pattern, logic):
	m = re.match(pattern, st)
	if m:
		out = m.groups()[-1]
		inputs = m.groups()[:-1]

		def eval():
			if out not in values:
				args = [int(inp) if inp.isdigit() else calc[inp]() for inp in inputs]
				values[out] = logic(*args)
			return values[out] 
		calc[out] = eval
	return m

for line in fileinput.input():
	if gate(line, '(.*) AND (.*) -> (.*)', lambda in1, in2: in1 & in2): continue
	if gate(line, '(.*) OR (.*) -> (.*)', lambda in1, in2: in1 | in2): continue
	if gate(line, '(.*) LSHIFT (.*) -> (.*)', lambda in1, in2: in1 << in2): continue
	if gate(line, '(.*) RSHIFT (.*) -> (.*)', lambda in1, in2: in1 >> in2): continue
	if gate(line, 'NOT (.*) -> (.*)', lambda in1: ~in1): continue
	if gate(line, '(.*) -> (.*)', lambda in1: in1): continue

values = {}
print calc['a']()
values = {'b': values['a']}
print calc['a']()