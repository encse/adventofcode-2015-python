import re
import fileinput

calc = {}
class wire:
	def __init__(self, out, inputs, logic):
		self.inputs = inputs
   	 	self.logic = logic
   	 	self.out = out

	def eval(self):
		if self.out not in values:
			args = [int(inp) if inp.isdigit() else calc[inp]() for inp in self.inputs]
			values[self.out] = self.logic(*args)
		return values[self.out] 

def gate(st, pattern, logic):
	m = re.match(pattern, st)
	if m:
		calc[m.groups()[-1]] = wire(m.groups()[-1], m.groups()[:-1], logic).eval
	return m

for line in fileinput.input():
	if gate(line, '(.*) AND (.*) -> (.*)', lambda in1, in2: in1 & in2): continue
	if gate(line, '(.*) OR (.*) -> (.*)', lambda in1, in2: in1 | in2): continue
	if gate(line, '(.*) LSHIFT (.*) -> (.*)', lambda in1, in2: in1 << in2): continue
	if gate(line, '(.*) RSHIFT (.*) -> (.*)', lambda in1, in2: in1 >> in2): continue
	if gate(line, 'NOT (.*) -> (.*)', lambda in1: ~in1): continue
	if gate(line, '(.*) -> (.*)', lambda in1: in1): continue

values = {}
res1 = calc['a']()
values = {'b': res1}
print res1
print calc['a']()