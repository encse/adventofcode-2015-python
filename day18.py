import fileinput

def lit_corners(tbl):
	tblNew = []
	
	for irow in xrange(0, crow):
		tblNew.append(list(tbl[irow]))

	tblNew[0][0] = tblNew[0][ccol-1] = tblNew[crow-1][0] = tblNew[crow-1][ccol-1] = '#'
	return tblNew

def step(tbl):
	tblNew = []
	
	for irow in xrange(0, crow):
		row = []
		for icol in xrange(0, ccol):
			c = 0
			if icol-1 >= 0 and irow-1 >= 0 and tbl[irow -1][icol-1] == '#': c+=1
			if irow-1 >= 0 and tbl[irow -1][icol] == '#': c+=1
			if icol+1 < ccol and irow-1 >= 0 and tbl[irow -1][icol+1] == '#': c+=1
			
			if icol-1 >= 0 and tbl[irow][icol-1] == '#': c+=1
			if icol+1 < ccol and tbl[irow][icol+1] == '#': c+=1
			
			if icol-1 >= 0 and irow+1 < crow and tbl[irow +1][icol-1] == '#': c+=1
			if irow+1 < crow and tbl[irow +1][icol] == '#': c+=1
			if icol+1 < ccol and irow+1 < crow and tbl[irow +1][icol+1] == '#': c+=1

			if tbl[irow][icol] == '#' :
				row .append('#' if c ==2 or c ==3 else '.')
			elif tbl[irow][icol] == '.':
				row .append('#' if c == 3 else '.')
			
		tblNew.append(row)
	return tblNew
tbl1 = []
for line in fileinput.input():
	tbl1.append(list(line.strip()))
crow = len(tbl1)
ccol = len(tbl1[0])

tbl2 = lit_corners(tbl1)

for i in xrange(0,100):
	tbl1 = step(tbl1)
	tbl2 = lit_corners(step(tbl2))

s1 = 0
s2 = 0
for irow in xrange(0, crow):
	for icol in xrange(0, ccol):
		if tbl1[irow][icol] == '#':
			s1 += 1
		if tbl2[irow][icol] == '#':
			s2 += 1
print s1
print s2