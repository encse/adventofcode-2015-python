import math
import itertools as it

weapons = [
	['Dagger', 8, 4, 0], ['Shortsword', 10, 5, 0], ['Warhammer', 25, 6, 0], 
	['Longsword', 40, 7, 0], ['Greataxe', 74, 8, 0]
]
armors = [
	['Leather', 13, 0, 1], ['Chainmail', 31, 0, 2],	['Splintmail', 53, 0, 3],
	['Bandedmail', 75, 0, 4], ['Platemail', 102, 0, 5]
]
rings = [
	['Damage +1', 25, 1, 0], ['Damage +2', 50, 2, 0], ['Damage +3', 100, 3, 0], 
	['Defense +1', 20, 0, 1], ['Defense +2', 40, 0, 2],	['Defense +3', 80, 0, 3]
]

def defeats(hp1, damage1, armor1, hp2, damage2, armor2):
	damage_dealt_by1 = max(1, damage1 - armor2)
	damage_dealt_by2 = max(1, damage2 - armor1) 
	return math.ceil( float(hp2) / damage_dealt_by1 ) <= math.ceil( float(hp1) / damage_dealt_by2 )

def opt(items): 
	return it.chain([['nothing', 0,0,0]], items)

min_price, max_price = 1000000, 0
for weapon, armor, ring1, ring2 in it.product(weapons, opt(armors), opt(rings), opt(rings)):
	if ring1 != ring2:
		sum_price, sum_damage, sum_armor = [sum(x) for x in zip(armor, weapon, ring1, ring2)[1:]]
		if defeats(100, sum_damage, sum_armor, 103, 9, 2):
			min_price = min(sum_price, min_price)
		else:
			max_price = max(sum_price, max_price)
print min_price
print max_price