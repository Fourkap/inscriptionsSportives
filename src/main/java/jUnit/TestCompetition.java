package jUnit;

import org.junit.*;

import static org.junit.Assert.*;

import java.time.LocalDate;

import inscriptions.Competition;
import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

public class TestCompetition {

	protected Inscriptions inscription = Inscriptions.getInscriptions();
	protected Personne alex, thomas;
	protected Competition ldc, cdm;
	protected Equipe france, allemagne;
	protected LocalDate premiereDateDeCloture = LocalDate.of(2022, 10, 22);
	protected LocalDate deuxiemeDateDeCloture = LocalDate.of(2017, 01, 31);
	
	@Before
	public void setUp() throws Exception {				
		this.ldc = inscription.createCompetition("Ligue des champions", premiereDateDeCloture, false);
		this.cdm = inscription.createCompetition("Coupe du monde", deuxiemeDateDeCloture, true);
		this.alex = inscription.createPersonne("Alex","foot2rue","deniro.Robert@youtalkingtome.com");
		this.thomas = inscription.createPersonne("Thomas","footSansrue","marlon.brando@thgodfhater.com");
		this.france = inscription.createEquipe("France");
		this.allemagne = inscription.createEquipe("Allemagne");
	}

	@Test
	public void testSetNom() {
		this.cdm.setNom("World cup");
		assertEquals("World cup", cdm.getNom());
	}

	@Test
	public void testGetNom() {
		assertEquals("Ligue des champions", ldc.getNom());
	}
	
	@Test
	public void testEstEnEquipe() {
		assertFalse(this.ldc.estEnEquipe());
		assertTrue(this.cdm.estEnEquipe());	
	}
	
	@Test
	public void testGetDateCloture() {
		assertEquals(premiereDateDeCloture, ldc.getDateCloture());
	}
	
	@Test
	public void testSetDateCloture() {
		ldc.setDateCloture(this.premiereDateDeCloture);
		assertEquals(this.premiereDateDeCloture, ldc.getDateCloture());
	}
	
	@Test
	public void testInscriptionsOuvertes(){
		assertTrue(ldc.inscriptionsOuvertes() && !cdm.inscriptionsOuvertes());
	}
	
	@Test
	public void TestAddPersonne() {
		assertTrue(ldc.add(alex));
		assertTrue(ldc.getCandidats().contains(alex));
	}
	
	@Test
	public void TestAddEquipe() {
		cdm.setDateCloture(premiereDateDeCloture);
		assertTrue(cdm.add(france) );
		assertTrue(cdm.getCandidats().contains(france));
		cdm.setDateCloture(this.deuxiemeDateDeCloture);
	}
	
	@Test
	public void TestRemoveCandidat() {
		assertTrue(ldc.add(alex));
		assertTrue(ldc.remove(alex));
		assertFalse(ldc.getCandidats().contains(alex));
	}

}
