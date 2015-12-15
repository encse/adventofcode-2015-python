import fileinput
import re
from numpy import *

def mixtures(ingredient_count, total_amount):
	if ingredient_count == 1:
		yield [total_amount]
	else :
		for amount in xrange(0, total_amount + 1):
			for mixture in mixtures(ingredient_count - 1, total_amount - amount):
				yield [amount] + mixture

def get_ingredients():
	ingredients = []
	for line in fileinput.input():
		m = re.match('(.*): capacity (.*), durability (.*), flavor (.*), texture (.*), calories (.*)', line)
		ingredients.append([int(m.group(2)), int(m.group(3)), int(m.group(4)), int(m.group(5)), int(m.group(6))])
	
	return matrix(ingredients)

ingredients = get_ingredients()

ingredient_count = len(ingredients)

max_value1 = 0
max_value2 = 0
for mixture in mixtures(ingredient_count,100):
	v = maximum(array([0,0,0,0,0]), (matrix([[1]*ingredient_count]) *  diag(mixture) * ingredients).A1)
	calories = v[4]
	value = prod(v[0:4])
	max_value1 = max(max_value1, value)
	if calories == 500:
		max_value2 = max(max_value2, value)
	
print max_value1
print max_value2