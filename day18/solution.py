import sys
import itertools as it
import copy

def dup(tbl): 
	return copy.deepcopy(tbl)

def lit_corners(tbl):
	tbl[0][0] = tbl[0][ccol-1] = tbl[crow-1][0] = tbl[crow-1][ccol-1] = '#'
	return tbl

def step(tbl):
	tblNew = dup(tbl)
	
	for (irow, icol) in [(irow, icol) for irow in xrange(0, crow) for icol in xrange(0, ccol)]:
		ch = tbl[irow][icol]
		c_neighbour = 0
		for (dx,dy) in [(-1,-1), (0,-1), (1,-1), (-1,0), (1,0), (-1,1), (0,1), (1,1)]:
			(icolN, irowN) = (icol + dx, irow +dy)
			if icolN >= 0 and icolN < ccol and irowN >= 0 and irowN < crow and tbl[irowN][icolN] == '#': 
				c_neighbour+=1
		tblNew[irow][icol] = {'#': '..##.....', '.': '...#.....'}[ch][c_neighbour]
	return tblNew

tbl1 = map(lambda line: list(line.strip()), sys.stdin.readlines())
(crow, ccol) = (len(tbl1), len(tbl1[0]))
tbl2 = lit_corners(copy.deepcopy(tbl1))

for i in xrange(0,100):
	tbl1 = step(tbl1)
	tbl2 = lit_corners(step(tbl2))

print list(it.chain(*tbl1)).count('#')
print list(it.chain(*tbl2)).count('#')