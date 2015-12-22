import fileinput

input = next(fileinput.input())

level = 0
for ch in input:
	if ch == '(': 
		level += 1
	else:
		level -= 1

print level


level = 0
i = 1
for ch in input:
	if ch == '(': 
		level += 1
	else:
		level -= 1

	if level < 0:
		break
	i+=1
print i