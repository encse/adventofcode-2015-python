import re
import fileinput

wires = {}
def gate(st, pattern, logic):
	m = re.match(pattern, st)
	if m:
		state = {'val':None, 't':0}
		inputs = m.groups()[:-1] 
		output = m.groups()[-1]
		def wire():
			if not state['val'] or t != state['t'] :
				args = [int(inp) if inp.isdigit() else wires[inp]() for inp in inputs]
				state['val'] = logic(*args)
				state['t'] = t
			return state['val'] 
		wires[output] = wire
	return m

for line in fileinput.input():
	if gate(line, '(.*) AND (.*) -> (.*)', lambda in1, in2: in1 & in2): continue
	if gate(line, '(.*) OR (.*) -> (.*)', lambda in1, in2: in1 | in2): continue
	if gate(line, '(.*) LSHIFT (.*) -> (.*)', lambda in1, in2: in1 << in2): continue
	if gate(line, '(.*) RSHIFT (.*) -> (.*)', lambda in1, in2: in1 >> in2): continue
	if gate(line, 'NOT (.*) -> (.*)', lambda in1: ~in1): continue
	if gate(line, '(.*) -> (.*)', lambda in1: in1): continue

t = 0
res1 = wires['a']()
wires['b'] = lambda : res1
t = 1
res2 = wires['a']()
print res1
print res2