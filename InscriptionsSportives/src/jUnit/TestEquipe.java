package jUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.*;

import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

public class TestEquipe {

	protected Inscriptions inscription = Inscriptions.getInscriptions();; 
	protected Personne alex, thomas;
	protected Equipe france;

	
	@Before
	public void setUp() throws Exception {
		 this.alex = inscription.createPersonne("Alex", "foot2rue", "alex@foot2.rue");
		 this.thomas = inscription.createPersonne("Thomas", "footSansRue", "thomas@footSans.rue");
		 this.france = inscription.createEquipe("France");
	}
	
	@Test
	public void testGetMembres() {
		Set<Personne> listeDesMembres = this.france.getMembres();
		assertTrue(this.france.add(this.alex));
		assertTrue(this.france.add(this.thomas));
		assertEquals(listeDesMembres, this.france.getMembres());
	}
	
	
	@Test
	public void testAdd() {
		Set<Personne> listeDesMembres = france.getMembres();
		assertTrue(this.france.add(this.alex));
		assertTrue(listeDesMembres.contains(this.alex));
		assertFalse(listeDesMembres.contains(this.thomas));
	}
	
	@Test
	public void testRemove() {
		Set<Personne> listeDesMembres = this.france.getMembres();
		assertTrue(this.france.add(this.alex));
		assertTrue(this.france.remove(this.alex));
		assertFalse(listeDesMembres.contains(this.alex));
	}

}
