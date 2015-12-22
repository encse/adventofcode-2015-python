import copy
import itertools as it

def no_log(*args):
	pass
def do_log(*args):
	print ' '.join(map(str, args))

class Spell:
	time = 0
	def cast(self, player, other_player):
		log('{0} casts {1}.'.format(player.name, self.__class__.__name__))
		pass
	
	def effect_over_time(self, player, other_player):
		log('{0}\'s timer is now {1}.'.format(self.__class__.__name__, self.time))
		pass

	def wear_off(self, player, other_player):
		log('{0} wears off'.format(self.__class__.__name__))
		pass

class Attack(Spell):

	def cast(self, player, other_player):
		other_player.do_damage(self.damage)
		if other_player.armor > 0:
			log('Boss attacks for', self.damage, '-', other_player.armor,'=', self.damage - other_player.armor, 'damage!')
			pass
		else:
			log('Boss attacks for', self.damage, 'damage!')
			pass
Attack.mana = 0

class MagicMissile(Spell):
	def cast(self, player, other_player):
		d = other_player.do_damage(self.damage)
		log('Player casts Magic Missile, dealing {0} damage.'.format(d))
MagicMissile.mana = 53
MagicMissile.damage = 4

class Drain(Spell):
	def cast(self, player, other_player):
		d = player.increase_hp(self.damage)
		other_player.do_damage(self.damage)
		log('Player casts Drain, dealing {0} damage, and healing {1} hit points.'.format(self.damage, d))
		pass
Drain.mana = 73
Drain.damage = 2

class Shield(Spell):
	time = 6
	def cast(self, player, other_player):
		player.armor += 7
		log(player.name, 'casts Shield, increasing armor by 7.')
		pass

	def wear_off(self, player, other_player):
		player.armor -= 7
		log('Shield wears off, decreasing armor by 7.')
		pass

Shield.damage = 0
Shield.mana = 113

class Poison(Spell):
	time = 6
	def effect_over_time(self, player, other_player):
		other_player.do_damage(Poison.damage)
		log('Poison deals {0} damage; its timer is now {0}.'.format(self.damage, self.time))
		pass

Poison.mana = 173
Poison.damage = 3

class Recharge(Spell):
	time = 5
	def effect_over_time(self, player, other_player):
		player.mana += 101
		log('Recharge provides 101 mana; its timer is now {0}.'.format(self.time))
		pass

Recharge.mana = 229
Recharge.damage = 0

class Player:
	def __init__(self, name, base_hp, hp, base_armor, armor, base_mana, mana, spell_book, active_spells):
		self.name = name
		self.base_hp = base_hp
		self.hp = hp
		self.base_armor = base_armor
		self.armor =armor
		self.base_mana = base_mana
		self.mana = mana
		self.spell_book = spell_book
		self.active_spells = active_spells
		self.mana_spent = 0

	def do_damage(self, d):
		d -= self.armor
		if d  <= 0:
			d = 1

		self.hp -= d
		return d 

	def increase_hp(self, d):
		if self.hp + d > self.base_hp:
			d = self.base_hp - self.hp
		self.hp += d
		return d

	def cast(self, spell, other_player):
		spell.cast(self, other_player)
		if self.mana < spell.mana:
			raise 'coki'
		self.mana -= spell.mana
		self.mana_spent += spell.mana
		if spell.time > 0:
			self.active_spells.insert(0, spell)

	def dup(self):
		return copy.deepcopy(self)

	def get_steps(self):
		for Spell in self.spell_book:
			is_active = False
			for active_spell in self.active_spells:
				if isinstance(active_spell, Spell) and active_spell.time > 1:
					is_active = True
					break

			if not is_active and self.mana >= Spell.mana:
				yield Spell()

	def __str__(self):
		if self.base_mana > 0:
			return '{0} has {1} hit points, {2} armor, {3} mana'.format(self.name, self.hp, self.armor, self.mana)
		else:
			return '{0} has {1} hit points'.format(self.name, self.hp)

def wizard(p1, p2):
	return p1 if p1.base_mana > 0 else p2

def warrior(p1, p2):
	return p1 if p1.base_mana == 0 else p2

def do_spells(player, other_player):
	for spell in list(player.active_spells):

		if spell.time > 0:
			spell.time -= 1
			spell.effect_over_time(player, other_player)
	
		if spell.time == 0:
			spell.wear_off(player, other_player)
			player.active_spells.remove(spell)
			
def init_round(player, other_player):
		player = player.dup()
		other_player = other_player.dup()

		do_spells(player, other_player)
		do_spells(other_player, player)
		return player, other_player
	
def turn(p1, p2, step):
	log('--', p1.name, 'turn --')
	log('-', wizard(p1,p2))
	log('-', warrior(p1,p2))
	(p1, p2) = init_round(p1, p2)

	if p1.hp > 0 and p2.hp >0:
		p1.cast(step, p2)

	return (p1, p2)

player_hp, player_mana, boss_hp, Attack.damage = 50, 500, 71, 10
# player_hp, player_mana, boss_hp, Attack.damage = 10, 250, 13, 8
# player_hp, player_mana, boss_hp, Attack.damage = 10, 250, 14, 8

player = Player('Player', player_hp,player_hp,0,0,player_mana,player_mana,[MagicMissile, Drain, Shield, Poison, Recharge], [])
boss = Player('Boss', boss_hp,boss_hp,0,0,0,0,[Attack], [])

def foo(player, boss):
	print 'foo'
	i=0
	states = [(player, boss, [])]
	while len(states) > 0:
		playerOrig, bossOrig, prev_steps = states[0]
		states = states[1:] 
		i+=1
		
		for step in playerOrig.get_steps():
			player, boss = turn(playerOrig, bossOrig, step)
			if player.hp > 0 and boss.hp <= 0:
				yield player.mana_spent, list(prev_steps) + [step]
			elif player.hp > 0 and boss.hp > 0:
				boss_steps = list(boss.get_steps())
				if len(boss_steps) != 1:
					raise 'wtf'
				boss_step = boss_steps[0]
				boss, player = turn(boss, player, boss_step)
				if player.hp > 0 and boss.hp <= 0:
					yield player.mana_spent, list(prev_steps) + [step]
				elif player.hp > 0:
					states.append((player, boss, list(prev_steps) + [step]))
		
		if (i % 10000 == 0):
			print len(states)
			print playerOrig
			print bossOrig
			print ''
			states = sorted(states, key=lambda(p1,p2,_):p2.mana_spent)
		log('')

log = no_log
for mana, steps in foo(player, boss):
	log = do_log
	print mana
	print steps
	break