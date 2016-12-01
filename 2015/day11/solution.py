
def nextSt(st):
	i = len(st) - 1
	stRes = list(st)
	while i >= 0:
		chNext = 'bcdefghjjkmmnppqrstuvwxyza'[ord(st[i]) - ord('a')]
		stRes[i] = chNext
		if chNext != 'a':
			break
		i -= 1
	return ''.join(stRes)

def ok(st):
	if any ([ch for ch in st if ch in 'iol' ]):
		return False

	if len (set([(p,q) for (p,q) in zip(st, st[1:]) if p == q])) < 2:
		return False
	
	if not any ([(p,q,r) for (p,q,r) in zip(st, st[1:], st[2:]) if ord(p) + 1 == ord(q) and ord(q) + 1 == ord(r)]):
		return False
	return True

def nextPass(passwd):
	passwd = nextSt(passwd)
	while not ok(passwd):
		passwd = nextSt(passwd)
	return passwd

passwd ='cqjxjnds'

passwd = nextPass(passwd)
print passwd

passwd = nextPass(passwd)
print passwd