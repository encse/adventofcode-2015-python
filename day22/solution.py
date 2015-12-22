import itertools as it
import sys

def spell(mana=0, time=0):
	class Spell:
		def cast(self, player, other_player): pass
		def effect_over_time(self, player, other_player): pass
		def wear_off(self, player, other_player): pass
		def dup(self):
			clone = self.__class__()
			clone.time = self.time
			return clone
	Spell.mana, Spell.time = mana, time
	return Spell

def attack(damage):
	class Action(spell()):
		def cast(self, player, other_player):
			other_player.damage(damage)
	return Action

def magic_missile(mana, damage):
	class Action(spell(mana)):
		def cast(self, player, other_player):
			other_player.damage(damage)
	return Action

def drain(mana, damage, heal):
	class Action(spell(mana)):
		def cast(self, player, other_player):
			player.heal(heal)
			other_player.damage(damage)
	return Action

def shield(mana, time, armor):
	class Action(spell(mana,time)):
		def cast(self, player, other_player):
			player.armor += armor

		def wear_off(self, player, other_player):
			player.armor -= armor
	return Action

def poison(mana, time, damage):
	class Action(spell(mana, time)):
		def effect_over_time(self, player, other_player):
			other_player.damage(damage)
	return Action

def recharge(mana, time, mana_recharged):
	class Action(spell(mana, time)):
		def effect_over_time(self, player, other_player):
			player.mana += mana_recharged
	return Action

class Player:
	def __init__(self, base_hp, base_mana):
		self.hp = self.base_hp = base_hp
		self.armor = self.base_armor = 0
		self.mana = self.base_mana = base_mana
		self.spell_book = []
		self.active_spells = []
		self.mana_spent = 0
		
	def with_spell(self, spell):
		self.spell_book.append(spell)
		return self

	def dup(self):
		clone = Player(self.base_hp, self.base_mana)
		clone.spell_book = self.spell_book 
		clone.mana_spent = self.mana_spent
		clone.mana = self.mana
		clone.hp = self.hp
		clone.active_spells = map(lambda x: x.dup(), self.active_spells)
		clone.base_armor = self.base_armor
		clone.armor = self.armor
		return clone
	
	def is_wizard(self): 
		return self.base_mana > 0
	
	def damage(self, d): 
		self.hp -= max(1, d - self.armor)

	def heal(self, d): 
		self.hp = min(self.base_hp, self.hp + d)

	def cast(self, spell, other_player):
		spell.cast(self, other_player)
		self.mana, self.mana_spent = self.mana - spell.mana, self.mana_spent + spell.mana
		if spell.time > 0:
			self.active_spells.insert(0, spell)

	def get_steps(self):
		for Spell in self.spell_book:
			is_active = False
			for active_spell in self.active_spells:
				if isinstance(active_spell, Spell) and active_spell.time > 1:
					is_active = True
					break

			if not is_active and self.mana >= Spell.mana:
				yield Spell()

def strategies(player, boss, is_hard):
	
	def do_spells(player, other_player):
		for spell in list(player.active_spells):

			spell.time -= 1
			spell.effect_over_time(player, other_player)
		
			if spell.time == 0:
				spell.wear_off(player, other_player)
				player.active_spells.remove(spell)

	states = [(player, boss)]
	while len(states) > 0:
		p1_orig, p2_orig = states.pop()
		
		for step in p1_orig.get_steps():
			p1, p2 = p1_orig.dup(), p2_orig.dup()

			if is_hard and p1.is_wizard():	p1.hp -= 1
			if p1.hp > 0 and p2.hp > 0: do_spells(p1, p2)
			if p1.hp > 0 and p2.hp > 0: do_spells(p2, p1)
			if p1.hp > 0 and p2.hp > 0:	p1.cast(step, p2)
			if p1.hp > 0 and p2.hp > 0:	states.append((p2, p1))
			
			if p1.is_wizard() and p1.hp > 0 and not p2.hp > 0: yield p1.mana_spent
			if p2.is_wizard() and p2.hp > 0 and not p1.hp > 0: yield p2.mana_spent

player = (Player(50, 500)
	.with_spell(magic_missile(mana = 53, damage = 4))
	.with_spell(drain(mana = 73, damage = 2, heal = 2))
	.with_spell(shield(mana = 113, time = 6, armor= 7))
	.with_spell(poison(mana = 173, time = 6, damage = 3))
	.with_spell(recharge(mana = 229, time = 5, mana_recharged = 101)))

boss = (Player(base_hp = 71, base_mana=0)
	.with_spell(attack(damage = 10)))

print min(strategies(player, boss, False))
print min(strategies(player, boss, True))