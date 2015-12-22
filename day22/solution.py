class Player:
	def __init__(self, base_hp, spells, mana_spent, mana, hp, spell_timers,  armor):
		self.base_hp = base_hp
		self.spells = spells
		self.mana_spent = mana_spent
		self.mana = mana
		self.hp = hp
		self.spell_timers = spell_timers
		self.armor = armor

	@staticmethod
	def create(base_hp, base_mana, spells):
		return Player(base_hp, { spell.name: spell for spell in spells }, 0, base_mana, base_hp, {}, 0)

	def dup(self):
		return Player(self.base_hp, self.spells, self.mana_spent, self.mana, self.hp, dict(self.spell_timers), self.armor)
	
	def is_wizard(self): 
		return len(self.spells) > 1
	
	def get_spells(self):
		return (spell for spell_name, spell in self.spells.iteritems() 
			if self.mana >= spell.mana and self.spell_timers.get(spell_name, 0) <= 1)

def spell(name=None, mana=0, rounds=0, cast={}, over_time={}, uncast={}):
	class Spell:
		def __init__(self):
			self.name = name
			self.mana = mana

		def cast(self, player1, player2): 
			self.__process(cast, player1, player2)
			player1.mana -= mana
			player1.mana_spent += mana
			if rounds > 0:
				player1.spell_timers[name] = rounds

		def over_time(self, player1, player2): 
			self.__process(over_time, player1, player2)

		def uncast(self, player1, player2): 
			self.__process(uncast, player1, player2)

		def __process(self, actions, player1, player2):
			for action, arg in actions.iteritems():
				if action == 'damage':  player2.hp -= max(1, arg - player2.armor)
				if action == 'heal': player1.hp = min(player1.base_hp, player1.hp + arg)
				if action == 'armor': player1.armor += arg
				if action == 'recover_mana': player1.mana += arg

	return Spell()

def strategies(player, boss, is_hard):
	
	def do_spells(player, other_player):
		for spell_name, timer in dict(player.spell_timers).iteritems():
			spell = player.spells[spell_name]
			spell.over_time(player, other_player)
			if timer == 1:
				spell.uncast(player, other_player)
				del player.spell_timers[spell_name]
			else:
				player.spell_timers[spell_name] = timer -1

	states = [(player, boss)]
	while len(states) > 0:
		p1_orig, p2_orig = states.pop()
		
		for spell in p1_orig.get_spells():
			p1, p2 = p1_orig.dup(), p2_orig.dup()

			if is_hard and p1.is_wizard():	p1.hp -= 1
			if p1.hp > 0 and p2.hp > 0: do_spells(p1, p2)
			if p1.hp > 0 and p2.hp > 0: do_spells(p2, p1)
			if p1.hp > 0 and p2.hp > 0: spell.cast(p1, p2)
			if p1.hp > 0 and p2.hp > 0:	states.append((p2, p1))
			
			if p1.is_wizard() and p1.hp > 0 and not p2.hp > 0: yield p1.mana_spent
			if p2.is_wizard() and p2.hp > 0 and not p1.hp > 0: yield p2.mana_spent

player = Player.create(50, 500, [
			spell(name = 'Magic missile', mana=53, cast={'damage': 4}),
			spell(name = 'Drain', mana=73, cast={'damage': 2, 'heal': 2}),
			spell(name = 'Shield', mana=113, cast={'armor': 7}, uncast={'armor': -7}, rounds=6),
			spell(name = 'Poison', mana=173, over_time={'damage': 3}, rounds=6),
			spell(name = 'Recharge', mana=229, over_time={'recover_mana': 101}, rounds=5)
		])

boss = Player.create(71, 0, spells=[spell(name='Attack', cast={'damage': 10})])

print min(strategies(player, boss, False))
print min(strategies(player, boss, True))