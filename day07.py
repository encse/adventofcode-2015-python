import re
import fileinput

t=0
wires = {}
def out(wire, eval):
	val = [None, t]
	
	def cache():
		if not val[0] or t != val[1]:
			val[0] = eval()
			val[1] = t
		return val[0]

	wires[wire] = cache
	return cache

def input(st):
	if st.isdigit(): 
		v = int(st)
		def eval():
			return v
		return eval
	else: 
		def eval():
			return wires[st]()
		return eval

def gate2(op, logic):
	def parse(st):
		m = re.match('(.*) '+op+' (.*) -> (.*)', st)
		if m:
			in1 = input(m.group(1))
			in2 = input(m.group(2))
			return out(m.group(3), lambda : logic(in1, in2))
		return None
	return parse

def gate1(op, logic):
	def parse(st):
		m = re.match(op+' (.*) -> (.*)', st)
		if m:
			in1 = input(m.group(1))
			return out(m.group(2), lambda : logic(in1))
		return None
	return parse

def gate0():
	def parse(st):
		m = re.match('(.*) -> (.*)', st)
		if m:
			in1 = input(m.group(1))
			return out(m.group(2), in1)
		return None
	return parse

gates = [
	gate2('AND', lambda in1, in2 : in1() & in2()),
	gate2('OR', lambda in1, in2 : in1() | in2()),
	gate2('LSHIFT', lambda in1, in2 : in1() << in2()),
	gate2('RSHIFT', lambda in1, in2 : in1() >> in2()),
	gate1('NOT', lambda in1 : 65535 - in1()),
	gate0()
]

for line in fileinput.input():
	for gate in gates:
		if (gate(line)):
			break;

res1 =  wires['a']()

wires['b'] = lambda : res1

t += 1 

print res1
print wires['a']()