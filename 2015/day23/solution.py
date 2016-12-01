import sys

def run(stms, a, b):
	ip = 0

	while ip < len(stms) and ip >= 0:
		stm = stms[ip]
		op = stm.split(', ')[0]
		arg = 0 if ',' not in stm else int(stm.split(', ')[1])
		if op.startswith('jmp'): op, arg = 'jmp', int(op.split()[1])

		if   op == 'hlf a': a, ip = a / 2, ip + 1 
		elif op == 'hlf b': b, ip = b / 2, ip + 1
		elif op == 'tpl a': a, ip = a * 3, ip + 1
		elif op == 'tpl b': b, ip = b * 3, ip + 1
		elif op == 'inc a': a, ip = a + 1, ip + 1
		elif op == 'inc b': b, ip = b + 1, ip + 1
		elif op == 'jmp'  : ip = ip + arg
		elif op == 'jie a': ip = ip + arg if a % 2 == 0 else ip + 1
		elif op == 'jie b': ip = ip + arg if b % 2 == 0 else ip + 1
		elif op == 'jio a': ip = ip + arg if a == 1 else ip + 1
		elif op == 'jio b': ip = ip + arg if b == 1 else ip + 1 
		else: raise Exception(op) 

	return b

stms = sys.stdin.read().split('\n')
print run(stms, 0, 0)
print run(stms, 1, 0)