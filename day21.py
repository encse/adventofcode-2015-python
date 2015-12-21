import math

items = {
	'weapons':[
		['Dagger', 8, 4, 0],
		['Shortsword', 10, 5, 0],
		['Warhammer', 25, 6, 0],
		['Longsword', 40, 7, 0],
		['Greataxe', 74, 8, 0]
	],
	'armor':[
		['nothing', 0, 0, 0],
		['Leather', 13, 0, 1],
		['Chainmail', 31, 0, 2],
		['Splintmail', 53, 0, 3],
		['Bandedmail', 75, 0, 4],
		['Platemail', 102, 0, 5],
	],
	'rings':[
		['nothing', 0, 0, 0],
		['nothing', 0, 0, 0],
		['Damage +1', 25, 1, 0],
		['Damage +2', 50, 2, 0],
		['Damage +3', 100, 3, 0],
		['Defense +1', 20, 0, 1],
		['Defense +2', 40, 0, 2],
		['Defense +3', 80, 0, 3]
	]
}

def defeats(hp1, damage1, armor1, hp2, damage2, armor2):
	damage_dealt_by1 = max(1, damage1 - armor2)
	damage_dealt_by2 = max(1, damage2 - armor1) 
	rounds_needed_to_win1 = math.ceil( float(hp2) / damage_dealt_by1 )
	rounds_needed_to_win2 = math.ceil( float(hp1) / damage_dealt_by2 )
	return rounds_needed_to_win1 <= rounds_needed_to_win2

minPrice = 1000000
maxPrice = 0
for armor in items['armor']:
	for weapon in items['weapons']:
		for ring1 in items['rings']:
			for ring2 in items['rings']:
				if ring1 != ring2:
					price = sum(map (lambda x: x[1], [armor, weapon, ring1, ring2]))
					sum_damage = sum(map (lambda x: x[2], [armor, weapon, ring1, ring2]))
					sum_armor = sum(map (lambda x: x[3], [armor, weapon, ring1, ring2]))
					if defeats(100, sum_damage, sum_armor, 103, 9, 2):
						minPrice = min(price, minPrice)
					else:
						maxPrice = max(price, maxPrice)
print minPrice
print maxPrice
