import fileinput
import re
from numpy import *

def mixtures(ingredient_count, total_amount):
	if ingredient_count == 1:
		yield [total_amount]
	else:
		for amount in xrange(0, total_amount + 1):
			for mixture in mixtures(ingredient_count - 1, total_amount - amount):
				yield [amount] + mixture

ingredients = []
for line in fileinput.input():
	m = re.match('(.*): capacity (.*), durability (.*), flavor (.*), texture (.*), calories (.*)', line)
	ingredients.append([int(m.group(2)), int(m.group(3)), int(m.group(4)), int(m.group(5)), int(m.group(6))])

ingredients = matrix(ingredients)

max_score1 = 0
max_score2 = 0

for mixture in mixtures(len(ingredients),100):
	v = mixture * ingredients
	if all(v >= 0):
		score = prod(v[0,0:4])
		calories = v[0,4]
		max_score1 = max(max_score1, score)
		if calories == 500:
		 	max_score2 = max(max_score2, score)
	
print max_score1
print max_score2