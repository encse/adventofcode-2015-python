import re
import fileinput

wires = {}
class wire:
	def __init__(self, inputs, logic):
		self.inputs = inputs
   	 	self.logic = logic
   	 	self.value = None

	def eval(self):
		if not self.value:
			args = [int(inp) if inp.isdigit() else wires[inp].eval() for inp in self.inputs]
			self.value = self.logic(*args)
		return self.value 

def gate(st, pattern, logic):
	m = re.match(pattern, st)
	if m:
		wires[m.groups()[-1]] = wire(m.groups()[:-1], logic)
	return m

for line in fileinput.input():
	if gate(line, '(.*) AND (.*) -> (.*)', lambda in1, in2: in1 & in2): continue
	if gate(line, '(.*) OR (.*) -> (.*)', lambda in1, in2: in1 | in2): continue
	if gate(line, '(.*) LSHIFT (.*) -> (.*)', lambda in1, in2: in1 << in2): continue
	if gate(line, '(.*) RSHIFT (.*) -> (.*)', lambda in1, in2: in1 >> in2): continue
	if gate(line, 'NOT (.*) -> (.*)', lambda in1: ~in1): continue
	if gate(line, '(.*) -> (.*)', lambda in1: in1): continue

res1 = wires['a'].eval()

for key in wires: 
	wires[key].value = None 
wires['b'].value = res1

print res1
print wires['a'].eval()