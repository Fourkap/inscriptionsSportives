package inscriptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Session;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Point d'entrée dans l'application, un seul objet de type Inscription permet
 * de gérer les compétitions, candidats (de type equipe ou personne) ainsi que
 * d'inscrire des candidats à des compétition.
 */

public class Inscriptions implements Serializable {
	private static final long serialVersionUID = -3095339436048473524L;
	private static final String FILE_NAME = "Inscriptions.srz";
	private static Inscriptions inscriptions;

	private SortedSet<Competition> competitions = new TreeSet<>();
	private SortedSet<Candidat> candidats = new TreeSet<>();

	private Inscriptions() {
	}

	/**
	 * Retourne les compétitions.
	 * 
	 * @return
	 */

	public SortedSet<Competition> getCompetitions() {
		Session session = HibernateFactory.openSession();
		List<Competition> competitions = session.createQuery("SELECT c FROM Competition c", Competition.class)
				.getResultList();
		session.close();
		return new TreeSet<>(competitions);
	}

	/**
	 * Retourne tous les candidats (personnes et équipes confondues).
	 * 
	 * @return
	 */

	public SortedSet<Candidat> getCandidats() {
		Session session = HibernateFactory.openSession();
		List<Candidat> candidats = session.createQuery("SELECT c FROM Candidat c", Candidat.class)
				.getResultList();
		session.close();
		return new TreeSet<>(candidats);
	}

	/**
	 * Retourne toutes les personnes.
	 * 
	 * @return
	 */

	public SortedSet<Personne> getPersonnes() {
		Session session = HibernateFactory.openSession();
		List<Personne> personnes = session.createQuery("SELECT p FROM Personne p", Personne.class)
				.getResultList();
		session.close();
		return new TreeSet<>(personnes);
	}

	/**
	 * Retourne toutes les équipes.
	 * 
	 * @return
	 */

	public SortedSet<Equipe> getEquipes() {
		SortedSet<Equipe> equipes = new TreeSet<>();
		for (Candidat c : getCandidats())
			if (c instanceof Equipe)
				equipes.add((Equipe) c);
		return Collections.unmodifiableSortedSet(equipes);
	}

	/**
	 * Créée une compétition. Ceci est le seul moyen, il n'y a pas de
	 * constructeur public dans {@link Competition}.
	 * 
	 * @param nom
	 * @param dateCloture
	 * @param enEquipe
	 * @return
	 */

	public Competition createCompetition(String nom, LocalDate dateCloture, boolean enEquipe) {
		Competition competition = new Competition(this, nom, dateCloture, enEquipe);
		Session sesion = HibernateFactory.openSession();
		sesion.save(competition);
		return competition;
	}

	/**
	 * Créée un Candidat de type Personne. Ceci est le seul moyen, il n'y a pas de
	 * constructeur public dans {@link Personne}.
	 * 
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */

	public Personne createPersonne(String nom, String prenom, String mail) {
		Personne personne = new Personne(this, nom, prenom, mail);
		candidats.add(personne);
		return personne;
	}

	/**
	 * Créée un Candidat de type équipe. Ceci est le seul moyen, il n'y a pas de
	 * constructeur public dans {@link Equipe}.
	 * 
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */

	public Equipe createEquipe(String nom) {
		Equipe equipe = new Equipe(this, nom);
		candidats.add(equipe);
		return equipe;
	}

	void delete(Competition competition) {
		competitions.remove(competition);
	}

	void delete(Candidat candidat) {
		candidats.remove(candidat);
	}

	/**
	 * Retourne l'unique instance de cette classe. Crée cet objet s'il n'existe
	 * déjà.
	 * 
	 * @return l'unique objet de type {@link Inscriptions}.
	 */

	public static Inscriptions getInscriptions() {

		if (inscriptions == null) {
			inscriptions = readObject();
			if (inscriptions == null)
				inscriptions = new Inscriptions();
		}
		return inscriptions;
	}

	/**
	 * Retourne un object inscriptions vide. Ne modifie pas les compétitions et
	 * candidats déjà existants.
	 */

	public Inscriptions reinitialiser() {
		inscriptions = new Inscriptions();
		return getInscriptions();
	}

	/**
	 * Efface toutes les modifications sur Inscriptions depuis la dernière
	 * sauvegarde. Ne modifie pas les compétitions et candidats déjà existants.
	 */

	public Inscriptions recharger() {
		inscriptions = null;
		return getInscriptions();
	}

	private static Inscriptions readObject() {
		ObjectInputStream ois = null;
		try {
			FileInputStream fis = new FileInputStream(FILE_NAME);
			ois = new ObjectInputStream(fis);
			return (Inscriptions) (ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			return null;
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement lors d'une
	 * exécution ultérieure du programme.
	 * 
	 * @throws IOException
	 */

	public void sauvegarder() throws IOException {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fis = new FileOutputStream(FILE_NAME);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(this);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public String toString() {
		return "Candidats : " + getCandidats().toString() + "\nCompetitions  " + getCompetitions().toString();
	}

	public static void main(String[] args) {
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/inscriptions_sportives", user = "", password = "";
			c = DriverManager.getConnection(url, user, password);
			String req = "select * from test";
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(req);
			while (rs.next()) {
				System.out.println(rs.getInt(1) + " : " + rs.getString(2));
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Pilote JDBC non install�.");
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				System.out.println("Impossible de fermer la connection.");
			}
		}

		Inscriptions inscriptions = Inscriptions.getInscriptions();
		Competition flechettes = inscriptions.createCompetition("Mondial de fléchettes", null, false);
		Personne tony = inscriptions.createPersonne("Tony", "Dent de plomb", "azerty"),
				boris = inscriptions.createPersonne("Boris", "le Hachoir", "ytreza");
		flechettes.add(tony);
		Equipe lesManouches = inscriptions.createEquipe("Les Manouches");
		lesManouches.add(boris);
		lesManouches.add(tony);
		System.out.println(inscriptions);
		lesManouches.delete();
		System.out.println(inscriptions);
		try {
			inscriptions.sauvegarder();
		} catch (IOException e) {
			System.out.println("Sauvegarde impossible." + e);
		}
	}
}
