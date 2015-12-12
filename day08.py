import fileinput
s1 = 0
s2 = 0
for line in fileinput.input():
	line = line.strip()
	s1 += len(line) - len(eval(line))
	s2 += 2 + len(re.escape(line)) - len(line)
print s1
print s2

