def get_divisors(n):
	for i in xrange(1, n+1):
		if n%i == 0:
			yield i

def is_prime(n):
	if n<2: return False
	if n == 2: return True
	for i in xrange(2, n):
		if n%i == 0:
			return False
		if i*i>n:
			return True

def lnko(a,b):
	while a!=b:
		if a>b: a=a-b
		else: b = b-a

	return a

divisors = [set(), {1}, {1,2}]
i=3
while True:
	for d1 in xrange(2, i+1):
		if i % d1 == 0:
			if d1 >= len(divisors):
				print 'coki', i, d1
			d2 = i/d1
			s = set()
			divisors.append(s)
			for s1 in divisors[d1]:
				for s2 in divisors[d2]:
					s |= {s1*s2}
			break
		if d1*d1 > i:
			divisors.append(set([1,i]))
			break

	# if sum(divisors[i]) != sum(get_divisors(i)):
	# 	print 'coki2', i, divisors[i], sum(get_divisors(i))
	# 	break
		
	if sum(divisors[i]) * 10 >= 36000000:
		break

	if i<10 or i %1000 == 0: print i, sum(divisors[i])*10
	i+=1

# i=3
# while True:

# dsum = [0,1,3]
# i=3
# while True:
# 	for d1 in xrange(2, i+1):
# 		if i % d1 == 0:
# 			if d1 >= len(dsum):
# 				print 'coki', i, d1
# 			d2 = i/d1
# 			dsum.append(dsum[d1] + dsum[d2] - dsum[lnko(d1, d2)] + i)
# 			break
# 		if d1*d1 > i:
# 			dsum.append( 1 + i)
# 			break

# 	if dsum[i] != sum(get_divisors(i)):
# 		print 'coki2', i, dsum[i], sum(get_divisors(i))
# 		break
		
# 	if dsum[i] * 10 >= 36000000:
# 		break

# 	if i<10 or i %1000 == 0: print i, dsum[i]*10
# 	i+=1
print i 

