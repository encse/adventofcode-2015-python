import copy
import itertools as it

def log(*args):
	print ' '.join(map(str, args))

class Spell:
	time = 0
	def cast(self, player, other_player):
		log('{0} casts {1}.'.format(player.name, self.__class__.__name__))
	
	def effect_over_time(self, player, other_player):
		log('{0}\'s timer is now {1}.'.format(self.__class__.__name__, self.time))

	def wear_off(self, player, other_player):
		log('{0} wears off'.format(self.__class__.__name__))

class Attack(Spell):
	def __init__(self, damage):
		self.damage = damage

	def cast(self, player, other_player):
		other_player.do_damage(self.damage)
		if other_player.armor > 0:
			log('Boss attacks for', self.damage, '-', other_player.armor,'=', self.damage - other_player.armor, 'damage!')
		else:
			log('Boss attacks for', self.damage, 'damage!')
Attack.mana = 0

class MagicMissile(Spell):
	def cast(self, player, other_player):
		d = other_player.do_damage(4)
		log('Player casts Magic Missile, dealing {0} damage.'.format(d))
MagicMissile.mana = 53


class Drain(Spell):
	def cast(self, player, other_player):
		d = player.increase_hp(2)
		other_player.do_damage(2)
		log('Player casts Drain, dealing 2 damage, and healing {0} hit points.'.format(d))
Drain.mana = 73

class Shield(Spell):
	time = 6
	def cast(self, player, other_player):
		player.armor += 7
		log(player.name, 'casts Shield, increasing armor by 7.')

	def wear_off(self, player, other_player):
		player.armor -= 7
		log('Shield wears off, decreasing armor by 7.')

Shield.mana = 113

class Poison(Spell):
	time = 6
	def effect_over_time(self, player, other_player):
		other_player.do_damage(3)
		log('Poison deals 3 damage; its timer is now {0}.'.format(self.time))
Poison.mana = 173


class Recharge(Spell):
	time = 5
	def effect_over_time(self, player, other_player):
		player.mana += 101
		log('Recharge provides 101 mana; its timer is now {0}.'.format(self.time))
Recharge.mana = 229


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

def get_next_steps(player, other_player):
		for Spell in player.spell_book:
			is_active = False
			for active_spell in player.active_spells:
				if isinstance(active_spell, Spell):
					is_active = True
					break
			if not is_active:
				yield Spell()

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
		self.mana -= spell.mana
		if spell.time > 0:
			self.active_spells.insert(0, spell)

	def dup(self):
		return copy.deepcopy(self)

	def __str__(self):
		if self.base_mana > 0:
			return '{0} has {1} hit points, {2} armor, {3} mana'.format(self.name, self.hp, self.armor, self.mana)
		else:
			return '{0} has {1} hit points'.format(self.name, self.hp)

def match(p1, p2, p1_steps, p2_steps):
	wizard = p1
	warrior = p2
	while (True):

		log('--', p1.name, 'turn --')
		log('--', p1 if p1.mana > 0 else p2)
		log('--', p2 if p1.mana > 0 else p1)

		(p1, p2) = init_round(p1, p2)
		if p1.hp <= 0 and p2.hp <=0:
			log('egal')
			return
		elif p1.hp <=0:
			log(p2.name, 'wins')
			return
		elif p2.hp <=0:
			log(p1.name, 'wins')
			return
		else:
			spell = next(p1_steps)
			p1.cast(spell, p2)	
			if p1.hp <=0:
				log('p1 wins')
				return
			p1, p2 = p2, p1
			p1_steps, p2_steps = p2_steps, p1_steps

		log('')

player_hp = 10
player_mana = 250
boss_hp = 13
boss_damage =8 

player = Player('Player', player_hp,player_hp,0,0,player_mana,player_mana,[MagicMissile, Drain, Shield, Poison, Recharge], [])
boss = Player('Boss', boss_hp,boss_hp,0,0,0,0,[Attack], [])

match(player, boss,iter([Poison(), MagicMissile()]),iter([Attack(boss_damage), Attack(boss_damage)]) )

# match(player, boss,iter([Recharge(), Shield(), Drain(), Poison(), MagicMissile()]),iter([Attack(boss_damage)] * 1000) )
