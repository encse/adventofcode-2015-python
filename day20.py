dsum1 = [1] * 5000000
dsum2 = [1] * 5000000
i=2
res1 = None
res2 = None
while True:
	d = i
	while d < len(dsum1):
		dsum1[d] += i
		if d < i*50:
			dsum2[d] += i
		d += i

	if not res1 and dsum1[i]*10 >= 36000000:
		res1 = i
	if not res2 and dsum2[i]*11 >= 36000000:
		res2 = i
	if res1 and res2:
		break
	i+=1

print res1
print res2

