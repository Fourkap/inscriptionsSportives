package jUnit;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

public class TestPersonne {
	
	private Inscriptions inscription = Inscriptions.getInscriptions();; 
	private Personne alex;

	
	@Before
	public void setUp() throws Exception {
		 alex = inscription.createPersonne("foot2rue", "Alex", "alex@foot2.rue");
	}
	
	@Test
	public void testGetPrenom() {
		assertEquals("Alex", alex.getPrenom());	
	}
	
	@Test
	public void testSetPrenom() {
		alex.setPrenom("Thomas");
		assertEquals("Thomas", alex.getPrenom());
	}
	
	@Test
	public void testSetNom() {
		alex.setNom("footSansrue");
		assertEquals("footSansrue", alex.getNom());
	}

	@Test
	public void testGetNom() {
		assertTrue(alex.getNom(), alex.getNom().equals("foot2rue"));
	}
	
	@Test
	public void testSetMail() {
		alex.setMail("thomas@footSan.rue");	
		assertEquals("thomas@footSan.rue", alex.getMail());
	}
	
	@Test
	public void testGetMail() {
		assertTrue(alex.getMail(), alex.getMail().equals("alex@foot2.rue"));
	}
	
	@Test
	public void testGetEquipe() {
		Equipe france = inscription.createEquipe("France");
		assertTrue(france.add(alex));
		Set<Equipe> setFrance = alex.getEquipes();
		assertTrue(setFrance.contains(france));
	}
	@Test
	public void testGetPersonneEquipe() {
		Equipe equipe1 = inscription.createEquipe("France");
		assertTrue(equipe1.add(alex));
		Set<Equipe> setEquipe1 = alex.getEquipes();
		alex.delete();
		assertFalse(setEquipe1.contains(equipe1));
	}

}