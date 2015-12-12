import fileinput
import itertools as it
import re
import math

st =  '1321131112'
for i in range(0,50):
	newSt = ''
	chPrev = ''

	cch = 1
	for ch in st:
		if ch == chPrev:
			cch += 1
		elif chPrev != '':
			newSt += str(cch) + chPrev
			cch = 1
		chPrev = ch
	newSt += str(cch) + chPrev
	st = newSt

print len(st)