package inscriptions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

/**
 * Représente une compétition, c'est-à-dire un ensemble de candidats inscrits
 * à un événement, les inscriptions sont closes à la date dateCloture.
 *
 */

@Entity
public class Competition implements Comparable<Competition>, Serializable {
	private static final long serialVersionUID = -2882150118573759729L;
	@Transient
	private Inscriptions inscriptions;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column()
	private String nom;
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "competitions")
	private Set<Candidat> candidats;

	@Column(nullable = false)
	private LocalDate dateCloture;

	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean enEquipe = false;

	public Competition() {
		
	}
	Competition(Inscriptions inscriptions, String nom, LocalDate dateCloture, boolean enEquipe) {
		this.enEquipe = enEquipe;
		this.inscriptions = inscriptions;
		this.nom = nom;
		this.dateCloture = dateCloture;
		candidats = new TreeSet<>();
	}

	/**
	 * Retourne le nom de la compétition.
	 * 
	 * @return
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnEquipe() {
		return enEquipe;
	}

	public void setEnEquipe(boolean enEquipe) {
		this.enEquipe = enEquipe;
	}

	public void setCandidats(Set<Candidat> candidats) {
		this.candidats = candidats;
	}

	public String getNom() {
		return nom;
	}

	/**
	 * Modifie le nom de la compétition.
	 */

	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Retourne vrai si les inscriptions sont encore ouvertes, faux si les
	 * inscriptions sont closes.
	 * 
	 * @return
	 */

	public boolean inscriptionsOuvertes() {
		return getDateCloture().isAfter(LocalDate.now());
	}

	/**
	 * Retourne la date de cloture des inscriptions.
	 * 
	 * @return
	 */

	public LocalDate getDateCloture() {
		return dateCloture;
	}

	/**
	 * Est vrai si et seulement si les inscriptions sont réservées aux équipes.
	 * 
	 * @return
	 */

	public boolean estEnEquipe() {
		return enEquipe;
	}

	/**
	 * Modifie la date de cloture des inscriptions. Il est possible de la reculer
	 * mais pas de l'avancer.
	 * 
	 * @param dateCloture
	 */

	public void setDateCloture(LocalDate dateCloture) {
		if (getDateCloture().isBefore(dateCloture)) {
			this.dateCloture = dateCloture;
		}

	}

	/**
	 * Retourne l'ensemble des candidats inscrits.
	 * 
	 * @return
	 */

	public Set<Candidat> getCandidats() {
		return Collections.unmodifiableSet(candidats);
	}

	/**
	 * Inscrit un candidat de type Personne à la compétition. Provoque une
	 * exception si la compétition est réservée aux équipes ou que les
	 * inscriptions sont closes.
	 * 
	 * @param personne
	 * @return
	 */

	public boolean add(Personne personne) {
		if (enEquipe || !inscriptionsOuvertes()) {
			throw new RuntimeException("La competition est close ou est r�serv� aux �quipes.");
		}

		personne.add(this);

		return candidats.add(personne);
	}

	/**
	 * Inscrit un candidat de type Equipe à la compétition. Provoque une exception
	 * si la compétition est réservée aux personnes ou que les inscriptions sont
	 * closes.
	 * 
	 * @param personne
	 * @return
	 */

	public boolean add(Equipe equipe) {
		if (!enEquipe || !inscriptionsOuvertes()) {
			throw new RuntimeException("La competition est close ou individuel.");
		}

		equipe.add(this);

		return candidats.add(equipe);
	}

	/**
	 * Retourne les Candidats que l'on peut inscrire à cette competition.
	 * 
	 * @return les candidats que l'on peut inscrire à cette compétition.
	 */

	public Set<Candidat> getCandidatsAInscrire() {
		Set<Candidat> listeDesCandidatsDisponible = new TreeSet<>();

		for (Candidat candidat : inscriptions.getCandidats()) {
			if (!(getCandidats()).contains(candidat)) {
				listeDesCandidatsDisponible.add(candidat);
			}
		}

		return listeDesCandidatsDisponible;
	}

	/**
	 * Désinscrit un candidat.
	 * 
	 * @param candidat
	 * @return
	 */

	public boolean remove(Candidat candidat) {
		candidat.remove(this);
		return candidats.remove(candidat);
	}

	/**
	 * Supprime la compétition de l'application.
	 */

	public void delete() {
		for (Candidat candidat : candidats)
			remove(candidat);
		inscriptions.delete(this);
	}

	@Override
	public int compareTo(Competition o) {
		return getNom().compareTo(o.getNom());
	}

	@Override
	public String toString() {
		return getNom();
	}
}
