import fileinput

def is_nice1(word):

	if len ([c for c in word if c in 'aeiou' ]) < 3:
		return False

	if not any ([(p,q) for (p,q) in zip(word, word[1:]) if p == q]):
		return False
	
	for st in ['ab', 'cd', 'pq', 'xy']:
		if st in word:
			return False

	return True

def is_nice2(word):

	ok = False
	for i in range(0, len(word)-2):
		st = word[i:i+2]
		rest = word[(i+2):]

		if st in rest:
			ok = True
			break
	
	if not ok:
		return False


	if not any ([(p,q,r) for (p,q,r) in zip(word, word[1:], word[2:]) if p == r]):
		return False
	

	return True

nice1 = 0
nice2 = 0
for word in fileinput.input():
	if is_nice1(word):
		nice1 += 1
	if is_nice2(word):
		nice2 += 1

print nice1
print nice2
